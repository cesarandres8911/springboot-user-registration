package com.example.registration.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un teléfono asociado a un usuario.
 */
@Entity
@Table(name = "phones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {
    /** Identificador único del teléfono. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Número de teléfono. */
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    /** Código de ciudad. */
    @Column(nullable = false)
    private String citycode;

    /** Código de país. */
    @Column(nullable = false)
    private String contrycode;

    /** Usuario al que pertenece el teléfono. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
