package com.example.registration.mapper;

import com.example.registration.dto.ConfigurationResponseDTO;
import com.example.registration.model.Configuration;
import com.example.registration.model.ConfigurationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper para convertir entre entidades Configuration y DTOs ConfigurationRequestDTO y ConfigurationResponseDTO.
 */
@Mapper(componentModel = "spring", uses = {ConfigurationTypeMapper.class})
public interface ConfigurationMapper {

    /**
     * Convierte un objeto Configuration a un ConfigurationResponseDTO.
     *
     * @param configuration Entidad Configuration a convertir
     * @return DTO ConfigurationResponseDTO con la información de la configuración
     */
    @Mapping(source = "active", target = "isActive")
    ConfigurationResponseDTO configurationToConfigurationResponseDTO(Configuration configuration);


    /**
     * Crear un objeto ConfigurationType a partir de su ID.
     * Mapear el campo configurationTypeId a configurationType.
     *
     * @param id id del tipo de configuración
     * @return Objeto ConfigurationType con el ID especificado
     */
    @Named("configurationTypeFromId")
    default ConfigurationType configurationTypeFromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfigurationType configurationType = new ConfigurationType();
        configurationType.setId(id);
        return configurationType;
    }
}
