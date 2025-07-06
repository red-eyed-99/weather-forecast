package utils.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import dto.openweather.LocationInfo;
import dto.openweather.TemperatureInfo;
import dto.openweather.WeatherGroup;
import dto.openweather.WeatherInfo;
import dto.openweather.WeatherResponseDTO;
import dto.openweather.WindInfo;
import java.io.IOException;
import java.math.BigDecimal;

public class WeatherResponseDtoDeserializer extends JsonDeserializer<WeatherResponseDTO> {

    @Override
    public WeatherResponseDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        var jsonNode = (JsonNode) jsonParser.getCodec().readTree(jsonParser);

        var locationInfo = getLocationInfo(jsonNode);
        var weatherInfo = getWeatherInfo(jsonNode);

        return new WeatherResponseDTO(locationInfo, weatherInfo);
    }

    private LocationInfo getLocationInfo(JsonNode jsonNode) {
        var coordJsonNode = jsonNode.path("coord");

        var longitudeValue = coordJsonNode.path("lon").asDouble();
        var latitudeValue = coordJsonNode.path("lat").asDouble();

        var longitude = BigDecimal.valueOf(longitudeValue);
        var latitude = BigDecimal.valueOf(latitudeValue);
        var country = jsonNode.path("sys").path("country").asText();
        var city = jsonNode.path("name").asText();

        return new LocationInfo(longitude, latitude, country, city);
    }

    private WeatherInfo getWeatherInfo(JsonNode jsonNode) {
        var weatherJsonNode = jsonNode.path("weather");
        var sysJsonNode = jsonNode.path("sys");

        var weatherGroupValue = weatherJsonNode.findPath("main")
                .asText()
                .toUpperCase();

        var weatherGroup = WeatherGroup.valueOf(weatherGroupValue);
        var description = weatherJsonNode.findPath("description").asText();
        var temperatureInfo = getTemperatureInfo(jsonNode);
        var windInfo = getWindInfo(jsonNode);
        var humidity = jsonNode.path("main").path("humidity").asInt();
        var visibility = jsonNode.path("visibility").asInt();
        var sunrise = sysJsonNode.path("sunrise").asLong();
        var sunset = sysJsonNode.path("sunset").asLong();

        return new WeatherInfo(weatherGroup, description, temperatureInfo, windInfo, humidity, visibility, sunrise, sunset);
    }

    private TemperatureInfo getTemperatureInfo(JsonNode jsonNode) {
        var temperatureJsonNode = jsonNode.path("main");

        var currentTemperatureValue = temperatureJsonNode.path("temp").asDouble();
        var minTemperatureValue = temperatureJsonNode.path("temp_min").asDouble();
        var maxTemperatureValue = temperatureJsonNode.path("temp_max").asDouble();
        var feelsLikeTemperatureValue = temperatureJsonNode.path("feels_like").asDouble();

        var currentTemperature = (int) Math.round(currentTemperatureValue);
        var minTemperature = (int) Math.round(minTemperatureValue);
        var maxTemperature = (int) Math.round(maxTemperatureValue);
        var feelsLikeTemperature = (int) Math.round(feelsLikeTemperatureValue);

        return new TemperatureInfo(currentTemperature, minTemperature, maxTemperature, feelsLikeTemperature);
    }

    private WindInfo getWindInfo(JsonNode jsonNode) {
        var windJsonNode = jsonNode.path("wind");

        var speedValue = windJsonNode.path("speed").asDouble();

        var speed = (int) Math.round(speedValue);
        var degrees = windJsonNode.path("deg").asInt();

        return new WindInfo(speed, degrees);
    }
}