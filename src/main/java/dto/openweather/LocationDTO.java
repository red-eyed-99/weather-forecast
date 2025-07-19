package dto.openweather;

public record LocationDTO(CoordinatesDTO coordinatesDto, String country, String name) {
}