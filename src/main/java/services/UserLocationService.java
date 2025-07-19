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
}