package com.example.registration.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase PasswordEncoderUtil.
 */
class PasswordEncoderUtilTest {

    private PasswordEncoderUtil passwordEncoderUtil;

    @BeforeEach
    void setUp() {
        passwordEncoderUtil = new PasswordEncoderUtil();
    }

    @Test
    void testPasswordEncoder() {
        // Verificar que el PasswordEncoder no sea nulo
        PasswordEncoder encoder = passwordEncoderUtil.getPasswordEncoder();
        assertNotNull(encoder, "El PasswordEncoder no debe ser nulo");
    }

    @Test
    void testEncodePassword() {
        // Verificar que la contraseña cifrada no sea igual a la original
        String rawPassword = "Password123!";
        String encodedPassword = passwordEncoderUtil.encode(rawPassword);

        assertNotNull(encodedPassword, "La contraseña cifrada no debe ser nula");
        assertNotEquals(rawPassword, encodedPassword, "La contraseña cifrada no debe ser igual a la original");
        assertFalse(encodedPassword.isEmpty(), "La contraseña cifrada debe tener longitud mayor a cero");
    }

    @Test
    void testMatchesPassword() {
        // Verificar que la contraseña original coincida con su versión cifrada
        String rawPassword = "Password123!";
        String encodedPassword = passwordEncoderUtil.encode(rawPassword);

        assertTrue(passwordEncoderUtil.matches(rawPassword, encodedPassword), 
                "La contraseña original debe coincidir con su versión cifrada");
    }

    @Test
    void testDifferentPasswordsDontMatch() {
        // Verificar que contraseñas diferentes no coincidan
        String password1 = "Password123!";
        String password2 = "DifferentPassword456!";
        String encodedPassword1 = passwordEncoderUtil.encode(password1);

        assertFalse(passwordEncoderUtil.matches(password2, encodedPassword1), 
                "Contraseñas diferentes no deben coincidir");
    }

    @Test
    void testNullOrEmptyPasswordThrowsException() {
        // Verificar que se lance una excepción al intentar cifrar una contraseña nula o vacía
        assertThrows(IllegalArgumentException.class, () -> passwordEncoderUtil.encode(null),
                "Debe lanzar una excepción al intentar cifrar una contraseña nula");

        assertThrows(IllegalArgumentException.class, () -> passwordEncoderUtil.encode(""),
                "Debe lanzar una excepción al intentar cifrar una contraseña vacía");
    }

    @Test
    void testNullPasswordsDontMatch() {
        // Verificar que contraseñas nulas no coincidan
        String rawPassword = "Password123!";
        String encodedPassword = passwordEncoderUtil.encode(rawPassword);

        assertFalse(passwordEncoderUtil.matches(null, encodedPassword), 
                "Una contraseña nula no debe coincidir con una contraseña cifrada");

        assertFalse(passwordEncoderUtil.matches(rawPassword, null), 
                "Una contraseña no debe coincidir con una contraseña cifrada nula");
    }
}
