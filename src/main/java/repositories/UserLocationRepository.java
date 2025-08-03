package repositories;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserLocationRepository {

    private final Session session;

    public void addLocationToUser(Long locationId, Long userId) {
        var sql = "insert into users_locations (user_id, location_id) values (:userId, :locationId)";

        session.createNativeMutationQuery(sql)
                .setParameter("userId", userId)
                .setParameter("locationId", locationId)
                .executeUpdate();
    }

    public void removeLocationFromUser(Long locationId, Long userId) {
        var sql = "delete from users_locations where user_id = :userId and location_id = :locationId";

        session.createNativeMutationQuery(sql)
                .setParameter("userId", userId)
                .setParameter("locationId", locationId)
                .executeUpdate();
    }
}