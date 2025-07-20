package components;

import dto.openweather.CoordinatesDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@Component
public class OpenWeatherUriBuilder {

    private static final String UNIT_OF_MEASUREMENT = "metric";

    @Value("${openWeather.url}")
    private String openWeatherUrl;

    @Value("${openWeather.key}")
    private String openWeatherKey;

    public String build(String locationName) {
        return UriComponentsBuilder.newInstance()
                .uri(URI.create(openWeatherUrl))
                .queryParam("q", locationName)
                .queryParam("units", UNIT_OF_MEASUREMENT)
                .queryParam("appid", openWeatherKey)
                .toUriString();
    }

    public String build(CoordinatesDTO coordinatesDTO) {
        return UriComponentsBuilder.newInstance()
                .uri(URI.create(openWeatherUrl))
                .queryParam("lat", coordinatesDTO.latitude())
                .queryParam("lon", coordinatesDTO.longitude())
                .queryParam("units", UNIT_OF_MEASUREMENT)
                .queryParam("appid", openWeatherKey)
                .toUriString();
    }
}