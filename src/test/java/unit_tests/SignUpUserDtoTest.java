package unit_tests;

import dto.SignUpUserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SignUpUserDtoTest {

    @Test
    @DisplayName("Trim leading and trailing spaces when construct")
    void shouldTrimLeadingAndTrailingSpacesOnConstruct() {
        var username = "    username ";
        var password = "  password   ";
        var repeatPassword = " password ";

        var signUpUserDto = new SignUpUserDTO(username, password, repeatPassword);

        assertAll(
                () -> assertEquals(username.trim(), signUpUserDto.getUsername()),
                () -> assertEquals(password.trim(), signUpUserDto.getPassword()),
                () -> assertEquals(repeatPassword.trim(), signUpUserDto.getRepeatPassword())
        );
    }

    @Test
    @DisplayName("Can't set encrypted password twice")
    void throwsIllegalStateException() {
        var password = "password";
        var encryptedPassword = PasswordEncoder.encode(password);
        var signUpUserDto = new SignUpUserDTO("username", password, password);

        signUpUserDto.setEncryptedPassword(encryptedPassword);

        assertThrows(IllegalStateException.class, () -> signUpUserDto.setEncryptedPassword(encryptedPassword));
    }
}
