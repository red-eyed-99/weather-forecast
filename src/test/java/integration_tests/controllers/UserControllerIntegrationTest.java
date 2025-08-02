package integration_tests.controllers;

import annotations.WebIntegrationTest;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import services.UserService;

import static dto.openweather.CoordinatesDTO.Fields.LATITUDE;
import static dto.openweather.CoordinatesDTO.Fields.LONGITUDE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static utils.CookieUtil.USER_SESSION_COOKIE;
import static utils.ModelAttributeUtil.ERROR_MESSAGE;
import static utils.PagesUtil.REDIRECT_HOME;
import static utils.PagesUtil.SIGN_IN;
import static utils.SqlScriptUtil.INSERT_SESSION;
import static utils.SqlScriptUtil.INSERT_USER;
import static utils.SqlScriptUtil.USER_SESSION_ID;

@WebIntegrationTest
@RequiredArgsConstructor
public class UserControllerIntegrationTest {

    private static final String USERS_LOCATIONS_URL = "/users/locations";

    private final WebApplicationContext webApplicationContext;

    @MockitoBean
    private final UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @ParameterizedTest
    @DisplayName("Post " + USERS_LOCATIONS_URL + " to add location to an authorized user by coordinates")
    @Sql(scripts = {INSERT_USER, INSERT_SESSION})
    @CsvFileSource(resources = "/data/correct_location_coordinates.csv", numLinesToSkip = 1)
    @SneakyThrows
    void addLocationToUserByCoordinates_userAuthorized_shouldAddLocationAndRedirectToHomePage(String longitude,
                                                                                              String latitude) {
        var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

        mockMvc.perform(post(USERS_LOCATIONS_URL)
                        .cookie(cookie)
                        .formField(LONGITUDE, longitude)
                        .formField(LATITUDE, latitude))
                .andExpectAll(
                        view().name(REDIRECT_HOME),
                        status().is3xxRedirection()
                );
    }

    @Test
    @DisplayName("Post " + USERS_LOCATIONS_URL + " to add location to an unauthorized user by coordinates")
    void addLocationToUserByCoordinates_userUnauthorized_shouldReturnSignInPageWithError() throws Exception {
        mockMvc.perform(post(USERS_LOCATIONS_URL)
                        .formField(LONGITUDE, "180")
                        .formField(LATITUDE, "90"))
                .andExpectAll(
                        model().attributeExists(ERROR_MESSAGE),
                        view().name(SIGN_IN),
                        status().isUnauthorized()
                );
    }

    @ParameterizedTest
    @DisplayName("Post " + USERS_LOCATIONS_URL + " with incorrect location coordinates")
    @CsvFileSource(resources = "/data/incorrect_location_coordinates.csv", numLinesToSkip = 1)
    @SneakyThrows
    void addLocationToUserByCoordinates_incorrectCoordinates_statusIsBadRequest(String longitude, String latitude) {
        mockMvc.perform(post(USERS_LOCATIONS_URL)
                        .formField(LONGITUDE, longitude)
                        .formField(LATITUDE, latitude))
                .andExpect(status().isBadRequest());
    }
}