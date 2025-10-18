package dto.openweather;

import utils.date_time.TimeOfDay;

public record WeatherDTO(

        WeatherGroup group,
        String description,
        TimeOfDay timeOfDay,
        TemperatureDTO temperatureDto,
        WindDTO windDto,
        int humidity,
        int visibility,
        String sunrise,
        String sunset) {

    public String getFullDescriptionWithHyphens() {
        return description.replaceAll(" ", "-") + "-" + timeOfDay.getValue();
    }
}