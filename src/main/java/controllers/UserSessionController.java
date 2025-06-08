package controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import services.UserSessionService;
import java.util.UUID;

import static utils.CookieUtil.USER_SESSION_COOKIE;
import static utils.ModelAttributeUtil.USER_SESSION;

@ControllerAdvice
@RequiredArgsConstructor
public class UserSessionController {

    private final UserSessionService userSessionService;

    @ModelAttribute
    public void addUserSession(Model model, @CookieValue(value = USER_SESSION_COOKIE, required = false) String sessionUUID) {
        if (sessionUUID != null && uuidIsValid(sessionUUID) && !model.containsAttribute(USER_SESSION)) {
            var sessionId = UUID.fromString(sessionUUID);

            var userSessionDTO = userSessionService.findNotExpiredById(sessionId);

            userSessionDTO.ifPresent(sessionDTO -> model.addAttribute(USER_SESSION, sessionDTO));
        }
    }

    private boolean uuidIsValid(String uuid) {
        return uuid.matches("^[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}$");
    }
}
