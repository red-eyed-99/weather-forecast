package services;

import dto.UserSessionDTO;
import lombok.RequiredArgsConstructor;
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
@PropertySource("classpath:/properties/user_session.properties")
@RequiredArgsConstructor
public class UserSessionService {

    @Value("${lifetime-days}")
    private long sessionLifetime;

    private final UserSessionRepository userSessionRepository;

    @Transactional(readOnly = true)
    public Optional<UserSessionDTO> findNotExpiredById(UUID id) {
        return userSessionRepository.findNotExpiredById(id);
    }

    @Transactional
    public UserSession createUserSession(User user) {
        var userSession = UserSession.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(sessionLifetime))
                .build();

        return userSessionRepository.save(userSession);
    }
}
