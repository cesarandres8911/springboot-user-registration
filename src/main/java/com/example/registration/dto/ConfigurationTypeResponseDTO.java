package com.example.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de tipo de configuración.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationTypeResponseDTO {
    /** Identificador único del tipo de configuración. */
    @Schema(example = "1")
    private Long id;

    /** Clave del tipo de configuración. */
    @Schema(example = "password.min.length")
    private String typeKey;

    /** Indica si el tipo de configuración está activo. */
    @Schema(example = "true")
    private boolean isActive;
}
