package controllers;

import dto.auth.UserSessionDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.UserService;
import validation.validators.LocationNameValidator;
import java.util.Objects;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static utils.ModelAttributeUtil.ERROR_MESSAGE;
import static utils.ModelAttributeUtil.USER_SESSION;
import static utils.PagesUtil.REDIRECT_HOME;
import static utils.PagesUtil.SIGN_IN;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/locations")
    public String addLocation(Model model, @Valid CoordinatesDTO coordinatesDto, HttpServletResponse response) {
    private final LocationNameValidator locationNameValidator;

    @PostMapping("/add-location")
    public String addLocation(Model model, @RequestParam("locationName") String locationName, HttpServletResponse response) {
        locationNameValidator.validate(locationName);

        if (!model.containsAttribute(USER_SESSION)) {
            model.addAttribute(ERROR_MESSAGE, "To add locations you need to sign in.");
            response.setStatus(SC_UNAUTHORIZED);
            return SIGN_IN;
        }

        var userSessionDto = (UserSessionDTO) model.getAttribute(USER_SESSION);

        var userId = Objects.requireNonNull(userSessionDto).userId();

        userService.addLocation(userId, coordinatesDto);
        userService.addLocation(userId, locationName);

        return REDIRECT_HOME;
    }

        return REDIRECT_HOME;
    }
}