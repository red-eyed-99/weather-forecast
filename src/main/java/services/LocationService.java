package services;

import dto.openweather.CoordinatesDTO;
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
    public Optional<Long> findLocationId(CoordinatesDTO coordinatesDTO) {
        var latitude = coordinatesDTO.latitude();
        var longitude = coordinatesDTO.longitude();
        return locationRepository.findLocationId(latitude, longitude);
    }

    @Transactional
    public Location addLocation(CoordinatesDTO coordinatesDTO) {
        var weatherResponseDTO = openWeatherService.getWeatherInfo(coordinatesDTO);

        var locationName = weatherResponseDTO.locationDto().name();

        var location = Location.builder()
                .name(locationName)
                .latitude(coordinatesDTO.latitude())
                .longitude(coordinatesDTO.longitude())
                .build();

        return locationRepository.save(location);
    }
}