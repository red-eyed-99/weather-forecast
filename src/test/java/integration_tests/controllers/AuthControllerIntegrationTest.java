package integration_tests.controllers;

import annotations.WebIntegrationTest;
import dto.SignInUserDTO;
import dto.SignUpUserDTO;
import exceptions.BadCredentialsException;
import exceptions.UserAlreadyExistException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import models.entities.UserSession;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import services.AuthService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static dto.SignUpUserDTO.Fields.PASSWORD;
import static dto.SignUpUserDTO.Fields.REPEAT_PASSWORD;
import static dto.SignUpUserDTO.Fields.USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static utils.CookieUtil.USER_SESSION_COOKIE;
import static utils.ModelAttributeUtil.ERROR_MESSAGE;
import static utils.ModelAttributeUtil.USER;
import static utils.ModelAttributeUtil.USER_SESSION;
import static utils.PagesUtil.REDIRECT_HOME;
import static utils.PagesUtil.SIGN_IN;
import static utils.PagesUtil.SIGN_UP;
import static utils.SqlScriptUtil.INSERT_EXPIRED_SESSION;
import static utils.SqlScriptUtil.INSERT_SESSION;
import static utils.SqlScriptUtil.INSERT_USER;
import static utils.SqlScriptUtil.USER_SESSION_ID;

@WebIntegrationTest
@RequiredArgsConstructor
class AuthControllerIntegrationTest {

    private static final String SIGN_UP_URL = "/sign-up";
    private static final String SIGN_IN_URL = "/sign-in";
    private static final String LOGOUT_URL = "/logout";

    private final WebApplicationContext webApplicationContext;

    @MockitoBean
    private final AuthService authService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Nested
    class SignUpTest {

        @Test
        @DisplayName("Get /sign-up with active session")
        @Sql(scripts = {INSERT_USER, INSERT_SESSION})
        void getSignUpPage_sessionIsActive_redirectToHomePage() throws Exception {
            var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

            mockMvc.perform(get(SIGN_UP_URL)
                            .cookie(cookie))
                    .andExpectAll(
                            status().is3xxRedirection(),
                            view().name(REDIRECT_HOME)
                    );
        }

        @Test
        @DisplayName("Get /sign-up with no session")
        void getSignUpPage_noSession_returnSignUpPage() throws Exception {
            mockMvc.perform(get(SIGN_UP_URL))
                    .andExpectAll(
                            status().isOk(),
                            model().attributeDoesNotExist(USER_SESSION),
                            view().name(SIGN_UP)
                    );
        }

        @Test
        @DisplayName("Get /sign-up with expired session")
        @Sql(scripts = {INSERT_USER, INSERT_EXPIRED_SESSION})
        void getSignUpPage_sessionExpired_returnSignUpPage() throws Exception {
            var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

            mockMvc.perform(get(SIGN_UP_URL)
                            .cookie(cookie))
                    .andExpectAll(
                            status().isOk(),
                            model().attributeDoesNotExist(USER_SESSION),
                            view().name(SIGN_UP)
                    );
        }

        @ParameterizedTest
        @DisplayName("Post /sign-up with invalid user data")
        @MethodSource(value = "utils.SignUpTestDataProvider#getSignUpInvalidData")
        void postSignUp_invalidUserData_returnSignUpPageWithErrors(String username, String password,
                                                                   String repeatPassword,
                                                                   String[] errorFields,
                                                                   String[] errorAttributes) throws Exception {
            mockMvc.perform(post(SIGN_UP_URL)
                            .formField(USERNAME, username)
                            .formField(PASSWORD, password)
                            .formField(REPEAT_PASSWORD, repeatPassword))
                    .andExpectAll(
                            model().attributeHasFieldErrors(USER, errorFields),
                            model().attributeExists(errorAttributes),
                            status().isBadRequest(),
                            view().name(SIGN_UP)
                    );
        }

        @Test
        @DisplayName("Post /sign-up with success response")
        void postSignUp_success_redirectHomePageWithSession() throws Exception {
            var sessionId = UUID.randomUUID();
            var sessionExpiresAt = LocalDateTime.now().plusDays(3);
            var userSession = new UserSession(sessionId, null, sessionExpiresAt);

            doReturn(userSession).when(authService).signUp(any(SignUpUserDTO.class));

            mockMvc.perform(post(SIGN_UP_URL)
                            .formField(USERNAME, "username")
                            .formField(PASSWORD, "password")
                            .formField(REPEAT_PASSWORD, "password"))
                    .andExpectAll(
                            status().is3xxRedirection(),
                            cookie().exists(USER_SESSION_COOKIE),
                            cookie().value(USER_SESSION_COOKIE, sessionId.toString()),
                            cookie().maxAge(USER_SESSION_COOKIE, getMaxAge(sessionExpiresAt)),
                            cookie().path(USER_SESSION_COOKIE, "/"),
                            cookie().httpOnly(USER_SESSION_COOKIE, true),
                            view().name(REDIRECT_HOME)
                    );
        }

        @Test
        @DisplayName("Post /sign-up when user already exist")
        void postSignUp_userAlreadyExist_returnSignUpPageWithError() throws Exception {
            var userAlreadyExistException = new UserAlreadyExistException();
            doThrow(userAlreadyExistException).when(authService).signUp(any(SignUpUserDTO.class));

            mockMvc.perform(post(SIGN_UP_URL)
                            .formField(USERNAME, "username")
                            .formField(PASSWORD, "password")
                            .formField(REPEAT_PASSWORD, "password"))
                    .andExpectAll(
                            status().isConflict(),
                            model().attribute(ERROR_MESSAGE, userAlreadyExistException.getMessage()),
                            view().name(SIGN_UP)
                    );
        }
    }

    @Nested
    class SignInTest {

        @Test
        @DisplayName("Get /sign-in with active session")
        @Sql(scripts = {INSERT_USER, INSERT_SESSION})
        void getSignInPage_sessionIsActive_redirectToHomePage() throws Exception {
            var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

            mockMvc.perform(get(SIGN_IN_URL)
                            .cookie(cookie))
                    .andExpectAll(
                            status().is3xxRedirection(),
                            view().name(REDIRECT_HOME)
                    );
        }

        @Test
        @DisplayName("Get /sign-in with no session")
        void getSignInPage_noSession_returnSignInPage() throws Exception {
            mockMvc.perform(get(SIGN_IN_URL))
                    .andExpectAll(
                            status().isOk(),
                            model().attributeDoesNotExist(USER_SESSION),
                            view().name(SIGN_IN)
                    );
        }

        @Test
        @DisplayName("Get /sign-in with expired session")
        @Sql(scripts = {INSERT_USER, INSERT_EXPIRED_SESSION})
        void getSignInPage_sessionExpired_returnSignInPage() throws Exception {
            var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

            mockMvc.perform(get(SIGN_IN_URL)
                            .cookie(cookie))
                    .andExpectAll(
                            status().isOk(),
                            model().attributeDoesNotExist(USER_SESSION),
                            view().name(SIGN_IN)
                    );
        }

        @ParameterizedTest
        @DisplayName("Post /sign-in with invalid data")
        @MethodSource(value = "utils.SignInTestDataProvider#getSignInInvalidData")
        void postSignIn_invalidData_returnSignInPageWithErrors(String username, String password,
                                                               String[] errorFields,
                                                               String[] errorAttributes) throws Exception {
            mockMvc.perform(post(SIGN_IN_URL)
                            .formField(USERNAME, username)
                            .formField(PASSWORD, password))
                    .andExpectAll(
                            model().attributeHasFieldErrors(USER, errorFields),
                            model().attributeExists(errorAttributes),
                            status().isBadRequest(),
                            view().name(SIGN_IN)
                    );
        }

        @Test
        @DisplayName("Post /sign-in with success response")
        void postSignIn_success_redirectHomePageWithSession() throws Exception {
            var sessionId = UUID.randomUUID();
            var sessionExpiresAt = LocalDateTime.now().plusDays(3);
            var userSession = new UserSession(sessionId, null, sessionExpiresAt);

            doReturn(userSession).when(authService).signIn(any(SignInUserDTO.class));

            mockMvc.perform(post(SIGN_IN_URL)
                            .formField(USERNAME, "username")
                            .formField(PASSWORD, "password"))
                    .andExpectAll(
                            status().is3xxRedirection(),
                            cookie().exists(USER_SESSION_COOKIE),
                            cookie().value(USER_SESSION_COOKIE, sessionId.toString()),
                            cookie().maxAge(USER_SESSION_COOKIE, getMaxAge(sessionExpiresAt)),
                            cookie().path(USER_SESSION_COOKIE, "/"),
                            cookie().httpOnly(USER_SESSION_COOKIE, true),
                            view().name(REDIRECT_HOME)
                    );
        }

        @Test
        @DisplayName("Post /sign-in when bad credentials")
        void postSignIn_badCredentials_returnSignInPageWithError() throws Exception {
            var username = "username";
            var badCredentialsException = new BadCredentialsException(username);
            var expectedSignInUserDto = new SignInUserDTO(username, "");

            doThrow(badCredentialsException).when(authService).signIn(any(SignInUserDTO.class));

            mockMvc.perform(post(SIGN_IN_URL)
                            .formField(USERNAME, username)
                            .formField(PASSWORD, "password"))
                    .andExpectAll(
                            status().isUnauthorized(),
                            model().attribute(ERROR_MESSAGE, badCredentialsException.getMessage()),
                            model().attribute(USER, new IsEqual<>(expectedSignInUserDto)),
                            view().name(SIGN_IN)
                    );
        }
    }

    @Nested
    class LogoutTest {

        @Test
        @DisplayName("Post /logout with active session")
        @Sql(scripts = {INSERT_USER, INSERT_SESSION})
        void postLogout_sessionIsActive_redirectToHomePage() throws Exception {
            var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

            mockMvc.perform(post(LOGOUT_URL)
                            .cookie(cookie))
                    .andExpectAll(
                            status().is3xxRedirection(),
                            cookie().exists(USER_SESSION_COOKIE),
                            cookie().value(USER_SESSION_COOKIE, new IsNull<>()),
                            cookie().maxAge(USER_SESSION_COOKIE, 0),
                            cookie().path(USER_SESSION_COOKIE, "/"),
                            view().name(REDIRECT_HOME)
                    );
        }

        @Test
        @DisplayName("Post /logout with no session")
        void postLogout_noSession_redirectHomePage() throws Exception {
            mockMvc.perform(post(LOGOUT_URL))
                    .andExpectAll(
                            status().is3xxRedirection(),
                            model().attributeDoesNotExist(USER_SESSION),
                            view().name(REDIRECT_HOME)
                    );
        }
    }

    private static int getMaxAge(LocalDateTime expiresAt) {
        var now = ZonedDateTime.now(ZoneOffset.UTC);
        var expires = expiresAt.atZone(ZoneOffset.UTC);
        return (int) Duration.between(now, expires).getSeconds();
    }
}
