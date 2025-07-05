package unit_tests;

import dto.auth.SignInUserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SignInUserDtoTest {

    @Test
    @DisplayName("Trim leading and trailing spaces when construct")
    void shouldTrimLeadingAndTrailingSpacesOnConstruct() {
        var username = "    username ";
        var password = "  password   ";

        var signInUserDto = new SignInUserDTO(username, password);

        assertAll(
                () -> assertEquals(username.trim(), signInUserDto.username()),
                () -> assertEquals(password.trim(), signInUserDto.password())
        );
    }
}
