package utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PasswordEncoder {

    private static final int PASSWORD_COST = 12;

    public static String encode(String password) {
        return BCrypt.withDefaults()
                .hashToString(PASSWORD_COST, password.toCharArray());
    }

    public static boolean verified(String password, String encryptedPassword) {
        return BCrypt.verifyer()
                .verify(password.toCharArray(), encryptedPassword)
                .verified;
    }
}