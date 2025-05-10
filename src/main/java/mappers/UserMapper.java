package mappers;

import dto.SignUpUserDTO;
import models.entities.User;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    User toUser(SignUpUserDTO signUpUserDTO);
}
