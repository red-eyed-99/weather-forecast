package utils.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import dto.openweather.CoordinatesDTO;
import dto.openweather.LocationDTO;
import dto.openweather.TemperatureDTO;
import dto.openweather.WeatherDTO;
import dto.openweather.WeatherGroup;
import dto.openweather.WeatherResponseDTO;
import dto.openweather.WindDTO;
import java.io.IOException;
import java.math.BigDecimal;

public class WeatherResponseDtoDeserializer extends JsonDeserializer<WeatherResponseDTO> {

    @Override
    public WeatherResponseDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        var jsonNode = (JsonNode) jsonParser.getCodec()
                .readTree(jsonParser);

        var locationDto = getLocationDto(jsonNode);
        var weatherDto = getWeatherDto(jsonNode);

        return new WeatherResponseDTO(locationDto, weatherDto);
    }

    private LocationDTO getLocationDto(JsonNode jsonNode) {
        var coordinatesDto = getCoordinatesDto(jsonNode);
        var country = jsonNode.path("sys").path("country").asText();
        var name = jsonNode.path("name").asText();

        return new LocationDTO(coordinatesDto, country, name);
    }

    private CoordinatesDTO getCoordinatesDto(JsonNode jsonNode) {
        var coordJsonNode = jsonNode.path("coord");

        var longitudeValue = coordJsonNode.path("lon").asDouble();
        var latitudeValue = coordJsonNode.path("lat").asDouble();

        var longitude = BigDecimal.valueOf(longitudeValue);
        var latitude = BigDecimal.valueOf(latitudeValue);

        return new CoordinatesDTO(longitude, latitude);
    }

    private WeatherDTO getWeatherDto(JsonNode jsonNode) {
        var weatherJsonNode = jsonNode.path("weather");
        var sysJsonNode = jsonNode.path("sys");

        var weatherGroupValue = weatherJsonNode.findPath("main")
                .asText()
                .toUpperCase();

        var weatherGroup = WeatherGroup.valueOf(weatherGroupValue);
        var description = weatherJsonNode.findPath("description").asText();
        var temperatureDto = getTemperatureDto(jsonNode);
        var windDto = getWindDto(jsonNode);
        var humidity = jsonNode.path("main").path("humidity").asInt();
        var visibility = jsonNode.path("visibility").asInt();
        var sunrise = sysJsonNode.path("sunrise").asLong();
        var sunset = sysJsonNode.path("sunset").asLong();

        return new WeatherDTO(weatherGroup, description, temperatureDto, windDto, humidity, visibility, sunrise, sunset);
    }

    private TemperatureDTO getTemperatureDto(JsonNode jsonNode) {
        var temperatureJsonNode = jsonNode.path("main");

        var currentTemperatureValue = temperatureJsonNode.path("temp").asDouble();
        var minTemperatureValue = temperatureJsonNode.path("temp_min").asDouble();
        var maxTemperatureValue = temperatureJsonNode.path("temp_max").asDouble();
        var feelsLikeTemperatureValue = temperatureJsonNode.path("feels_like").asDouble();

        var currentTemperature = (int) Math.round(currentTemperatureValue);
        var minTemperature = (int) Math.round(minTemperatureValue);
        var maxTemperature = (int) Math.round(maxTemperatureValue);
        var feelsLikeTemperature = (int) Math.round(feelsLikeTemperatureValue);

        return new TemperatureDTO(currentTemperature, minTemperature, maxTemperature, feelsLikeTemperature);
    }

    private WindDTO getWindDto(JsonNode jsonNode) {
        var windJsonNode = jsonNode.path("wind");

        var speedValue = windJsonNode.path("speed").asDouble();

        var speed = (int) Math.round(speedValue);
        var degrees = windJsonNode.path("deg").asInt();

        return new WindDTO(speed, degrees);
    }
}