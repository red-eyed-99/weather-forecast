package repositories;

import lombok.RequiredArgsConstructor;
import models.entities.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final Session session;

    public Optional<User> findByUsername(String username) {
        var hql = "from User where username = :username";

        return session.createQuery(hql, User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public User save(User user) {
        session.persist(user);
        return user;
    }
}
