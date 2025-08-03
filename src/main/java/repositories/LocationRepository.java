package repositories;

import lombok.RequiredArgsConstructor;
import models.entities.Location;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepository {

    private final Session session;

    public Optional<Long> findLocationId(String locationName) {
        var hql = "select id from Location where name = :locationName";

        return session.createQuery(hql, Long.class)
                .setParameter("locationName", locationName)
                .uniqueResultOptional();
    }

    public Location save(Location location) {
        session.persist(location);
        return location;
    }
}