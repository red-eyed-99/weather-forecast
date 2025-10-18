package dto.openweather;

import utils.wind.WindDirection;

public record WindDTO(

        double speed,
        int beaufortPoints,
        int degrees,
        double gust,
        WindDirection direction
) {
}