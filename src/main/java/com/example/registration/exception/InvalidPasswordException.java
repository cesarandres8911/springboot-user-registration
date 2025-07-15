package com.example.registration.exception;

/**
 * Excepción que se lanza cuando una contraseña no cumple con los requisitos de validación.
 */
public class InvalidPasswordException extends RuntimeException {

    /**
     * Crea una nueva instancia de InvalidPasswordException con un mensaje personalizado.
     *
     * @param message El mensaje de error
     */
    public InvalidPasswordException(String message) {
        super(message);
    }

}