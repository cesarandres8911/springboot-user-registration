package com.example.registration.service;

import com.example.registration.model.Configuration;
import com.example.registration.model.ConfigurationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el servicio de validación de contraseñas.
 */
class PasswordValidationServiceTest {

    private ConfigurationService configurationService;
    private PasswordValidationService passwordValidationService;
    private List<Configuration> configurations;

    /**
     * Configura las pruebas con configuraciones predeterminadas.
     */
    @BeforeEach
    void setup() {
        configurations = new ArrayList<>();

        // Crear configuraciones de prueba
        addConfiguration("password.min.length", "8");
        addConfiguration("password.max.length", "30");
        addConfiguration("password.min.uppercase", "1");
        addConfiguration("password.min.lowercase", "1");
        addConfiguration("password.min.digits", "1");
        addConfiguration("password.min.special", "1");
        addConfiguration("password.allowed.special", "-.#&");

        // Crear el mock del servicio de configuración
        configurationService = Mockito.mock(ConfigurationService.class);

        // Configurar el mock para devolver las configuraciones
        when(configurationService.getPasswordConfigurations()).thenReturn(configurations);

        // Crear el servicio de validación de contraseñas con el mock
        passwordValidationService = new PasswordValidationService(configurationService);
    }

    /**
     * Prueba la validación de contraseñas válidas e inválidas.
     */
    @ParameterizedTest
    @CsvSource({
            "Password1#, true",
            "Abcdef1&, true",
            "StrongP4$$w0rd, false", // $ no está en los caracteres especiales permitidos
            "Pass1-, false", // muy corta (6 caracteres, mínimo 8)
            "VeryLongPassword123456789.#, true",
            "short1A#, true", // 8 caracteres, cumple con el mínimo
            "ALLCAPS123#, false", // falta minúscula
            "alllowercase123#, false", // falta mayúscula
            "Password#, false", // falta dígito
            "PASSWORD1#, false", // falta minúscula
            "Password12, false", // falta carácter especial
            "Pa1#, false", // muy corta
            "ThisPasswordIsWayTooLongForTheMaximumAllowedLength1234567890#, false" // muy larga
    })
    void testPasswordValidation(String password, boolean expectedResult) {
        // Validar la contraseña
        assertEquals(expectedResult, passwordValidationService.isPasswordValid(password));
    }

    /**
     * Prueba la obtención del patrón de expresión regular.
     */
    @Test
    void testGetPasswordRegexPattern() {
        // Obtener el patrón
        String pattern = passwordValidationService.getPasswordRegexPattern();

        // Verificar el patrón
        assertNotNull(pattern);
        assertTrue(pattern.startsWith("^"));
        assertTrue(pattern.endsWith("$"));
    }

    /**
     * Prueba la inicialización del validador cuando no hay configuraciones.
     */
    @Test
    void testInitializeValidatorWithEmptyConfigurations() {
        // Crear un nuevo mock para este test específico
        ConfigurationService emptyConfigService = Mockito.mock(ConfigurationService.class);

        // Configurar el mock para devolver una lista vacía
        when(emptyConfigService.getPasswordConfigurations()).thenReturn(new ArrayList<>());

        // Verificar que se lanza una excepción al crear una nueva instancia del servicio
        assertThrows(IllegalStateException.class, () -> new PasswordValidationService(emptyConfigService));
    }

    /**
     * Prueba que el servicio llama a la función getPasswordConfigurations del ConfigurationService.
     */
    @Test
    void testConfigurationServiceIsCalled() {
        // Reiniciar el contador de llamadas al mock
        reset(configurationService);

        // Configurar el mock para devolver las configuraciones
        when(configurationService.getPasswordConfigurations()).thenReturn(configurations);

        // Llamar a la función que debería invocar a getPasswordConfigurations
        passwordValidationService.initializeValidator();

        // Verificar que se llamó a la función getPasswordConfigurations
        verify(configurationService, times(1)).getPasswordConfigurations();
    }

    /**
     * Agregar configuraciones a la lista.
     */
    private void addConfiguration(String typeKey, String value) {
        ConfigurationType type = new ConfigurationType();
        type.setId(configurations.size() + 1L);
        type.setTypeKey(typeKey);
        type.setDescription("Test description for " + typeKey);
        type.setActive(true);
        type.setCreatedAt(LocalDateTime.now());
        type.setUpdatedAt(LocalDateTime.now());

        Configuration config = new Configuration();
        config.setId(configurations.size() + 1L);
        config.setConfigurationType(type);
        config.setConfigValue(value);
        config.setActive(true);
        config.setCreatedAt(LocalDateTime.now());
        config.setUpdatedAt(LocalDateTime.now());

        configurations.add(config);
    }
}
