package com.example.registration.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa un tipo de configuración del sistema.
 */
@Entity
@Table(name = "configuration_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationType {
    /** Identificador único del tipo de configuración. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Clave del tipo de configuración. */
    @Column(name = "type_key", nullable = false, unique = true)
    private String typeKey;

    /** Descripción del tipo de configuración. */
    @Column(name = "description")
    private String description;

    /** Indica si el tipo de configuración está activo. */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /** Fecha de creación del tipo de configuración. */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** Fecha de última modificación del tipo de configuración. */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
