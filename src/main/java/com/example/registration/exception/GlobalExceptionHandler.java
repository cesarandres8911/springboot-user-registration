package com.example.registration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación.
 * Proporciona respuestas consistentes para diferentes tipos de errores.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MESSAGE_KEY = "mensaje";

    /**
     * Maneja excepciones de validación de argumentos.
     * Estas excepciones ocurren cuando la validación de los campos de un DTO falla.
     *
     * @param ex La excepción de validación
     * @return ResponseEntity con un mapa de errores de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            if (!errorMessages.isEmpty()) {
                errorMessages.append(", ");
            }
            errorMessages.append(fieldName).append(" ").append(errorMessage);
        });

        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE_KEY, !errorMessages.isEmpty() ? errorMessages.toString() : "Error de validación");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de argumento ilegal.
     * Estas excepciones ocurren cuando se proporciona un valor inválido para un campo.
     *
     * @param ex La excepción de argumento ilegal
     * @return ResponseEntity con un mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE_KEY, ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de credenciales inválidas.
     * Estas excepciones ocurren cuando se proporciona una contraseña incorrecta.
     *
     * @param ignored La excepción de credenciales inválidas
     * @return ResponseEntity con un mensaje de error
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ignored) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE_KEY, "Credenciales inválidas");

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja excepciones de usuario no encontrado.
     * Estas excepciones ocurren cuando se intenta acceder a un usuario que no existe.
     *
     * @param ex La excepción de usuario no encontrado
     * @return ResponseEntity con un mensaje de error
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE_KEY, ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de contraseña inválida.
     * Estas excepciones ocurren cuando una contraseña no cumple con los requisitos de validación.
     *
     * @param ex La excepción de contraseña inválida
     * @return ResponseEntity con un mensaje de error
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE_KEY, ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de validación de JWT.
     * Estas excepciones ocurren cuando hay un error en la validación de un token JWT.
     *
     * @param ex La excepción de validación de JWT
     * @return ResponseEntity con un mensaje de error
     */
    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<Object> handleJwtValidationException(JwtValidationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE_KEY, ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja excepciones de acceso denegado.
     * Estas excepciones ocurren cuando un usuario no tiene los permisos necesarios para acceder a un recurso.
     *
     * @param ignored La excepción de acceso denegado
     * @return ResponseEntity con un mensaje de error
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ignored) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE_KEY, "Acceso denegado: No tiene permisos para acceder a este recurso");

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Maneja excepciones generales.
     * Este es un manejador de respaldo para cualquier excepción no manejada específicamente.
     *
     * @param ex La excepción general
     * @return ResponseEntity con un mensaje de error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE_KEY, "Error en el servidor: " + ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
