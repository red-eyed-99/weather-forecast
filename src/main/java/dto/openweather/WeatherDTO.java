package dto.openweather;

import utils.date_time.TimeOfDay;

public record WeatherDTO(

        WeatherGroup group,
        WeatherDescription description,
        TimeOfDay timeOfDay,
        TemperatureDTO temperatureDto,
        WindDTO windDto,
        int humidity,
        int visibility,
        String sunrise,
        String sunset) {

    public String getFullDescriptionWithHyphens() {
        return description.getDescription()
                .replaceAll("[\\s/]", "-") + "-" + timeOfDay.getValue();
    }

    public String getDescriptionInUpperCase() {
        return description.getDescription().toUpperCase();
    }
}