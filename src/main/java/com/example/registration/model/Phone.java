package com.example.registration.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un teléfono asociado a un usuario.
 */
@Entity
@Table(name = "phones")
@Getter
@Setter
@ToString
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
    @Column(name = "city_code", nullable = false)
    private String cityCode;

    /** Código de país. */
    @Column(name = "country_code", nullable = false)
    private String countryCode;

    /** Usuario al que pertenece el teléfono. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}
