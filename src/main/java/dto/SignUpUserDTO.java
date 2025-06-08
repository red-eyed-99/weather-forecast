package dto;

import lombok.Getter;
import validation.annotations.PasswordMatches;
import validation.annotations.ValidPassword;
import validation.annotations.ValidUsername;
import java.beans.ConstructorProperties;

import static utils.FieldErrorUtil.PASSWORD;
import static utils.FieldErrorUtil.REPEAT_PASSWORD;
import static utils.FieldErrorUtil.USERNAME;

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
}
