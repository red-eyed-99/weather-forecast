package dto.auth;

import lombok.Getter;
import validation.annotations.PasswordMatches;
import validation.annotations.ValidPassword;
import validation.annotations.ValidUsername;
import java.beans.ConstructorProperties;

import static dto.auth.SignUpUserDTO.Fields.PASSWORD;
import static dto.auth.SignUpUserDTO.Fields.REPEAT_PASSWORD;
import static dto.auth.SignUpUserDTO.Fields.USERNAME;

@Getter
@PasswordMatches
public class SignUpUserDTO {

    @ValidUsername
    private final String username;

    @ValidPassword
    private String password;

    private String repeatPassword;

    private boolean passwordEncrypted = false;

    @ConstructorProperties({USERNAME, PASSWORD, REPEAT_PASSWORD})
    public SignUpUserDTO(String username, String password, String repeatPassword) {
        this.username = username.trim();
        this.password = password.trim();
        this.repeatPassword = repeatPassword.trim();
    }

    public void setEncryptedPassword(String password) {
        if (passwordEncrypted) {
            throw new IllegalStateException("Password already encrypted and cannot be changed.");
        }

        this.password = password;
        this.repeatPassword = password;
        this.passwordEncrypted = true;
    }

    public SignUpUserDTO getCopy(SignUpUserDTO signUpUserDTO) {
        return new SignUpUserDTO(username, password, repeatPassword);
    }

    public static final class Fields {
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String REPEAT_PASSWORD = "repeatPassword";
    }
}
