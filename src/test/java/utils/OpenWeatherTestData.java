package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.openweather.WeatherResponseDTO;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import java.util.HashMap;
import java.util.Map;

import static utils.LocationTestData.LONDON;
import static utils.LocationTestData.MOSCOW;

@UtilityClass
public class OpenWeatherTestData {

    private static final Map<String, WeatherResponseDTO> OPEN_WEATHER_RESPONSES;

    static {
        OPEN_WEATHER_RESPONSES = new HashMap<>();

        OPEN_WEATHER_RESPONSES.put(MOSCOW, initWeatherResponseDto(MOSCOW));
        OPEN_WEATHER_RESPONSES.put(LONDON, initWeatherResponseDto(LONDON));
    }

    @SneakyThrows
    private static WeatherResponseDTO initWeatherResponseDto(String locationName) {
        var path = "openweather/sucess_response/" + locationName + ".json";

        var openWeatherResponse = new ClassPathResource(path).getInputStream();

        return new ObjectMapper()
                .readValue(openWeatherResponse, WeatherResponseDTO.class);
    }

    public static WeatherResponseDTO getWeatherResponseDto(String locationName) {
        return OPEN_WEATHER_RESPONSES.get(locationName);
    }
}