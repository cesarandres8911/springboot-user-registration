package com.example.registration.mapper;

import com.example.registration.dto.UserRequestDTO;
import com.example.registration.dto.UserResponseDTO;
import com.example.registration.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre entidades User y DTOs UserRequestDTO y UserResponseDTO.
 */
@Mapper(componentModel = "spring", uses = {PhoneMapper.class})
public interface UserMapper {

    /**
     * Convierte un objeto User a un UserResponseDTO.
     * Mapea los campos con nombres diferentes entre las clases.
     * Los campos fullName, userEmail y phones no se incluyen en la respuesta.
     *
     * @param user Entidad User a convertir
     * @return DTO UserResponseDTO con la información del usuario
     */
    @Mapping(source = "createdAt", target = "created")
    @Mapping(source = "updatedAt", target = "modified")
    @Mapping(source = "lastLogin", target = "last_login")
    @Mapping(source = "userToken", target = "token")
    @Mapping(target = "isactive", expression = "java(user.isActive())")
    UserResponseDTO userToUserResponseDTO(User user);

    /**
     * Convierte un objeto UserRequestDTO a un User.
     * Mapea los campos con nombres diferentes entre las clases.
     *
     * @param userRequestDTO DTO UserRequestDTO a convertir
     * @return Entidad User con la información del usuario
     */
    @Mapping(source = "name", target = "fullName")
    @Mapping(source = "email", target = "userEmail")
    @Mapping(source = "password", target = "userPassword")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "userToken", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "phones", ignore = true)
    User userRequestDTOToUser(UserRequestDTO userRequestDTO);
}
