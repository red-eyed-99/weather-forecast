package services;

import dto.SignInUserDTO;
import dto.SignUpUserDTO;
import dto.UserSessionDTO;
import exception.BadCredentialsException;
import exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import models.entities.User;
import models.entities.UserSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final UserSessionService userSessionService;

    @Transactional
    public UserSession signUp(SignUpUserDTO signUpUserDTO) {
        var user = userService.save(signUpUserDTO);
        return userSessionService.create(user);
    }

    @Transactional
    public UserSession signIn(SignInUserDTO signInUserDTO) {
        var username = signInUserDTO.username();

        User user;

        try {
            user = userService.findByUsername(username);
        } catch (NotFoundException exception) {
            throw new BadCredentialsException(username);
        }

        verifyPassword(username, signInUserDTO.password(), user.getPassword());

        return userSessionService.create(user);
    }

    private void verifyPassword(String username, String password, String encryptedPassword) {
        var verified = PasswordEncoder.verified(password, encryptedPassword);

        if (!verified) {
            throw new BadCredentialsException(username);
        }
    }

    @Transactional
    public void logout(UserSessionDTO userSessionDTO) {
        userSessionService.delete(userSessionDTO);
    }
}
