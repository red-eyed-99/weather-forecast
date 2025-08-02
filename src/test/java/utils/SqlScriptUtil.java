package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlScriptUtil {

    private static final String BASE_PATH = "/db/sql_scripts";

    public static final String INSERT_USER = BASE_PATH + "/insert_user_to_users.sql";

    public static final String INSERT_SESSION = BASE_PATH + "/sessions/insert_session_to_sessions.sql";
    public static final String INSERT_EXPIRED_SESSION = BASE_PATH + "/sessions/insert_expired_session_to_sessions.sql";
    public static final String INSERT_SESSIONS = BASE_PATH + "/sessions/insert_sessions_to_sessions.sql";

    public static final String INSERT_LOCATIONS = BASE_PATH + "/insert_locations_to_locations.sql";

    private static CoordinatesDTO getLocationCoordinates() {
        var latitude = new BigDecimal("55.7522");
        var longitude = new BigDecimal("37.6156");
        return new CoordinatesDTO(longitude, latitude);
    }
}
