package integration_tests;

import annotations.IntegrationTest;
import dto.SignUpUserDTO;
import exceptions.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.AuthService;
import services.UserService;
import utils.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@RequiredArgsConstructor
class SignUpIntegrationTest {

    private final AuthService authService;

    private final UserService userService;

    @Test
    @DisplayName(value = "User doesn't exist")
    public void shouldSuccessfullyRegisterUser() {
        var username = "dummy";
        var password = PasswordEncoder.encode("dummy");
        var signUpUserDto = new SignUpUserDTO(username, password, password);

        var userSession = authService.signUp(signUpUserDto);
        var user = userService.findByUsername(username);

        assertAll(
                () -> assertEquals(username, user.getUsername()),
                () -> assertEquals(password, user.getPassword()),

                () -> assertEquals(user.getId(), userSession.getUser().getId())
        );
    }

    @Test
    @DisplayName(value = "User already exists")
    public void throwsUserAlreadyExistException() {
        var username = "dummy";
        var password = PasswordEncoder.encode("dummy");
        var signUpUserDto = new SignUpUserDTO(username, password, password);

        userService.save(signUpUserDto);

        assertThrows(UserAlreadyExistException.class, () -> authService.signUp(signUpUserDto));
    }
}
