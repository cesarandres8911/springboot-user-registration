package com.example.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para la solicitud de registro de usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    /** Nombre completo del usuario. */
    @Schema(example = "Juan Rodriguez")
    @NotBlank(message = "El nombre es obligatorio.")
    private String name;

    /** Correo electrónico del usuario. */
    @Schema(example = "juan@rodriguez.cl")
    @NotBlank(message = "El correo es obligatorio.")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.cl$", message = "El correo debe tener un formato válido y terminar en .cl (ejemplo: correo@dominio.cl)")
    private String email;

    /** Contraseña del usuario. */
    @Schema(example = "hunter2")
    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    /** Lista de teléfonos asociados al usuario. */
    @NotEmpty(message = "Debe proporcionar al menos un teléfono.")
    private List<PhoneRequestDTO> phones;
}
