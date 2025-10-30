package dto.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import dto.openweather.CoordinatesDTO;
import dto.openweather.LocationDTO;
import dto.openweather.TemperatureDTO;
import dto.openweather.WeatherDTO;
import dto.openweather.WeatherDescription;
import dto.openweather.WeatherGroup;
import dto.openweather.WeatherResponseDTO;
import dto.openweather.WindDTO;
import utils.date_time.DateTimeUtil;
import utils.date_time.TimeOfDay;
import utils.wind.BeaufortScaleMeasurer;
import utils.wind.WindDirectionResolver;
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

        var sunriseValue = sysJsonNode.path("sunrise").asLong();
        var sunsetValue = sysJsonNode.path("sunset").asLong();

        var weatherGroup = getWeatherGroup(weatherJsonNode);
        var description = getWeatherDescription(weatherJsonNode);
        var timeOfDay = getTimeOfDay(jsonNode);
        var temperatureDto = getTemperatureDto(jsonNode);
        var windDto = getWindDto(jsonNode);
        var humidity = jsonNode.path("main").path("humidity").asInt();
        var visibility = jsonNode.path("visibility").asInt();
        var timezone = jsonNode.path("timezone").asInt();
        var sunriseTime = DateTimeUtil.getTime(sunriseValue, timezone, "HH:mm");
        var sunsetTime = DateTimeUtil.getTime(sunsetValue, timezone, "HH:mm");

        return new WeatherDTO(
                weatherGroup, description, timeOfDay, temperatureDto, windDto, humidity, visibility,
                sunriseTime, sunsetTime
        );
    }

    private WeatherGroup getWeatherGroup(JsonNode weatherJsonNode) {
        var weatherGroupValue = weatherJsonNode.findPath("main")
                .asText()
                .toUpperCase();

        return WeatherGroup.valueOf(weatherGroupValue);
    }

    private WeatherDescription getWeatherDescription(JsonNode weatherJsonNode) {
        var description = weatherJsonNode.findPath("description").asText();
        return WeatherDescription.getByDescription(description);
    }

    private TimeOfDay getTimeOfDay(JsonNode jsonNode) {
        var timezone = jsonNode.path("timezone").asInt();
        return DateTimeUtil.determineTimeOfDay(timezone);
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

        var speed = windJsonNode.path("speed").asDouble();
        var beaufortPoints = BeaufortScaleMeasurer.measure(speed);
        var degrees = windJsonNode.path("deg").asInt();
        var gust = windJsonNode.path("gust").asDouble();
        var direction = WindDirectionResolver.resolve(degrees);

        return new WindDTO(speed, beaufortPoints, degrees, gust, direction);
    }
}