package services;

import dto.SignUpUserDTO;
import lombok.RequiredArgsConstructor;
import mappers.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional
    public void signUp(SignUpUserDTO signUpUserDTO) {
        var user = userMapper.toUser(signUpUserDTO);

        userRepository.save(user);
    }
}
