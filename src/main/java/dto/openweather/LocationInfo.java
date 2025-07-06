package dto.openweather;

import java.math.BigDecimal;

public record LocationInfo(BigDecimal longitude, BigDecimal latitude, String country, String city) {
}