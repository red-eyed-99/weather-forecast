package services;

import dto.SignUpUserDTO;
import lombok.RequiredArgsConstructor;
import mappers.UserMapper;
import models.entities.UserSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserSessionService userSessionService;

    @Transactional
    public UserSession signUp(SignUpUserDTO signUpUserDTO) {
        var user = userMapper.toUser(signUpUserDTO);

        user = userRepository.save(user);

        return userSessionService.createUserSession(user);
    }
}
