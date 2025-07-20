package controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.OpenWeatherService;
import utils.ExtraSpacesRemover;
import validation.annotations.ValidLocationName;

import static utils.ModelAttributeUtil.LOCATION_WEATHER;
import static utils.PagesUtil.SEARCH_LOCATIONS;

@Controller
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {

    private final OpenWeatherService openWeatherService;

    @GetMapping
    public String searchLocations(Model model, @RequestParam(name = "locationName") @ValidLocationName String locationName) {
        locationName = ExtraSpacesRemover.removeExtraSpaces(locationName);
        var weatherResponseDto = openWeatherService.getWeatherInfo(locationName);
        model.addAttribute(LOCATION_WEATHER, weatherResponseDto);
        return SEARCH_LOCATIONS;
    }
}