package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlScriptUtil {

    public static final String USER_SESSION_ID = "28e5000a-474d-49bc-928e-48e2cf817864";

    public static final String INSERT_USER = "/db/sql_scripts/insert_user_to_users.sql";

    public static final String INSERT_SESSION = "/db/sql_scripts/insert_session_to_sessions.sql";
    public static final String INSERT_EXPIRED_SESSION = "/db/sql_scripts/insert_expired_session_to_sessions.sql";
}
