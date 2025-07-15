package com.example.registration.utils;

import com.example.registration.model.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Utilidad para validar contraseñas según configuraciones almacenadas en la base de datos.
 * <p>
 * Esta clase utiliza PasswordRegexGenerator para generar una expresión regular
 * basada en configuraciones y luego validar las contraseñas contra esa expresión.
 */
public class PasswordValidator {

    private static final Logger logger = LoggerFactory.getLogger(PasswordValidator.class);
    private final Pattern passwordPattern;
    private final String allowedSpecialChars;

    /**
     * Constructor que inicializa el validador con una lista de configuraciones.
     *
     * @param configurations Lista de configuraciones para validación de contraseñas.
     * @throws IllegalArgumentException Si no se puede generar una expresión regular válida.
     */
    public PasswordValidator(List<Configuration> configurations) {
        String regex = PasswordRegexGenerator.generateRegex(configurations);
        this.passwordPattern = Pattern.compile(regex);

        // Extract allowed special characters from configurations
        String specialChars = null;
        for (Configuration config : configurations) {
            if (config.getConfigurationType() != null && 
                PasswordConfigurationType.ALLOWED_SPECIAL.getTypeKey().equals(config.getConfigurationType().getTypeKey())) {
                specialChars = config.getConfigValue();
                break;
            }
        }

        // Use default if not found in configurations
        this.allowedSpecialChars = (specialChars != null) ? specialChars : "-.#$%&";
        logger.info("Validador de contraseñas inicializado con expresión regular: {} y caracteres especiales permitidos: {}", 
                    regex, this.allowedSpecialChars);
    }

    /**
     * Validar si una contraseña cumple con las reglas de configuración.
     *
     * @param password La contraseña a validar.
     * @return true si la contraseña cumple con todas las reglas, false en caso contrario.
     */
    public boolean isValid(String password) {
        if (password == null) {
            logger.warn("Se intentó validar una contraseña nula");
            return false;
        }

        // Verificar longitud máxima explícitamente
        if (password.length() > 30) {
            logger.debug("Contraseña inválida: excede la longitud máxima permitida");
            return false;
        }

        // Verificar caracteres especiales permitidos
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (!Character.isLetterOrDigit(c) && this.allowedSpecialChars.indexOf(c) == -1) {
                logger.debug("Contraseña inválida: contiene caracteres especiales no permitidos: {}", c);
                return false;
            }
        }

        boolean isValid = passwordPattern.matcher(password).matches();

        if (!isValid) {
            logger.debug("Contraseña inválida: no cumple con la expresión regular");
        }

        return isValid;
    }

    /**
     * Obtiene la expresión regular utilizada para validar contraseñas.
     *
     * @return La expresión regular como cadena de texto.
     */
    public String getRegexPattern() {
        return passwordPattern.pattern();
    }
}
