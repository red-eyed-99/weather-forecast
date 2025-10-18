package dto.openweather;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import dto.deserializers.WeatherResponseDtoDeserializer;

@JsonDeserialize(using = WeatherResponseDtoDeserializer.class)
@RequiredArgsConstructor
@Getter
public class WeatherResponseDTO {

    private final LocationDTO locationDto;

    private final WeatherDTO weatherDto;

    @Setter
    private boolean locationAdded;
}