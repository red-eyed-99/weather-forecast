package controllers;

import dto.auth.UserSessionDTO;
import dto.openweather.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.LocationService;
import services.OpenWeatherService;
import services.UserLocationService;
import utils.ExtraSpacesRemover;
import validation.annotations.ValidLocationName;
import java.util.Objects;

import static utils.ModelAttributeUtil.LOCATION_WEATHER;
import static utils.ModelAttributeUtil.USER_SESSION;
import static utils.PagesUtil.SEARCH_LOCATIONS;

@Controller
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private final OpenWeatherService openWeatherService;

    private final LocationService locationService;

    private final UserLocationService userLocationService;

    @GetMapping
    public String searchLocations(Model model, @RequestParam(name = "locationName") @ValidLocationName String locationName) {
        locationName = ExtraSpacesRemover.removeExtraSpaces(locationName);

        var weatherResponseDto = openWeatherService.getWeatherInfo(locationName);

        if (model.containsAttribute(USER_SESSION)) {
            var userSessionDto = (UserSessionDTO) model.getAttribute(USER_SESSION);
            var userId = Objects.requireNonNull(userSessionDto).userId();
            checkIfLocationIsAdded(userId, weatherResponseDto);
        }

        model.addAttribute(LOCATION_WEATHER, weatherResponseDto);

        return SEARCH_LOCATIONS;
    }

    private void checkIfLocationIsAdded(Long userId, WeatherResponseDTO weatherResponseDto) {
        var locationName = weatherResponseDto.getLocationDto().name();
        var locationId = locationService.findLocationId(locationName);

        locationId.ifPresent(
                id -> {
                    var locationAdded = userLocationService.locationAdded(id, userId);
                    weatherResponseDto.setLocationAdded(locationAdded);
                }
        );
    }
}