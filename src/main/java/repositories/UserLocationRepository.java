package repositories;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserLocationRepository {

    private static final String USER_ID_PARAMETER = "userId";
    private static final String LOCATION_ID_PARAMETER = "locationId";

    private final Session session;

    public void addLocationToUser(Long locationId, Long userId) {
        var sql = "insert into users_locations (user_id, location_id) values (:userId, :locationId)";

        session.createNativeMutationQuery(sql)
                .setParameter(USER_ID_PARAMETER, userId)
                .setParameter(LOCATION_ID_PARAMETER, locationId)
                .executeUpdate();
    }

    public void removeLocationFromUser(Long locationId, Long userId) {
        var sql = "delete from users_locations where user_id = :userId and location_id = :locationId";

        session.createNativeMutationQuery(sql)
                .setParameter(USER_ID_PARAMETER, userId)
                .setParameter(LOCATION_ID_PARAMETER, locationId)
                .executeUpdate();
    }

    public boolean locationAdded(Long locationId, Long userId) {
        var sql = "select count(*) from users_locations where user_id = :userId and location_id = :locationId";

        return session.createNativeQuery(sql, Boolean.class)
                .setParameter(USER_ID_PARAMETER, userId)
                .setParameter(LOCATION_ID_PARAMETER, locationId)
                .getSingleResult();
    }
}