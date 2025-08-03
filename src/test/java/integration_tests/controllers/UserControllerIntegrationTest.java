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
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import services.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static utils.CookieUtil.USER_SESSION_COOKIE;
import static utils.LocationTestData.MOSCOW;
import static utils.ModelAttributeUtil.ERROR_MESSAGE;
import static utils.PagesUtil.REDIRECT_HOME;
import static utils.PagesUtil.SIGN_IN;
import static utils.SqlScriptUtil.INSERT_SESSION;
import static utils.SqlScriptUtil.INSERT_USER;
import static utils.UserTestData.USER_SESSION_ID;

@WebIntegrationTest
@RequiredArgsConstructor
public class UserControllerIntegrationTest {

    private static final String USERS_ADD_LOCATION_URL = "/users/add-location";

    private final WebApplicationContext webApplicationContext;

    @MockitoBean
    private final UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @ParameterizedTest
    @DisplayName("Post " + USERS_ADD_LOCATION_URL + " to add location to an authorized user")
    @Sql(scripts = {INSERT_USER, INSERT_SESSION})
    @CsvFileSource(resources = "/data/correct_location_names.csv")
    @SneakyThrows
    void addLocationToUser_userAuthorized_shouldAddLocationAndRedirectToHomePage(String locationName) {
        var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

        mockMvc.perform(post(USERS_ADD_LOCATION_URL)
                        .cookie(cookie)
                        .formField("locationName", locationName))
                .andExpectAll(
                        view().name(REDIRECT_HOME),
                        status().is3xxRedirection()
                );
    }

    @Test
    @DisplayName("Post " + USERS_ADD_LOCATION_URL + " to add location to an unauthorized user")
    void addLocationToUser_userUnauthorized_shouldReturnSignInPageWithError() throws Exception {
        mockMvc.perform(post(USERS_ADD_LOCATION_URL)
                        .formField("locationName", MOSCOW))
                .andExpectAll(
                        model().attributeExists(ERROR_MESSAGE),
                        view().name(SIGN_IN),
                        status().isUnauthorized()
                );
    }

    @ParameterizedTest
    @DisplayName("Post " + USERS_ADD_LOCATION_URL + " with incorrect location name")
    @ValueSource(strings = {"Москва", "Mosква", "Mo$cow", "", "-,.’'&()/"})
    @Sql(scripts = {INSERT_USER, INSERT_SESSION})
    @SneakyThrows
    void addLocationToUser_incorrectLocationName_statusIsBadRequest(String locationName) {
        var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

        mockMvc.perform(post(USERS_ADD_LOCATION_URL)
                        .cookie(cookie)
                        .formField("locationName", locationName))
                .andExpect(status().isBadRequest());
    }
}