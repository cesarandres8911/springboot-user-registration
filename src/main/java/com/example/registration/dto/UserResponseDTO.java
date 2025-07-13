package com.example.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para la respuesta de usuario tras el registro o autenticación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    /** Identificador único del usuario (UUID). */
    @Schema(example = "b3b6a7e2-8c2e-4e2a-9c2e-8c2e4e2a9c2e")
    private UUID id;

    /** Fecha de creación del usuario. */
    @Schema(example = "2025-07-11T10:15:30")
    private LocalDateTime created;

    /** Fecha de última modificación del usuario. */
    @Schema(example = "2025-07-11T10:15:30")
    private LocalDateTime modified;

    /** Fecha del último login del usuario. */
    @Schema(example = "2025-07-11T10:15:30")
    private LocalDateTime lastLogin;

    /** Nombre completo del usuario. */
    @Schema(example = "Juan Rodriguez")
    private String fullName;

    /** Correo electrónico del usuario. */
    @Schema(example = "juan@rodriguez.org")
    private String userEmail;

    /** Token de acceso del usuario. */
    @Schema(example = "token1234")
    private String userToken;

    /** Indica si el usuario está activo. */
    @Schema(example = "true")
    private boolean isActive;

    /** Lista de teléfonos asociados al usuario. */
    private List<PhoneResponseDTO> phones;
}
