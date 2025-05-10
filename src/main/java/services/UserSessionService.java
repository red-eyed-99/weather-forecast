package services;

import dto.UserSessionDTO;
import lombok.RequiredArgsConstructor;
import models.entities.UserSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.UserSessionRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    @Transactional(readOnly = true)
    public Optional<UserSessionDTO> findNotExpiredSession(UUID sessionUUID) {
        var userSessionDTO = userSessionRepository.findById(sessionUUID);

        if (userSessionDTO.isPresent()) {
            if (!isExpired(userSessionDTO.get())) {
                return userSessionDTO;
            }
        }

        return Optional.empty();
    }

    public void save(UserSession userSession) {
        userSessionRepository.save(userSession);
    }

    private boolean isExpired(UserSessionDTO userSessionDTO) {
        var sessionExpiresAt = userSessionDTO.expiresAt();
        return LocalDateTime.now().isAfter(sessionExpiresAt);
    }
}
