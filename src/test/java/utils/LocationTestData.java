package utils;

import dto.openweather.CoordinatesDTO;
import lombok.experimental.UtilityClass;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class LocationTestData {

    public static final String MOSCOW = "Moscow";
    public static final String LONDON = "London";

    public static final Map<String, LocationInfo> LOCATIONS_INFO;

    static {
        LOCATIONS_INFO = new HashMap<>();

        LOCATIONS_INFO.put(MOSCOW, getMoscowLocationInfo());
        LOCATIONS_INFO.put(LONDON, getLondonLocationInfo());
    }

    private static LocationInfo getMoscowLocationInfo() {
        var locationId = 1L;

        var longitude = new BigDecimal("37.6156");
        var latitude = new BigDecimal("55.7522");
        var coordinatesDto = new CoordinatesDTO(longitude, latitude);

        return new LocationInfo(locationId, MOSCOW, coordinatesDto);
    }

    private static LocationInfo getLondonLocationInfo() {
        var locationId = 2L;

        var longitude = new BigDecimal("-0.1257");
        var latitude = new BigDecimal("51.5085");
        var coordinatesDto = new CoordinatesDTO(longitude, latitude);

        return new LocationInfo(locationId, LONDON, coordinatesDto);
    }

    public record LocationInfo(Long id, String name, CoordinatesDTO coordinates) {
    }

    public LocationInfo getLocationInfo(String locationName) {
        return LOCATIONS_INFO.get(locationName);
    }
}