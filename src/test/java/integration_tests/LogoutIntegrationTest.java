package integration_tests;

import annotations.IntegrationTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import services.AuthService;
import services.UserService;
import services.UserSessionService;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.SqlScriptUtil.INSERT_SESSION;
import static utils.SqlScriptUtil.INSERT_USER;
import static utils.SqlScriptUtil.USER_SESSION_ID;

@IntegrationTest
@RequiredArgsConstructor
class LogoutIntegrationTest {

    private final AuthService authService;

    private final UserSessionService userSessionService;

    private final UserService userService;

    @Test
    @DisplayName(value = "User session exists")
    @Sql(scripts = {INSERT_USER, INSERT_SESSION})
    public void shouldSuccessfullyLogoutUser() {
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
