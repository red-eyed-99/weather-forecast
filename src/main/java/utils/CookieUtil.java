package utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import models.entities.UserSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@UtilityClass
public class CookieUtil {

    public static final String USER_SESSION_COOKIE = "user_session";

    public static void addUserSessionCookie(UserSession userSession, HttpServletResponse response) {
        var cookie = new Cookie(USER_SESSION_COOKIE, userSession.getId().toString());

        var maxAge = getMaxAge(userSession.getExpiresAt());

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    private static int getMaxAge(LocalDateTime expiresAt) {
        var now = ZonedDateTime.now(ZoneOffset.UTC);
        var expires = expiresAt.atZone(ZoneOffset.UTC);
        return (int) Duration.between(now, expires).getSeconds();
    }
}
