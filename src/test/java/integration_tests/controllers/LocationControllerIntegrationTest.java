package integration_tests.controllers;

import annotations.WebIntegrationTest;
import components.OpenWeatherUriBuilder;
import dto.openweather.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import utils.OpenWeatherTestData;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static utils.LocationTestData.MOSCOW;
import static utils.ModelAttributeUtil.ERROR_MESSAGE;
import static utils.ModelAttributeUtil.LOCATION_WEATHER;
import static utils.PagesUtil.HOME;
import static utils.PagesUtil.SEARCH_LOCATIONS;

@WebIntegrationTest
@RequiredArgsConstructor
class LocationControllerIntegrationTest {

    private static final String SEARCH_LOCATIONS_URL = "/locations";
    private static final String LOCATION_NAME_PARAMETER = "locationName";

    private final WebApplicationContext webApplicationContext;

    private final OpenWeatherUriBuilder openWeatherUriBuilder;

    @MockitoBean
    private final RestTemplate restTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Nested
    @DisplayName("Search location tests")
    class SearchLocationTest {

        @Test
        @DisplayName("Get " + SEARCH_LOCATIONS_URL + " with success response")
        void searchLocation_locationExists_returnSearchLocationsPageWithWeatherInfo() throws Exception {
            var weatherResponseDto = OpenWeatherTestData.getWeatherResponseDto(MOSCOW);

            var uri = openWeatherUriBuilder.build(MOSCOW);

            doReturn(weatherResponseDto)
                    .when(restTemplate)
                    .getForObject(uri, WeatherResponseDTO.class);

            mockMvc.perform(get(SEARCH_LOCATIONS_URL)
                            .queryParam(LOCATION_NAME_PARAMETER, MOSCOW))
                    .andExpectAll(
                            model().attribute(LOCATION_WEATHER, weatherResponseDto),
                            view().name(SEARCH_LOCATIONS),
                            status().isOk()
                    );
        }

        @Test
        @DisplayName("Get " + SEARCH_LOCATIONS_URL + " when location not found")
        void searchLocation_locationNotFound_returnSearchLocationsPageWithNotFoundMessage() throws Exception {
            var httpClientErrorException = new HttpClientErrorException(HttpStatusCode.valueOf(404));

            var uri = openWeatherUriBuilder.build("UNKNOWN-LOCATION");

            doThrow(httpClientErrorException)
                    .when(restTemplate)
                    .getForObject(uri, WeatherResponseDTO.class);

            mockMvc.perform(get(SEARCH_LOCATIONS_URL)
                            .queryParam(LOCATION_NAME_PARAMETER, "UNKNOWN-LOCATION"))
                    .andExpectAll(
                            model().attribute(ERROR_MESSAGE, "Location not found"),
                            view().name(SEARCH_LOCATIONS),
                            status().isNotFound()
                    );
        }

        @ParameterizedTest
        @DisplayName("Get " + SEARCH_LOCATIONS_URL + " when open weather returns errors")
        @ValueSource(ints = {400, 405, 500, 502, 503})
        void searchLocation_openWeatherReturnsErrors_returnHomePageWithErrorMessage(int statusCode) throws Exception {
            var httpStatusCode = HttpStatusCode.valueOf(statusCode);
            var httpClientErrorException = new HttpClientErrorException(httpStatusCode);

            var uri = openWeatherUriBuilder.build(MOSCOW);

            doThrow(httpClientErrorException)
                    .when(restTemplate)
                    .getForObject(uri, WeatherResponseDTO.class);

            mockMvc.perform(get(SEARCH_LOCATIONS_URL)
                            .queryParam(LOCATION_NAME_PARAMETER, MOSCOW))
                    .andExpectAll(
                            model().attribute(ERROR_MESSAGE, "Unable to retrieve weather data, please try again later"),
                            view().name(HOME),
                            status().isInternalServerError()
                    );
        }

        @ParameterizedTest
        @DisplayName("Get " + SEARCH_LOCATIONS_URL + " with incorrect location name")
        @CsvFileSource(resources = "/data/incorrect_location_names_with_message.csv")
        void searchLocation_incorrectLocationName_returnSearchLocationsPageWithErrorMessage(
                String locationName, String expectedErrorMessage) throws Exception {

            mockMvc.perform(get(SEARCH_LOCATIONS_URL)
                            .queryParam(LOCATION_NAME_PARAMETER, locationName))
                    .andExpectAll(
                            model().attribute(ERROR_MESSAGE, expectedErrorMessage),
                            view().name(SEARCH_LOCATIONS),
                            status().isBadRequest()
                    );
        }

        @ParameterizedTest
        @DisplayName("Get " + SEARCH_LOCATIONS_URL + " with correct location name")
        @CsvFileSource(resources = "/data/correct_location_names.csv")
        void searchLocation_correctLocationName_shouldReturnSearchLocationsPageWithoutErrors(String locationName) throws Exception {
            var uri = openWeatherUriBuilder.build(locationName);

            doReturn(null)
                    .when(restTemplate)
                    .getForObject(uri, WeatherResponseDTO.class);

            mockMvc.perform(get(SEARCH_LOCATIONS_URL)
                            .queryParam(LOCATION_NAME_PARAMETER, locationName))
                    .andExpectAll(
                            model().attributeDoesNotExist(ERROR_MESSAGE),
                            view().name(SEARCH_LOCATIONS),
                            status().isOk()
                    );
        }
    }
}