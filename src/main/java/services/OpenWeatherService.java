package services;

import dto.openweather.CoordinatesDTO;
import dto.openweather.WeatherResponseDTO;
import exceptions.LocationNotFoundException;
import exceptions.OpenWeatherException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static utils.PropertiesUtil.APPLICATION_PROPERTIES_CLASSPATH;

@Service
@PropertySource(APPLICATION_PROPERTIES_CLASSPATH)
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherService {

    private static final String UNIT_OF_MEASUREMENT = "metric";

    private final RestTemplate restTemplate;

    @Value("${openWeather.url}")
    private String openWeatherUrl;

    @Value("${openWeather.key}")
    private String openWeatherKey;

    public WeatherResponseDTO getWeatherInfo(String locationName) {
        var uri = UriComponentsBuilder.newInstance()
                .uri(URI.create(openWeatherUrl))
                .queryParam("q", locationName)
                .queryParam("units", UNIT_OF_MEASUREMENT)
                .queryParam("appid", openWeatherKey)
                .toUriString();

        return getWeatherResponseDto(uri);
    }

    public WeatherResponseDTO getWeatherInfo(CoordinatesDTO coordinatesDTO) {
        var uri = UriComponentsBuilder.newInstance()
                .uri(URI.create(openWeatherUrl))
                .queryParam("lat", coordinatesDTO.latitude())
                .queryParam("lon", coordinatesDTO.longitude())
                .queryParam("units", UNIT_OF_MEASUREMENT)
                .queryParam("appid", openWeatherKey)
                .toUriString();

        return getWeatherResponseDto(uri);
    }

    private WeatherResponseDTO getWeatherResponseDto(String uri) {
        var weatherResponseDTO = (WeatherResponseDTO) null;

        try {
            weatherResponseDTO = restTemplate.getForObject(uri, WeatherResponseDTO.class);
        } catch (HttpClientErrorException exception) {
            var statusCode = exception.getStatusCode();

            if (statusCode.value() == NOT_FOUND.value()) {
                throw new LocationNotFoundException("Location not found");
            }

            if (statusCode.isError()) {
                log.error(exception.getMessage());
                throw new OpenWeatherException("Unable to retrieve weather data, please try again later");
            }
        }

        return weatherResponseDTO;
    }
}