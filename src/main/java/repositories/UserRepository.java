package repositories;

import lombok.RequiredArgsConstructor;
import models.entities.User;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
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

    public Optional<User> findByIdWithLocations(Long id) {
        var hql = "from User where id = :id";

        var entityGraph = session.createEntityGraph(User.class);

        entityGraph.addAttributeNodes("locations");

        return session.createQuery(hql, User.class)
                .setParameter("id", id)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), entityGraph)
                .uniqueResultOptional();
    }

    public User save(User user) {
        session.persist(user);
        return user;
    }
}
