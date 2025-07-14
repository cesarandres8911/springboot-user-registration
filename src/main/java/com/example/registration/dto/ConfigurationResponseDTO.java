package com.example.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para la respuesta de configuración.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationResponseDTO {
    /** Identificador único de la configuración. */
    @Schema(example = "1")
    private Long id;

    /** Tipo de configuración asociado. */
    private ConfigurationTypeResponseDTO configurationType;

    /** Valor de la configuración. */
    @Schema(example = "8")
    private String configValue;

    /** Indica si la configuración está activa. */
    @Schema(example = "true")
    private boolean isActive;

    /** Fecha de creación de la configuración. */
    @Schema(example = "2025-07-11T10:15:30")
    private LocalDateTime createdAt;

    /** Fecha de última modificación de la configuración. */
    @Schema(example = "2025-07-11T10:15:30")
    private LocalDateTime updatedAt;
}
