package dto;

import lombok.Getter;
import validation.annotations.PasswordMatches;
import validation.annotations.ValidPassword;
import validation.annotations.ValidUsername;
import java.beans.ConstructorProperties;

@Getter
@PasswordMatches
public class SignUpUserDTO {

    @ValidUsername
    private final String username;

    @ValidPassword
    private String password;

    private String repeatPassword;

    @ConstructorProperties({"username", "password", "repeatPassword"})
    public SignUpUserDTO(String username, String password, String repeatPassword) {
        this.username = username.trim();
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public void setPassword(String password) {
        this.password = password != null ? password.trim() : null;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword != null ? repeatPassword.trim() : null;
    }
}
