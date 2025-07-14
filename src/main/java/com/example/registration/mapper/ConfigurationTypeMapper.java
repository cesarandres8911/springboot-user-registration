package com.example.registration.mapper;

import com.example.registration.dto.ConfigurationTypeResponseDTO;
import com.example.registration.model.ConfigurationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre entidades ConfigurationType y DTOs ConfigurationTypeRequestDTO y ConfigurationTypeResponseDTO.
 */
@Mapper(componentModel = "spring")
public interface ConfigurationTypeMapper {

    /**
     * Convierte un objeto ConfigurationType a un ConfigurationTypeResponseDTO.
     *
     * @param configurationType Entidad ConfigurationType a convertir
     * @return DTO ConfigurationTypeResponseDTO con la información del tipo de configuración
     */
    @Mapping(source = "active", target = "isActive")
    ConfigurationTypeResponseDTO configurationTypeToConfigurationTypeResponseDTO(ConfigurationType configurationType);
}
