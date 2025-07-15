package com.example.registration.exception;

/**
 * Excepción lanzada cuando hay un error en la validación de un token JWT.
 */
public class JwtValidationException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param message El mensaje de error
     */
    public JwtValidationException(String message) {
        super(message);
    }
}