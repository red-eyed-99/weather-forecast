package repositories;

import lombok.RequiredArgsConstructor;
import models.entities.Location;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepository {

    private final Session session;

    public Optional<Long> findLocationId(BigDecimal latitude, BigDecimal longitude) {
        var hql = "select id from Location where latitude = :latitude and longitude = :longitude";

        return session.createQuery(hql, Long.class)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .uniqueResultOptional();
    }

    public Location save(Location location) {
        session.persist(location);
        return location;
    }
}