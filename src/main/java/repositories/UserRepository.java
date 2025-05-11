package repositories;

import lombok.RequiredArgsConstructor;
import models.entities.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final Session session;

    public User save(User user) {
        session.persist(user);
        return user;
    }
}
