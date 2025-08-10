package integration_tests.controllers;

import annotations.WebIntegrationTest;
import components.OpenWeatherUriBuilder;
import dto.openweather.WeatherResponseDTO;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import utils.OpenWeatherTestData;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static utils.CookieUtil.USER_SESSION_COOKIE;
import static utils.LocationTestData.LONDON;
import static utils.LocationTestData.MOSCOW;
import static utils.ModelAttributeUtil.LOCATIONS_WEATHER;
import static utils.ModelAttributeUtil.USER_SESSION;
import static utils.PagesUtil.HOME;
import static utils.SqlScriptUtil.INSERT_LOCATIONS;
import static utils.SqlScriptUtil.INSERT_SESSION;
import static utils.SqlScriptUtil.INSERT_USER;
import static utils.SqlScriptUtil.INSERT_USERS_LOCATIONS;
import static utils.UserTestData.USER_SESSION_ID;

@WebIntegrationTest
@RequiredArgsConstructor
public class HomeControllerIntegrationTest {

    private static final String HOME_URL = "/";

    private final WebApplicationContext webApplicationContext;

    private final OpenWeatherUriBuilder openWeatherUriBuilder;

    @MockitoBean
    private final RestTemplate restTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Get home page when user is not authorized")
    void showHomePage_userUnauthorized_returnHomePage() throws Exception {
        mockMvc.perform(get(HOME_URL))
                .andExpectAll(
                        view().name(HOME),
                        status().isOk()
                );
    }

    @Test
    @DisplayName("Show home page when user is authorized but has no added locations")
    @Sql(scripts = {INSERT_USER, INSERT_SESSION})
    void showHomePage_userAuthorized_returnHomePageWithoutLocations() throws Exception {
        var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

        mockMvc.perform(get(HOME_URL)
                        .cookie(cookie))
                .andExpectAll(
                        model().attributeExists(USER_SESSION),
                        model().attributeDoesNotExist(LOCATIONS_WEATHER),
                        view().name(HOME),
                        status().isOk()
                );
    }

    @Test
    @DisplayName("Show home page with added locations when user is authorized")
    @Sql(scripts = {INSERT_USER, INSERT_SESSION, INSERT_LOCATIONS, INSERT_USERS_LOCATIONS})
    void showHomePage_userAuthorizedAndHasAddedLocations_returnHomePageWithAddedLocations() throws Exception {
        var weatherResponseDtos = List.of(
                OpenWeatherTestData.getWeatherResponseDto(MOSCOW),
                OpenWeatherTestData.getWeatherResponseDto(LONDON)
        );

        for (var weatherResponseDto : weatherResponseDtos) {
            var locationName = weatherResponseDto.getLocationDto().name();

            var uri = openWeatherUriBuilder.build(locationName);

            doReturn(weatherResponseDto)
                    .when(restTemplate)
                    .getForObject(uri, WeatherResponseDTO.class);
        }

        var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

        mockMvc.perform(get(HOME_URL)
                        .cookie(cookie))
                .andExpectAll(
                        model().attributeExists(USER_SESSION),
                        model().attributeExists(LOCATIONS_WEATHER),
                        view().name(HOME),
                        status().isOk()
                );
    }
}