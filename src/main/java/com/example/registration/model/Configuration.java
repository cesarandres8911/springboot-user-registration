package com.example.registration.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa una configuración del sistema.
 * Esta entidad almacena valores de configuración asociados a tipos de configuración.
 */
@Entity
@Table(name = "configuration")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Configuration {
    /** Identificador único de la configuración. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo de configuración asociado. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "configuration_type_id", nullable = false)
    private ConfigurationType configurationType;

    /** Valor de la configuración. */
    @Column(name = "config_value", nullable = false)
    private String configValue;

    /** Indica si la configuración está activa. */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /** Fecha de creación de la configuración. */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** Fecha de última modificación de la configuración. */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
