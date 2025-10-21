package services;

import dto.openweather.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLocationsWeatherInfoService {

    private static final int LOCATIONS_PER_PAGE = 6;

    private final UserService userService;

    private final OpenWeatherService openWeatherService;

    public List<WeatherResponseDTO> getWeatherInfo(Long userId, int pageNumber) {
        var offset = (pageNumber - 1) * LOCATIONS_PER_PAGE;

        var user = userService.findByIdWithLocations(userId, LOCATIONS_PER_PAGE, offset);

        var locations = user.getLocations();

        return openWeatherService.getWeatherInfo(locations);
    }
}
