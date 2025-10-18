package dto.auth;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserSessionDTO(UUID id, LocalDateTime expiresAt, Long userId, String username) {
}