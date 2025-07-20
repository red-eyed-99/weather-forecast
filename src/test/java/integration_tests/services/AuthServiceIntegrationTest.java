package integration_tests.services;

import annotations.IntegrationTest;
import dto.auth.SignInUserDTO;
import dto.auth.SignUpUserDTO;
import exceptions.BadCredentialsException;
import exceptions.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.jdbc.Sql;
import services.AuthService;
import services.UserService;
import services.UserSessionService;
import utils.PasswordEncoder;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.SqlScriptUtil.INSERT_SESSION;
import static utils.SqlScriptUtil.INSERT_USER;
import static utils.SqlScriptUtil.USER_SESSION_ID;

@IntegrationTest
@RequiredArgsConstructor
public class AuthServiceIntegrationTest {

    @Value("${userSession.lifetime-days}")
    private long sessionLifetime;

    private final AuthService authService;

    private final UserService userService;

    private final UserSessionService userSessionService;

    @Nested
    class SignUpTest {

        @Test
        @DisplayName(value = "User doesn't exist")
        public void shouldSuccessfullyRegisterUser() {
            var username = "dummy";
            var password = PasswordEncoder.encode("dummy");
            var signUpUserDto = new SignUpUserDTO(username, password, password);

            var userSession = authService.signUp(signUpUserDto);
            var expectedSessionExpiration = LocalDateTime.now().plusDays(sessionLifetime);
            var user = userService.findByUsername(username);

            assertAll(
                    () -> assertEquals(username, user.getUsername()),
                    () -> assertEquals(password, user.getPassword()),

                    () -> assertEquals(user.getId(), userSession.getUser().getId()),
                    () -> assertEquals(0, ChronoUnit.SECONDS.between(expectedSessionExpiration, userSession.getExpiresAt()))
            );
        }

        @Test
        @DisplayName(value = "User already exists")
        public void throwsUserAlreadyExistException() {
            var username = "dummy";
            var password = PasswordEncoder.encode("dummy");
            var signUpUserDto = new SignUpUserDTO(username, password, password);

            userService.save(signUpUserDto);

            assertThrows(UserAlreadyExistException.class, () -> authService.signUp(signUpUserDto));
        }
    }

    @Nested
    class SignInTest {

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

    @Nested
    class LogoutTest {

        @Test
        @DisplayName(value = "User session exists")
        @Sql(scripts = {INSERT_USER, INSERT_SESSION})
        void shouldSuccessfullyLogoutUser() {
            var sessionId = UUID.fromString(USER_SESSION_ID);
            var userSessionDtoOptional = userSessionService.findNotExpiredById(sessionId);
            var userSessionDto = userSessionDtoOptional.orElseThrow();
            var user = userService.findByUsername("dummy");

            authService.logout(userSessionDto);
            var userSessionDtoActual = userSessionService.findNotExpiredById(sessionId);

            assertAll(
                    () -> assertTrue(userSessionDtoActual.isEmpty()),
                    () -> assertEquals(user.getId(), userSessionDto.userId())
            );
        }
    }
}