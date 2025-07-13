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
    private String fullName;

    /** Correo electrónico del usuario. */
    @Schema(example = "juan@rodriguez.org")
    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo debe tener un formato válido.")
    private String userEmail;

    /** Contraseña del usuario. */
    @Schema(example = "hunter2")
    @NotBlank(message = "La contraseña es obligatoria.")
    private String userPassword;

    /** Lista de teléfonos asociados al usuario. */
    @NotEmpty(message = "Debe proporcionar al menos un teléfono.")
    private List<PhoneRequestDTO> phones;
}
