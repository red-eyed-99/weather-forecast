package services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.UserLocationRepository;

@Service
@RequiredArgsConstructor
public class UserLocationService {

    private final UserLocationRepository userLocationRepository;

    @Transactional
    public void addLocationToUser(Long locationId, Long userId) {
        userLocationRepository.addLocationToUser(locationId, userId);
    }

    @Transactional
    public void removeLocationFromUser(Long locationId, Long userId) {
        userLocationRepository.removeLocationFromUser(locationId, userId);
    }

    @Transactional(readOnly = true)
    public boolean locationAdded(Long locationId, Long userId) {
        return userLocationRepository.locationAdded(locationId, userId);
    }

    @Transactional(readOnly = true)
    public int getAddedLocationsNumber(Long userId) {
        return userLocationRepository.getAddedLocationsNumber(userId);
    }
}