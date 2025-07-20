package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlScriptUtil {

    private static final String BASE_PATH = "/db/sql_scripts";

    public static final String USER_SESSION_ID = "28e5000a-474d-49bc-928e-48e2cf817864";

    public static final String INSERT_USER = BASE_PATH + "/insert_user_to_users.sql";

    public static final String INSERT_SESSION =  BASE_PATH + "/insert_session_to_sessions.sql";
    public static final String INSERT_EXPIRED_SESSION = BASE_PATH + "/insert_expired_session_to_sessions.sql";
    public static final String INSERT_SESSIONS = BASE_PATH + "/insert_sessions_to_sessions.sql";
    public static final String INSERT_SESSION =  BASE_PATH + "/sessions/insert_session_to_sessions.sql";
    public static final String INSERT_EXPIRED_SESSION = BASE_PATH + "/sessions/insert_expired_session_to_sessions.sql";
    public static final String INSERT_SESSIONS = BASE_PATH + "/sessions/insert_sessions_to_sessions.sql";

}
