package services;

import dto.auth.UserSessionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.entities.User;
import models.entities.UserSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.UserSessionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static utils.PropertiesUtil.APPLICATION_PROPERTIES_CLASSPATH;

@Service
@PropertySource(APPLICATION_PROPERTIES_CLASSPATH)
@RequiredArgsConstructor
@Slf4j
public class UserSessionService {

    @Value("${userSession.lifetime-days}")
    private long sessionLifetime;

    private final UserSessionRepository userSessionRepository;

    @Transactional(readOnly = true)
    public Optional<UserSessionDTO> findNotExpiredById(UUID id) {
        return userSessionRepository.findNotExpiredById(id);
    }

    @Transactional(readOnly = true)
    public List<UUID> findAllSessionIds() {
        return userSessionRepository.findAllSessionIds();
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
        userSessionRepository.deleteById(userSessionDTO.id());
    }

    @Transactional
    @Scheduled(cron = "${userSession.cleaning-time}")
    public void deleteAllExpired() {
        userSessionRepository.deleteAllExpired();
        log.info("All expired sessions have been deleted");
    }
}