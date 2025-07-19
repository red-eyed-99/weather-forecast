package controllers;

import dto.auth.UserSessionDTO;
import dto.openweather.CoordinatesDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.UserService;
import java.util.Objects;

import static utils.ModelAttributeUtil.ERROR_MESSAGE;
import static utils.ModelAttributeUtil.USER_SESSION;
import static utils.PagesUtil.HOME;
import static utils.PagesUtil.SIGN_IN;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/locations")
    public String addLocation(Model model, @Valid CoordinatesDTO coordinatesDto) {
        if (!model.containsAttribute(USER_SESSION)) {
            model.addAttribute(ERROR_MESSAGE, "To add locations you need to sign in.");
            return SIGN_IN;
        }

        var userSessionDto = (UserSessionDTO) model.getAttribute(USER_SESSION);

        var userId = Objects.requireNonNull(userSessionDto).userId();

        userService.addLocation(userId, coordinatesDto);

        return HOME;
    }
}