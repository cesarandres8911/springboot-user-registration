package com.example.registration.mapper;

import com.example.registration.dto.PhoneRequestDTO;
import com.example.registration.model.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre entidades Phone y DTO PhoneRequestDTO y PhoneResponseDTO.
 */
@Mapper(componentModel = "spring")
public interface PhoneMapper {

    /**
     * Convierte un objeto PhoneRequestDTO a un Phone.
     * Mapea los campos con nombres diferentes entre las clases.
     *
     * @param phoneRequestDTO DTO PhoneRequestDTO a convertir
     * @return Entidad Phone con la información del teléfono
     */
    @Mapping(source = "number", target = "phoneNumber")
    @Mapping(source = "citycode", target = "cityCode")
    @Mapping(source = "contrycode", target = "countryCode")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Phone phoneRequestDTOToPhone(PhoneRequestDTO phoneRequestDTO);

    /**
     * Convierte una lista de objetos PhoneRequestDTO a una lista de Phone.
     *
     * @param phoneRequestDTOs Lista de DTOs PhoneRequestDTO a convertir
     * @return Lista de entidades Phone
     */
    java.util.List<Phone> phoneRequestDTOsToPhones(java.util.List<PhoneRequestDTO> phoneRequestDTOs);
}
