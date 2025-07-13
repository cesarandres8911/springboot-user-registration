package com.example.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de registro de teléfono.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneRequestDTO {
    /** Número de teléfono. */
    @Schema(example = "1234567")
    @NotBlank(message = "El número es obligatorio.")
    private String phoneNumber;

    /** Código de ciudad. */
    @Schema(example = "1")
    @NotBlank(message = "El código de ciudad es obligatorio.")
    private String citycode;

    /** Código de país. */
    @Schema(example = "57")
    @NotBlank(message = "El código de país es obligatorio.")
    private String contrycode;
}
