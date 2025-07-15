package com.example.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de creación o actualización de tipo de configuración.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationTypeRequestDTO {
    /** Clave del tipo de configuración. */
    @Schema(example = "password.min.length")
    @NotBlank(message = "La clave del tipo de configuración es obligatoria.")
    private String typeKey;

    /** Descripción del tipo de configuración. */
    @Schema(example = "Longitud mínima de caracteres para la contraseña")
    private String description;
}