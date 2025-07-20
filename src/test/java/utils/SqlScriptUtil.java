package utils;

import dto.openweather.CoordinatesDTO;
import lombok.experimental.UtilityClass;
import java.math.BigDecimal;

@UtilityClass
public class SqlScriptUtil {

    private static final String BASE_PATH = "/db/sql_scripts";

    public static final Long USER_ID = 1L;
    public static final String USER_SESSION_ID = "28e5000a-474d-49bc-928e-48e2cf817864";

    public static final Long LOCATION_ID = 1L;
    public static final String LOCATION_NAME = "Moscow";
    public static final CoordinatesDTO LOCATION_COORDINATES = getLocationCoordinates();

    public static final String INSERT_USER = BASE_PATH + "/insert_user_to_users.sql";

    public static final String INSERT_SESSION =  BASE_PATH + "/sessions/insert_session_to_sessions.sql";
    public static final String INSERT_EXPIRED_SESSION = BASE_PATH + "/sessions/insert_expired_session_to_sessions.sql";
    public static final String INSERT_SESSIONS = BASE_PATH + "/sessions/insert_sessions_to_sessions.sql";

    public static final String INSERT_LOCATION = BASE_PATH + "/insert_location_to_locations.sql";

    private static CoordinatesDTO getLocationCoordinates() {
        var latitude = new BigDecimal("55.7522");
        var longitude = new BigDecimal("37.6156");
        return new CoordinatesDTO(longitude, latitude);
    }
}
