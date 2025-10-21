package controllers;

import dto.auth.UserSessionDTO;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.UserLocationsWeatherInfoService;
import java.util.Objects;

import static utils.ModelAttributeUtil.LOCATIONS_WEATHER;
import static utils.ModelAttributeUtil.USER_SESSION;
import static utils.PagesUtil.HOME;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserLocationsWeatherInfoService userLocationsWeatherInfoService;

    @GetMapping
    public String showHomePage(@RequestParam(name = "page", required = false, defaultValue = "1")
                               @Min(value = 1, message = "Page starts with '1'") int pageNumber,
                               Model model) {

        if (model.containsAttribute(USER_SESSION)) {
            var userSessionDto = (UserSessionDTO) model.getAttribute(USER_SESSION);
            var userId = Objects.requireNonNull(userSessionDto).userId();

            var weatherResponseDtos = userLocationsWeatherInfoService.getWeatherInfo(userId, pageNumber);

            if (!weatherResponseDtos.isEmpty()) {
                model.addAttribute(LOCATIONS_WEATHER, weatherResponseDtos);
            }
        }

        return HOME;
    }
}
