package services;

import dto.auth.SignUpUserDTO;
import dto.openweather.CoordinatesDTO;
import exceptions.NotFoundException;
import exceptions.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import mappers.UserMapper;
import models.entities.User;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final LocationService locationService;

    private final UserLocationService userLocationService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findByIdWithLocations(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional
    public User save(SignUpUserDTO signUpUserDTO) {
        var user = userMapper.toUser(signUpUserDTO);

        try {
            return userRepository.save(user);
        } catch (ConstraintViolationException exception) {
            throw new UserAlreadyExistException();
        }
    }

    public void addLocation(Long userId, CoordinatesDTO coordinatesDTO) {
        var locationIdOptional = locationService.findLocationId(coordinatesDTO);
        var locationId = (Long) null;

        if (locationIdOptional.isPresent()) {
            locationId = locationIdOptional.get();
        } else {
            var location = locationService.addLocation(coordinatesDTO);
            locationId = location.getId();
        }

        userLocationService.addLocationToUser(locationId, userId);
    }
}
