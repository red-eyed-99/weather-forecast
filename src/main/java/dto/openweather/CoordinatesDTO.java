package dto.openweather;

import validation.annotations.ValidLatitude;
import validation.annotations.ValidLongitude;
import java.math.BigDecimal;

public record CoordinatesDTO(@ValidLongitude BigDecimal longitude, @ValidLatitude BigDecimal latitude) {
}