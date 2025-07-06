package dto.openweather;

public record WeatherInfo(

        WeatherGroup group,
        String description,
        TemperatureInfo temperatureInfo,
        WindInfo windInfo,
        int humidity,
        int visibility,
        Long sunrise,
        Long sunset){
}