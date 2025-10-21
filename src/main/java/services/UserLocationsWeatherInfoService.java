package services;

import dto.openweather.WeatherResponseDTO;
import dto.pageable.PageableResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utils.WeatherCardsPaginator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLocationsWeatherInfoService {

    private final UserService userService;

    private final UserLocationService userLocationService;

    private final OpenWeatherService openWeatherService;

    public PageableResult<List<WeatherResponseDTO>> getWeatherInfo(Long userId, int pageNumber) {
        var offset = (pageNumber - 1) * WeatherCardsPaginator.LOCATIONS_PER_PAGE;

        var user = userService.findByIdWithLocations(userId, WeatherCardsPaginator.LOCATIONS_PER_PAGE, offset);

        var totalLocations = userLocationService.getAddedLocationsNumber(userId);

        var locations = user.getLocations();

        var weatherResponseDtos = openWeatherService.getWeatherInfo(locations);

        var pageInfo = WeatherCardsPaginator.getPageInfo(totalLocations, pageNumber);

        return new PageableResult<>(weatherResponseDtos, pageInfo);
    }
}
