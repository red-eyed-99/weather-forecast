package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.openweather.WeatherResponseDTO;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

@UtilityClass
public class OpenWeatherTestData {

    private static final String SUCCESS_RESPONSE_PATH = "openweather/sucess_response/";

    private static final String JSON_FORMAT = ".json";

    @SneakyThrows
    public static WeatherResponseDTO getWeatherResponseDto(String locationName) {
        var path = SUCCESS_RESPONSE_PATH + locationName + JSON_FORMAT;

        var openWeatherResponse = new ClassPathResource(path).getInputStream();

        return new ObjectMapper()
                .readValue(openWeatherResponse, WeatherResponseDTO.class);
    }
}