package controllers;

import dto.auth.UserSessionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import services.OpenWeatherService;
import services.UserService;
import java.util.Objects;

import static utils.ModelAttributeUtil.LOCATIONS_WEATHER;
import static utils.ModelAttributeUtil.USER_SESSION;
import static utils.PagesUtil.HOME;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    private final OpenWeatherService openWeatherService;

    @GetMapping
    public String showHomePage(Model model) {
        if (model.containsAttribute(USER_SESSION)) {
            var userSessionDto = (UserSessionDTO) model.getAttribute(USER_SESSION);
            var userId = Objects.requireNonNull(userSessionDto).userId();

            var user = userService.findById(userId);

            var weatherResponseDtos = openWeatherService.getWeatherInfo(user.getLocations());

            if (!weatherResponseDtos.isEmpty()) {
                model.addAttribute(LOCATIONS_WEATHER, weatherResponseDtos);
            }
        }

        return HOME;
    }
}
