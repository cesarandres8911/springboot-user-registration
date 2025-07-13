package com.example.registration.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa un usuario en el sistema.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    /** Identificador único del usuario (UUID). */
    @Id
    @GeneratedValue
    private UUID id;

    /** Nombre completo del usuario. */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /** Correo electrónico del usuario. */
    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    /** Contraseña del usuario. */
    @Column(name = "user_password", nullable = false)
    private String userPassword;

    /** Fecha del último login del usuario. */
    @Column(name = "last_login", nullable = false)
    private LocalDateTime lastLogin;

    /** Token de acceso del usuario. */
    @Column(name = "user_token", nullable = false, length = 512)
    private String userToken;

    /** Indica si el usuario está activo. */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /** Fecha de creación del usuario. */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** Fecha de última modificación del usuario. */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /** Lista de teléfonos asociados al usuario. */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Phone> phones;
}
