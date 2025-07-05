package integration_tests;

import annotations.IntegrationTest;
import dto.auth.SignInUserDTO;
import exceptions.BadCredentialsException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import services.AuthService;
import services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.SqlScriptUtil.INSERT_USER;

@IntegrationTest
@RequiredArgsConstructor
class SignInIntegrationTest {

    private final AuthService authService;

    private final UserService userService;

    @Test
    @DisplayName(value = "User exists")
    @Sql(scripts = INSERT_USER)
    void shouldSuccessfullyLoginUser() {
        var username = "dummy";
        var signInUserDto = new SignInUserDTO(username, "dummy");

        var user = userService.findByUsername(username);
        var userSession = authService.signIn(signInUserDto);

        assertEquals(user.getId(), userSession.getUser().getId());
    }

    @Test
    @DisplayName(value = "User doesn't exist")
    void userDoesNotExist_throwsBadCredentialsException() {
        var signInUserDto = new SignInUserDTO("dummy", "dummy");
        assertThrows(BadCredentialsException.class, () -> authService.signIn(signInUserDto));
    }

    @Test
    @DisplayName(value = "Incorrect user password")
    @Sql(scripts = INSERT_USER)
    void incorrectUserPassword_throwsBadCredentialsException() {
        var signInUserDto = new SignInUserDTO("dummy", "incorrectPassword");
        assertThrows(BadCredentialsException.class, () -> authService.signIn(signInUserDto));
    }
}
