package integration_tests.services;

import annotations.IntegrationTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import services.UserSessionService;
import utils.SessionTestData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.SqlScriptUtil.INSERT_SESSIONS;
import static utils.SqlScriptUtil.INSERT_USER;

@IntegrationTest
@RequiredArgsConstructor
class UserSessionServiceIntegrationTest {

    private final UserSessionService userSessionService;

    @Test
    @DisplayName("Delete all expired sessions")
    @Sql(scripts = {INSERT_USER, INSERT_SESSIONS})
    void shouldDeleteAllExpiredSessions() {
        var expectedSessionIds = SessionTestData.ACTIVE_SESSION_IDS;

        var expiredSessionIds = SessionTestData.EXPIRED_SESSION_IDS;

        userSessionService.deleteAllExpired();

        var sessionIds = userSessionService.findAllSessionIds();

        assertTrue(sessionIds.containsAll(expectedSessionIds));
        assertFalse(sessionIds.containsAll(expiredSessionIds));
    }
}
