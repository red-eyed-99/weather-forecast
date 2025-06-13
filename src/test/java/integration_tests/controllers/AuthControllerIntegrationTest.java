package integration_tests.controllers;

import annotations.WebIntegrationTest;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static utils.CookieUtil.USER_SESSION_COOKIE;
import static utils.ModelAttributeUtil.USER_SESSION;
import static utils.PagesUtil.REDIRECT_HOME;
import static utils.PagesUtil.SIGN_UP;
import static utils.SqlScriptUtil.INSERT_EXPIRED_SESSION;
import static utils.SqlScriptUtil.INSERT_SESSION;
import static utils.SqlScriptUtil.INSERT_USER;
import static utils.SqlScriptUtil.USER_SESSION_ID;

@WebIntegrationTest
@RequiredArgsConstructor
class AuthControllerIntegrationTest {

    private static final String SIGN_UP_URL = "/sign-up";

    private final WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Nested
    class SignUpTest {

        @Test
        @DisplayName("Get /sign-up with active session")
        @Sql(scripts = {INSERT_USER, INSERT_SESSION})
        public void getSignUpPage_sessionIsActive_redirectToHomePage() throws Exception {
            var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

            mockMvc.perform(get(SIGN_UP_URL)
                            .cookie(cookie))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name(REDIRECT_HOME));
        }

        @Test
        @DisplayName("Get /sign-up with no session")
        public void getSignUpPage_noSession_returnSignUpPage() throws Exception {
            mockMvc.perform(get(SIGN_UP_URL))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeDoesNotExist(USER_SESSION))
                    .andExpect(view().name(SIGN_UP));
        }

        @Test
        @DisplayName("Get /sign-up with expired session")
        @Sql(scripts = {INSERT_USER, INSERT_EXPIRED_SESSION})
        public void getSignUpPage_sessionExpired_returnSignUpPage() throws Exception {
            var cookie = new Cookie(USER_SESSION_COOKIE, USER_SESSION_ID);

            mockMvc.perform(get(SIGN_UP_URL)
                            .cookie(cookie))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeDoesNotExist(USER_SESSION))
                    .andExpect(view().name(SIGN_UP));
        }
    }

}
