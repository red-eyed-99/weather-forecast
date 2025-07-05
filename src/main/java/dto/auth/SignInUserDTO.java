package dto.auth;

import validation.annotations.ValidPassword;
import validation.annotations.ValidUsername;

public record SignInUserDTO(@ValidUsername String username, @ValidPassword String password) {

    public SignInUserDTO(String username, String password) {
        this.username = username.trim();
        this.password = password.trim();
    }
}
