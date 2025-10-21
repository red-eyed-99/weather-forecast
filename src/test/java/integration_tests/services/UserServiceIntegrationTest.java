package integration_tests.services;

import annotations.IntegrationTest;
import components.OpenWeatherUriBuilder;
import dto.openweather.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;
import services.UserService;
import utils.LocationTestData;
import utils.OpenWeatherTestData;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static utils.LocationTestData.MOSCOW;
import static utils.SqlScriptUtil.INSERT_LOCATIONS;
import static utils.SqlScriptUtil.INSERT_USER;
import static utils.UserTestData.USER_ID;
import static utils.WeatherCardsPaginator.LOCATIONS_PER_PAGE;

@IntegrationTest
@RequiredArgsConstructor
class UserServiceIntegrationTest {

    private static final int OFFSET = 0;

    private final UserService userService;

    private final OpenWeatherUriBuilder openWeatherUriBuilder;

    @MockitoBean
    private final RestTemplate restTemplate;

    @Test
    @DisplayName("Add location to user when location exists in database")
    @Sql({INSERT_USER, INSERT_LOCATIONS})
    void addLocationToUser_locationExistsInDatabase_shouldAddLocationToUser() {
        var locationInfo = LocationTestData.getLocationInfo(MOSCOW);

        var locationId = locationInfo.id();
        var locationName = locationInfo.name();
        var locationCoordinates = locationInfo.coordinates();

        userService.addLocation(USER_ID, locationName);

        var user = userService.findByIdWithLocations(USER_ID, LOCATIONS_PER_PAGE, OFFSET);

        var addedLocation = user.getLocations().stream()
                .filter(location -> Objects.equals(location.getId(), locationId))
                .findFirst()
                .orElseThrow();

        assertAll(
                () -> assertEquals(locationId, addedLocation.getId()),
                () -> assertEquals(locationInfo.name(), addedLocation.getName()),
                () -> assertEquals(locationCoordinates.latitude(), addedLocation.getLatitude()),
                () -> assertEquals(locationCoordinates.longitude(), addedLocation.getLongitude())
        );
    }

    @Test
    @DisplayName("Add location to user when location doesn't exist in database")
    @Sql(INSERT_USER)
    void addLocationToUser_locationDoesNotExistInDatabase_shouldAddLocationToDatabaseAndUser() {
        var weatherResponseDto = OpenWeatherTestData.getWeatherResponseDto(MOSCOW);

        var locationName = weatherResponseDto.getLocationDto().name();
        var locationCoordinates = weatherResponseDto.getLocationDto().coordinatesDto();

        var uri = openWeatherUriBuilder.build(locationName);

        doReturn(weatherResponseDto)
                .when(restTemplate)
                .getForObject(uri, WeatherResponseDTO.class);

        userService.addLocation(USER_ID, locationName);

        var user = userService.findByIdWithLocations(USER_ID, LOCATIONS_PER_PAGE, OFFSET);

        var addedLocation = user.getLocations().stream()
                .filter(location -> Objects.equals(location.getName(), locationName))
                .findFirst()
                .orElseThrow();

        assertAll(
                () -> assertNotNull(addedLocation.getId()),
                () -> assertEquals(locationName, addedLocation.getName()),
                () -> assertEquals(locationCoordinates.latitude(), addedLocation.getLatitude()),
                () -> assertEquals(locationCoordinates.longitude(), addedLocation.getLongitude())
        );
    }
}