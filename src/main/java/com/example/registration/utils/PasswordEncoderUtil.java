package com.example.registration.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utilidad para el cifrado de contraseñas utilizando BCrypt.
 * <p>
 * Esta clase proporciona métodos para cifrar contraseñas y verificar si una contraseña
 * coincide con su versión cifrada. Utiliza el algoritmo BCrypt de Spring Security,
 * que es considerado una de las mejores prácticas para el almacenamiento seguro de contraseñas.
 * </p>
 */
@Component
public class PasswordEncoderUtil {

    private static final Logger logger = LoggerFactory.getLogger(PasswordEncoderUtil.class);
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor que inicializa el codificador de contraseñas BCrypt.
     */
    public PasswordEncoderUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        logger.info("Inicializado el codificador de contraseñas BCrypt");
    }

    /**
     * Proporciona el bean PasswordEncoder para ser utilizado en la aplicación.
     *
     * @return Una instancia de BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoderUtil() {
        return passwordEncoder;
    }

    /**
     * Cifra una contraseña utilizando el algoritmo BCrypt.
     *
     * @param rawPassword La contraseña en texto plano a cifrar
     * @return La contraseña cifrada
     */
    public String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        logger.debug("Contraseña cifrada correctamente");
        return encodedPassword;
    }

    /**
     * Verifica si una contraseña en texto plano coincide con una contraseña cifrada.
     *
     * @param rawPassword La contraseña en texto plano
     * @param encodedPassword La contraseña cifrada
     * @return true si la contraseña coincide, false en caso contrario
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        logger.debug("Verificación de contraseña: {}", matches ? "exitosa" : "fallida");
        return matches;
    }
}
