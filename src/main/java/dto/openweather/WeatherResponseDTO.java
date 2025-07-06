package dto.openweather;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import utils.deserializers.WeatherResponseDtoDeserializer;

@JsonDeserialize(using = WeatherResponseDtoDeserializer.class)
public record WeatherResponseDTO(LocationInfo locationInfo, WeatherInfo weatherInfo) {
}