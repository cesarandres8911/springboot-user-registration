package com.example.registration.utils;

import com.example.registration.model.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Utilidad para generar expresiones regulares para validación de contraseñas
 * basadas en configuraciones almacenadas en la base de datos.
 * <p>
 * Esta clase toma una lista de objetos Configuration y genera una expresión regular
 * que puede ser utilizada para validar contraseñas según los criterios especificados.
 */
public class PasswordRegexGenerator {

    private PasswordRegexGenerator() {
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada.");
    }

    private static final Logger logger = LoggerFactory.getLogger(PasswordRegexGenerator.class);

    /**
     * Genera una expresión regular para validar contraseñas basada en una lista de configuraciones.
     *
     * @param configurations Lista de configuraciones para validación de contraseñas.
     * @return Una expresión regular que valida las contraseñas según las configuraciones proporcionadas.
     * @throws IllegalArgumentException Si alguna configuración es inválida o si no se pueden generar
     *                                  expresiones regulares válidas.
     */
    public static String generateRegex(List<Configuration> configurations) {
        if (configurations == null || configurations.isEmpty()) {
            logger.warn("No se proporcionaron configuraciones para generar la expresión regular");
            throw new IllegalArgumentException("La lista de configuraciones no puede ser nula o vacía");
        }

        // Mapa para almacenar los valores de configuración por tipo
        Map<PasswordConfigurationType, String> configMap = new EnumMap<>(PasswordConfigurationType.class);

        // Procesar cada configuración y almacenarla en el mapa
        for (Configuration config : configurations) {
            if (config.getConfigurationType() == null || config.getConfigurationType().getTypeKey() == null) {
                logger.warn("Configuración inválida: tipo de configuración nulo");
                continue;
            }

            PasswordConfigurationType type = PasswordConfigurationType.fromTypeKey(
                    config.getConfigurationType().getTypeKey());

            if (type != null && config.getConfigValue() != null) {
                configMap.put(type, config.getConfigValue());
                logger.debug("Configuración procesada: {} = {}", type, config.getConfigValue());
            } else {
                logger.warn("Configuración no reconocida o valor nulo: {}",
                        config.getConfigurationType().getTypeKey());
            }
        }

        // Construir la expresión regular
        return buildRegexFromConfig(configMap);
    }

    /**
     * Construye una expresión regular a partir del mapa de configuraciones.
     *
     * @param configMap Mapa de configuraciones por tipo.
     * @return Una expresión regular para validación de contraseñas.
     * @throws IllegalArgumentException Si no se puede construir una expresión regular válida.
     */
    private static String buildRegexFromConfig(Map<PasswordConfigurationType, String> configMap) {
        // Validar longitud mínima y máxima
        int minLength = getIntConfig(configMap, PasswordConfigurationType.MIN_LENGTH, 8);
        int maxLength = getIntConfig(configMap, PasswordConfigurationType.MAX_LENGTH, 30);

        validateLengthConstraints(minLength, maxLength);

        // Construir la expresión regular
        String regex = buildBaseRegex(configMap, minLength, maxLength);

        // Validar y ajustar la expresión regular si es necesario
        return validateAndAdjustRegex(regex, configMap, minLength, maxLength);
    }

    /**
     * Validar que la longitud mínima no sea mayor que la longitud máxima.
     *
     * @param minLength Longitud mínima.
     * @param maxLength Longitud máxima.
     * @throws IllegalArgumentException Si la longitud mínima es mayor que la longitud máxima.
     */
    private static void validateLengthConstraints(int minLength, int maxLength) {
        if (minLength > maxLength) {
            logger.error("La longitud mínima ({}) no puede ser mayor que la longitud máxima ({})",
                    minLength, maxLength);
            throw new IllegalArgumentException(
                    "La longitud mínima no puede ser mayor que la longitud máxima");
        }
    }

    /**
     * Construye la expresión regular base con todas las restricciones.
     *
     * @param configMap Mapa de configuraciones.
     * @param minLength Longitud mínima.
     * @param maxLength Longitud máxima.
     * @return La expresión regular base.
     */
    private static String buildBaseRegex(Map<PasswordConfigurationType, String> configMap,
                                         int minLength, int maxLength) {
        StringBuilder regexBuilder = new StringBuilder();

        // Iniciar la expresión regular con un lookahead positivo para la longitud
        regexBuilder.append("^(?=.{").append(minLength).append(",").append(maxLength).append("})");

        // Añadir restricciones de caracteres
        addCharacterRestriction(regexBuilder, configMap, PasswordConfigurationType.MIN_UPPERCASE, "[A-Z]");
        addCharacterRestriction(regexBuilder, configMap, PasswordConfigurationType.MIN_LOWERCASE, "[a-z]");
        addCharacterRestriction(regexBuilder, configMap, PasswordConfigurationType.MIN_DIGITS, "\\d");

        // Añadir restricción de caracteres especiales
        String allowedSpecial = configMap.getOrDefault(PasswordConfigurationType.ALLOWED_SPECIAL, "-.#$%&");
        String escapedSpecial = Pattern.quote(allowedSpecial);

        int minSpecial = getIntConfig(configMap, PasswordConfigurationType.MIN_SPECIAL, 0);
        if (minSpecial > 0) {
            regexBuilder.append("(?=(?:.*[").append(escapedSpecial).append("]){")
                    .append(minSpecial).append(",})");
        }

        // Permitir solo caracteres válidos en la contraseña
        regexBuilder.append("[A-Za-z\\d").append(escapedSpecial).append("]*$");

        return regexBuilder.toString();
    }

    /**
     * Añade una restricción de caracteres a la expresión regular.
     *
     * @param regexBuilder      Constructor de la expresión regular.
     * @param configMap         Mapa de configuraciones.
     * @param configurationType Tipo de configuración.
     * @param charClass         Clase de caracteres para la restricción.
     */
    private static void addCharacterRestriction(StringBuilder regexBuilder,
                                                Map<PasswordConfigurationType, String> configMap,
                                                PasswordConfigurationType configurationType,
                                                String charClass) {
        int minCount = getIntConfig(configMap, configurationType, 0);
        if (minCount > 0) {
            regexBuilder.append("(?=(?:.*").append(charClass).append("){").append(minCount).append(",})");
        }
    }

    /**
     * Validar y ajusta la expresión regular si es necesario.
     *
     * @param regex     Expresión regular a validar.
     * @param configMap Mapa de configuraciones.
     * @param minLength Longitud mínima.
     * @param maxLength Longitud máxima.
     * @return La expresión regular validada y ajustada.
     * @throws IllegalArgumentException Si no se puede compilar la expresión regular.
     */
    private static String validateAndAdjustRegex(String regex, Map<PasswordConfigurationType, String> configMap,
                                                 int minLength, int maxLength) {
        try {
            Pattern pattern = Pattern.compile(regex);
            logger.info("Expresión regular generada: {}", regex);

            // Ajustar la expresión regular si es necesario para manejar correctamente la longitud máxima
            if (configMap.containsKey(PasswordConfigurationType.MIN_LENGTH) &&
                    configMap.containsKey(PasswordConfigurationType.MAX_LENGTH)) {

                String tooLongPassword = "a".repeat(maxLength + 1);
                if (pattern.matcher(tooLongPassword).matches()) {
                    logger.warn("La expresión regular permite contraseñas demasiado largas");
                    regex = "^(?=.*" + minLength + "," + maxLength + "})(?!.*" + (maxLength + 1) + ",})" +
                            regex.substring(regex.indexOf("}") + 1);
                    logger.info("Expresión regular ajustada: {}", regex);
                }
            }

            return regex;
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("No se pudo generar una expresión regular válida", e);
        }
    }

    /**
     * Obtiene un valor de configuración como entero.
     *
     * @param configMap    Mapa de configuraciones.
     * @param type         Tipo de configuración.
     * @param defaultValue Valor por defecto si la configuración no existe o no es válida.
     * @return El valor de la configuración como entero.
     */
    private static int getIntConfig(Map<PasswordConfigurationType, String> configMap,
                                    PasswordConfigurationType type,
                                    int defaultValue) {
        String value = configMap.get(type);
        if (value == null) {
            logger.debug("Configuración no encontrada para {}, usando valor por defecto: {}",
                    type, defaultValue);
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException _) {
            logger.warn("Valor no numérico para {}: {}, usando valor por defecto: {}",
                    type, value, defaultValue);
            return defaultValue;
        }
    }
}
