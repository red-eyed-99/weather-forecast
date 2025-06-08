package services;

import dto.UserSessionDTO;
import lombok.RequiredArgsConstructor;
import mappers.UserSessionMapper;
import models.entities.User;
import models.entities.UserSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.UserSessionRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/application.properties")
@RequiredArgsConstructor
public class UserSessionService {

    @Value("${userSession.lifetime-days}")
    private long sessionLifetime;

    private final UserSessionRepository userSessionRepository;

    private final UserSessionMapper userSessionMapper;

    @Transactional(readOnly = true)
    public Optional<UserSessionDTO> findNotExpiredById(UUID id) {
        return userSessionRepository.findNotExpiredById(id);
    }

    @Transactional
    public UserSession create(User user) {
        var userSession = UserSession.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(sessionLifetime))
                .build();

        return userSessionRepository.save(userSession);
    }

    @Transactional
    public void delete(UserSessionDTO userSessionDTO) {
        var userSession = userSessionMapper.toUserSession(userSessionDTO);
        userSessionRepository.delete(userSession);
    }
}
