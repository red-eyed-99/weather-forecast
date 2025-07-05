package repositories;

import dto.auth.UserSessionDTO;
import lombok.RequiredArgsConstructor;
import models.entities.UserSession;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserSessionRepository {

    private final Session session;

    public Optional<UserSessionDTO> findNotExpiredById(UUID id) {
        var hql = """
                select us.id, us.expiresAt, u.id
                from UserSession us join us.user u
                where us.id = :id and us.expiresAt > current_timestamp
                """;

        return session.createQuery(hql, UserSessionDTO.class)
                .setParameter("id", id)
                .uniqueResultOptional();
    }

    public UserSession save(UserSession userSession) {
        session.persist(userSession);
        return userSession;
    }

    public void deleteById(UUID id) {
        var hql = "delete from UserSession us where us.id = :id";

        session.createMutationQuery(hql)
                .setParameter("id", id)
                .executeUpdate();
    }
}