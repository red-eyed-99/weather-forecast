package dto.openweather;

public record WeatherDTO(

        WeatherGroup group,
        String description,
        TemperatureDTO temperatureDto,
        WindDTO windDto,
        int humidity,
        int visibility,
        Long sunrise,
        Long sunset){
}