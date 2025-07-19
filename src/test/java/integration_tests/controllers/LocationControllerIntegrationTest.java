package integration_tests.controllers;

import annotations.WebIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.openweather.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
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

    @MockitoBean
    private final RestTemplate restTemplate;

    @Value("${openWeather.url}")
    private String openWeatherUrl;

    @Value("${openWeather.key}")
    private String openWeatherKey;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Nested
    @DisplayName("Search location tests")
    class SearchLocationTest {

        @Test
        @DisplayName("Get /locations with success response")
        void searchLocation_locationExists_returnSearchLocationsPageWithWeatherInfo() throws Exception {
            var locationName = "Moscow";
            var openWeatherResponse = new ClassPathResource("openweather/success_response.json").getInputStream();
            var weatherResponseDto = new ObjectMapper().readValue(openWeatherResponse, WeatherResponseDTO.class);

            doReturn(weatherResponseDto)
                    .when(restTemplate)
                    .getForObject(getUri(locationName), WeatherResponseDTO.class);

            mockMvc.perform(get(SEARCH_LOCATIONS_URL)
                            .queryParam(LOCATION_NAME_PARAMETER, locationName))
                    .andExpectAll(
                            model().attribute(LOCATION_WEATHER, weatherResponseDto),
                            view().name(SEARCH_LOCATIONS),
                            status().isOk()
                    );
        }

        @Test
        @DisplayName("Get /locations when location not found")
        void searchLocation_locationNotFound_returnSearchLocationsPageWithNotFoundMessage() throws Exception {
            var locationName = "dummy";
            var httpClientErrorException = new HttpClientErrorException(HttpStatusCode.valueOf(404));

            doThrow(httpClientErrorException)
                    .when(restTemplate)
                    .getForObject(getUri(locationName), WeatherResponseDTO.class);

            mockMvc.perform(get(SEARCH_LOCATIONS_URL)
                            .queryParam(LOCATION_NAME_PARAMETER, locationName))
                    .andExpectAll(
                            model().attribute(ERROR_MESSAGE, "Location not found"),
                            view().name(SEARCH_LOCATIONS),
                            status().isNotFound()
                    );
        }

        @ParameterizedTest
        @DisplayName("Get /locations when open weather returns errors")
        @ValueSource(ints = {400, 405, 500, 502, 503})
        void searchLocation_openWeatherReturnsErrors_returnHomePageWithErrorMessage(int statusCode) throws Exception {
            var locationName = "dummy";
            var httpStatusCode = HttpStatusCode.valueOf(statusCode);
            var httpClientErrorException = new HttpClientErrorException(httpStatusCode);

            doThrow(httpClientErrorException)
                    .when(restTemplate)
                    .getForObject(getUri(locationName), WeatherResponseDTO.class);

            mockMvc.perform(get(SEARCH_LOCATIONS_URL)
                            .queryParam(LOCATION_NAME_PARAMETER, locationName))
                    .andExpectAll(
                            model().attribute(ERROR_MESSAGE, "Unable to retrieve weather data, please try again later"),
                            view().name(HOME),
                            status().isInternalServerError()
                    );
        }

        @ParameterizedTest
        @DisplayName("Get /location/search with incorrect location name")
        @CsvFileSource(resources = "/data/incorrect_location_names.csv")
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
        @DisplayName("Get /location/search with correct location name")
        @CsvFileSource(resources = "/data/correct_location_names.csv")
        void searchLocation_correctLocationName_shouldReturnSearchLocationsPageWithoutErrors(String locationName) throws Exception {
            doReturn(null)
                    .when(restTemplate)
                    .getForObject(getUri(locationName), WeatherResponseDTO.class);

            mockMvc.perform(get(SEARCH_LOCATIONS_URL)
                            .queryParam(LOCATION_NAME_PARAMETER, locationName))
                    .andExpectAll(
                            model().attributeDoesNotExist(ERROR_MESSAGE),
                            view().name(SEARCH_LOCATIONS),
                            status().isOk()
                    );
        }

        private String getUri(String locationName) {
            return UriComponentsBuilder.newInstance()
                    .uri(URI.create(openWeatherUrl))
                    .queryParam("q", locationName)
                    .queryParam("units", "metric")
                    .queryParam("appid", openWeatherKey)
                    .toUriString();
        }
    }
}