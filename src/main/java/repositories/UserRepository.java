package repositories;

import lombok.RequiredArgsConstructor;
import models.entities.Location;
import models.entities.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public Optional<User> findByIdWithLocations(Long id, int limit, int offset) {
        var sql = """
                SELECT u.id AS user_id, u.username, l.id AS location_id, l.name AS location_name, l.latitude, l.longitude
                FROM users u
                    LEFT JOIN users_locations ul ON u.id = ul.user_id
                    LEFT JOIN locations l ON ul.location_id = l.id
                WHERE u.id = :id
                LIMIT :limit
                OFFSET :offset
                """;

        var rows = session.createNativeQuery(sql, Map.class)
                .setParameter("id", id)
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .setTupleTransformer((tuple, aliases) -> {
                    var row = new HashMap<String, Object>();

                    for (int i = 0; i < aliases.length; i++) {
                        row.put(aliases[i].toLowerCase(), tuple[i]);
                    }

                    return row;
                })
                .list();

        if (rows.isEmpty()) {
            return Optional.empty();
        }

        var user = getUser(rows);

        return Optional.of(user);
    }

    private User getUser(List<HashMap<String, Object>> rows) {
        var firstRow = rows.getFirst();

        var userId = (Long) firstRow.get("user_id");
        var username = (String) firstRow.get("username");

        var locations = getLocations(rows);

        return User.builder()
                .id(userId)
                .username(username)
                .locations(locations)
                .build();
    }

    private List<Location> getLocations(List<HashMap<String, Object>> rows) {
        return rows.stream()
                .map(row -> {
                    var id = (Long) row.get("location_id");

                    if (id != null) {
                        return Location.builder()
                                .id(id)
                                .name((String) row.get("location_name"))
                                .latitude((BigDecimal) row.get("latitude"))
                                .longitude((BigDecimal) row.get("longitude"))
                                .build();
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public User save(User user) {
        session.persist(user);
        return user;
    }
}
