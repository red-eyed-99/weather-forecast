package services;

import lombok.RequiredArgsConstructor;
import models.entities.Location;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.LocationRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final OpenWeatherService openWeatherService;

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public Optional<Long> findLocationId(String locationName) {
        return locationRepository.findLocationId(locationName);
    }

    @Transactional
    public Location addLocation(String locationName) {
        var weatherResponseDTO = openWeatherService.getWeatherInfo(locationName);

        var coordinatesDto = weatherResponseDTO.getLocationDto().coordinatesDto();

        var latitude = coordinatesDto.latitude();
        var longitude = coordinatesDto.longitude();

        locationName = weatherResponseDTO.getLocationDto().name();

        var location = Location.builder()
                .name(locationName)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return locationRepository.save(location);
    }
}