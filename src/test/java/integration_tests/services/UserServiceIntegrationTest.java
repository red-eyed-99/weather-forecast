package integration_tests.services;

import annotations.IntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import components.OpenWeatherUriBuilder;
import dto.openweather.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;
import services.UserService;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static utils.SqlScriptUtil.INSERT_LOCATION;
import static utils.SqlScriptUtil.INSERT_USER;
import static utils.SqlScriptUtil.LOCATION_COORDINATES;
import static utils.SqlScriptUtil.LOCATION_ID;
import static utils.SqlScriptUtil.LOCATION_NAME;
import static utils.SqlScriptUtil.USER_ID;

@IntegrationTest
@RequiredArgsConstructor
class UserServiceIntegrationTest {

    private final UserService userService;

    private final OpenWeatherUriBuilder openWeatherUriBuilder;

    @MockitoBean
    private final RestTemplate restTemplate;

    @Test
    @DisplayName("Add location to user when location exists in database")
    @Sql({INSERT_USER, INSERT_LOCATION})
    void addLocationToUser_locationExistsInDatabase_shouldAddLocationToUser() {
        userService.addLocation(USER_ID, LOCATION_COORDINATES);

        var user = userService.findById(USER_ID);

        var addedLocation = user.getLocations().stream()
                .filter(location -> Objects.equals(location.getId(), LOCATION_ID))
                .findFirst()
                .orElseThrow();

        assertAll(
                () -> assertEquals(LOCATION_ID, addedLocation.getId()),
                () -> assertEquals(LOCATION_NAME, addedLocation.getName()),
                () -> assertEquals(LOCATION_COORDINATES.latitude(), addedLocation.getLatitude()),
                () -> assertEquals(LOCATION_COORDINATES.longitude(), addedLocation.getLongitude())
        );
    }

    @Test
    @DisplayName("Add location to user when location doesn't exist in database")
    @Sql(INSERT_USER)
    void addLocationToUser_locationDoesNotExistInDatabase_shouldAddLocationToDatabaseAndUser() throws IOException {
        var openWeatherResponse = new ClassPathResource("openweather/success_response.json").getInputStream();

        var weatherResponseDto = new ObjectMapper()
                .readValue(openWeatherResponse, WeatherResponseDTO.class);

        var uri = openWeatherUriBuilder.build(LOCATION_COORDINATES);

        doReturn(weatherResponseDto)
                .when(restTemplate)
                .getForObject(uri, WeatherResponseDTO.class);

        userService.addLocation(USER_ID, LOCATION_COORDINATES);

        var user = userService.findById(USER_ID);

        var addedLocation = user.getLocations().stream()
                .filter(location -> Objects.equals(location.getId(), LOCATION_ID))
                .findFirst()
                .orElseThrow();

        assertAll(
                () -> assertEquals(LOCATION_ID, addedLocation.getId()),
                () -> assertEquals(LOCATION_NAME, addedLocation.getName()),
                () -> assertEquals(LOCATION_COORDINATES.latitude(), addedLocation.getLatitude()),
                () -> assertEquals(LOCATION_COORDINATES.longitude(), addedLocation.getLongitude())
        );
    }
}