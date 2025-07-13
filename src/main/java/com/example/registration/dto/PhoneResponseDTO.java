package com.example.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de teléfono asociado a un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneResponseDTO {
    /** Número de teléfono. */
    @Schema(example = "1234567")
    private String phoneNumber;

    /** Código de ciudad. */
    @Schema(example = "1")
    private String citycode;

    /** Código de país. */
    @Schema(example = "57")
    private String contrycode;
}
