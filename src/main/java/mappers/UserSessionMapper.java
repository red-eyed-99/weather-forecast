package mappers;

import dto.UserSessionDTO;
import models.entities.UserSession;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserSessionMapper {

    UserSession toUserSession(UserSessionDTO userSessionDTO);
}
