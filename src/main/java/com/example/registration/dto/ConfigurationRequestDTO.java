package com.example.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de creación o actualización de configuración.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationRequestDTO {
    /** ID del tipo de configuración asociado. */
    @Schema(example = "1")
    @NotNull(message = "El ID del tipo de configuración es obligatorio.")
    private Long configurationTypeId;

    /** Valor de la configuración. */
    @Schema(example = "8")
    @NotBlank(message = "El valor de configuración es obligatorio.")
    private String configValue;
}
