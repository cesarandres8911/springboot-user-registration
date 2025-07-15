package com.example.registration.service;

import com.example.registration.model.Configuration;
import com.example.registration.model.ConfigurationType;
import com.example.registration.repository.ConfigurationRepository;
import com.example.registration.repository.ConfigurationTypeRepository;
import com.example.registration.utils.PasswordConfigurationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de configuraciones.
 * Proporciona métodos para obtener y actualizar configuraciones del sistema.
 */
@Service
public class ConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationTypeRepository configurationTypeRepository;

    public ConfigurationService(ConfigurationRepository configurationRepository, 
                               ConfigurationTypeRepository configurationTypeRepository) {
        this.configurationRepository = configurationRepository;
        this.configurationTypeRepository = configurationTypeRepository;
    }

    /**
     * Obtiene todas las configuraciones activas.
     *
     * @return Lista de configuraciones activas
     */
    public List<Configuration> getAllActiveConfigurations() {
        return configurationRepository.findByActiveTrue();
    }

    /**
     * Obtiene todas las configuraciones de contraseña activas.
     *
     * @return Lista de configuraciones de contraseña activas
     */
    public List<Configuration> getPasswordConfigurations() {
        List<Configuration> allConfigs = configurationRepository.findByActiveTrue();
        return allConfigs.stream()
                .filter(config -> {
                    String typeKey = config.getConfigurationType().getTypeKey();
                    return PasswordConfigurationType.fromTypeKey(typeKey) != null;
                })
                .toList();
    }

    /**
     * Obtiene una configuración por su tipo.
     *
     * @param typeKey Clave del tipo de configuración
     * @return Configuración encontrada o null si no existe
     */
    public Optional<Configuration> getConfigurationByType(String typeKey) {
        Optional<ConfigurationType> configurationType = configurationTypeRepository.findByTypeKey(typeKey);
        if (configurationType.isEmpty()) {
            logger.warn("Tipo de configuración no encontrado: {}", typeKey);
            return Optional.empty();
        }

        List<Configuration> configurations = configurationRepository
                .findByConfigurationTypeAndActiveTrue(configurationType.get());

        return configurations.isEmpty() ? Optional.empty() : Optional.of(configurations.get(0));
    }

    /**
     * Actualiza o crea una configuración.
     *
     * @param typeKey Clave del tipo de configuración
     * @param value Valor de la configuración
     * @return Configuración actualizada o creada
     */
    @Transactional
    public Configuration updateConfiguration(String typeKey, String value) {
        // Buscar o crear el tipo de configuración
        ConfigurationType configurationType = configurationTypeRepository.findByTypeKey(typeKey)
                .orElseGet(() -> createConfigurationType(typeKey));

        // Buscar configuraciones activas de este tipo
        List<Configuration> existingConfigs = configurationRepository
                .findByConfigurationTypeAndActiveTrue(configurationType);

        // Si existe una configuración activa, actualizarla
        if (!existingConfigs.isEmpty()) {
            Configuration config = existingConfigs.get(0);
            config.setConfigValue(value);
            config.setUpdatedAt(LocalDateTime.now());
            return configurationRepository.save(config);
        }

        // Si no existe, crear una nueva
        return createConfiguration(configurationType, value);
    }

    /**
     * Crea un nuevo tipo de configuración.
     *
     * @param typeKey Clave del tipo de configuración
     * @return Tipo de configuración creado
     */
    private ConfigurationType createConfigurationType(String typeKey) {
        LocalDateTime now = LocalDateTime.now();
        ConfigurationType configurationType = ConfigurationType.builder()
                .typeKey(typeKey)
                .description("Tipo de configuración para " + typeKey)
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return configurationTypeRepository.save(configurationType);
    }

    /**
     * Crea una nueva configuración.
     *
     * @param configurationType Tipo de configuración
     * @param value Valor de la configuración
     * @return Configuración creada
     */
    private Configuration createConfiguration(ConfigurationType configurationType, String value) {
        LocalDateTime now = LocalDateTime.now();
        Configuration configuration = Configuration.builder()
                .configurationType(configurationType)
                .configValue(value)
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return configurationRepository.save(configuration);
    }
}
