package com.example.registration.service;

import com.example.registration.model.Configuration;
import com.example.registration.utils.PasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la validación de contraseñas.
 * Proporciona métodos para validar contraseñas según configuraciones almacenadas en la base de datos.
 */
@Service
public class PasswordValidationService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordValidationService.class);

    private final ConfigurationService configurationService;
    private PasswordValidator passwordValidator;

    public PasswordValidationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
        initializeValidator();
    }

    /**
     * Inicializa el validador de contraseñas con las configuraciones actuales.
     * Esta función se llama automáticamente al iniciar el servicio y puede ser llamado
     * manualmente cuando se actualizan las configuraciones.
     */
    public void initializeValidator() {
        List<Configuration> passwordConfigurations = configurationService.getPasswordConfigurations();

        if (passwordConfigurations.isEmpty()) {
            logger.warn("No se encontraron configuraciones de contraseña.");
            throw new IllegalStateException("No se pudo inicializar el validador de contraseñas");
        }

        try {
            this.passwordValidator = new PasswordValidator(passwordConfigurations);
            logger.info("Validador de contraseñas inicializado con éxito. Patrón: {}", 
                    this.passwordValidator.getRegexPattern());
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo inicializar el validador de contraseñas", e);
        }
    }

    /**
     * Validar si una contraseña cumple con las reglas configuradas.
     *
     * @param password La contraseña a validar
     * @return true si la contraseña es válida, false en caso contrario
     */
    public boolean isPasswordValid(String password) {
        if (passwordValidator == null) {
            initializeValidator();
        }

        boolean isValid = passwordValidator.isValid(password);

        if (!isValid) {
            logger.debug("Contraseña inválida: no cumple con el patrón {}", 
                    passwordValidator.getRegexPattern());
        }

        return isValid;
    }

    /**
     * Obtiene el patrón de expresión regular utilizado para validar contraseñas.
     *
     * @return El patrón de expresión regular como cadena de texto
     */
    public String getPasswordRegexPattern() {
        if (passwordValidator == null) {
            initializeValidator();
        }

        return passwordValidator.getRegexPattern();
    }
}
