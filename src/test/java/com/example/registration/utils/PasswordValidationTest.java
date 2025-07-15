package com.example.registration.utils;

import com.example.registration.model.Configuration;
import com.example.registration.model.ConfigurationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para las utilidades de validación de contraseñas.
 */
class PasswordValidationTest {

    private List<Configuration> configurations;
    private PasswordValidator validator;

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

        // Inicializar el validador
        validator = new PasswordValidator(configurations);
    }

    /**
     * Prueba la generación de expresiones regulares con configuraciones válidas.
     */
    @Test
    void testRegexGeneration() {
        String regex = PasswordRegexGenerator.generateRegex(configurations);
        assertNotNull(regex);
        assertTrue(regex.contains("^(?=.{8,30})"));
        System.out.println("Expresión regular generada: " + regex);
    }

    /**
     * Prueba la validación de contraseñas válidas.
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
        assertEquals(expectedResult, validator.isValid(password));
    }

    /**
     * Prueba la generación de expresiones regulares con configuraciones inválidas.
     */
    @Test
    void testInvalidConfigurations() {
        // Probar con lista vacía
        List<Configuration> emptyConfigs = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> PasswordRegexGenerator.generateRegex(emptyConfigs));

        // Probar con min > max
        List<Configuration> invalidConfigs = new ArrayList<>();
        addConfigurationToList(invalidConfigs, "password.min.length", "40");
        addConfigurationToList(invalidConfigs, "password.max.length", "30");

        assertThrows(IllegalArgumentException.class, () -> PasswordRegexGenerator.generateRegex(invalidConfigs));
    }

    /**
     * Prueba la obtención del patrón de expresión regular.
     */
    @Test
    void testGetRegexPattern() {
        String pattern = validator.getRegexPattern();
        assertNotNull(pattern);
        assertTrue(pattern.startsWith("^"));
        assertTrue(pattern.endsWith("$"));
    }

    /**
     * Prueba la validación con diferentes configuraciones.
     */
    @Test
    void testDifferentConfigurations() {
        // Configuración con solo longitud mínima
        List<Configuration> minLengthOnly = new ArrayList<>();
        addConfigurationToList(minLengthOnly, "password.min.length", "6");

        PasswordValidator simpleValidator = new PasswordValidator(minLengthOnly);
        assertTrue(simpleValidator.isValid("simple"));
        assertFalse(simpleValidator.isValid("short"));

        // Configuración con solo dígitos
        List<Configuration> digitsOnly = new ArrayList<>();
        addConfigurationToList(digitsOnly, "password.min.length", "4");
        addConfigurationToList(digitsOnly, "password.min.digits", "2");

        PasswordValidator digitsValidator = new PasswordValidator(digitsOnly);
        assertTrue(digitsValidator.isValid("test12"));
        assertFalse(digitsValidator.isValid("test1"));
    }

    /**
     * Agregar configuraciones a la lista principal.
     */
    private void addConfiguration(String typeKey, String value) {
        addConfigurationToList(configurations, typeKey, value);
    }

    /**
     * Agregar configuraciones a una lista específica.
     */
    private void addConfigurationToList(List<Configuration> configList, String typeKey, String value) {
        ConfigurationType type = new ConfigurationType();
        type.setId(configList.size() + 1L);
        type.setTypeKey(typeKey);
        type.setDescription("Test description for " + typeKey);
        type.setActive(true);
        type.setCreatedAt(LocalDateTime.now());
        type.setUpdatedAt(LocalDateTime.now());

        Configuration config = new Configuration();
        config.setId(configList.size() + 1L);
        config.setConfigurationType(type);
        config.setConfigValue(value);
        config.setActive(true);
        config.setCreatedAt(LocalDateTime.now());
        config.setUpdatedAt(LocalDateTime.now());

        configList.add(config);
    }
}
