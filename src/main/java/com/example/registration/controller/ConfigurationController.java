package com.example.registration.controller;

import com.example.registration.dto.ConfigurationRequestDTO;
import com.example.registration.dto.ConfigurationResponseDTO;
import com.example.registration.mapper.ConfigurationMapper;
import com.example.registration.model.Configuration;
import com.example.registration.model.ConfigurationType;
import com.example.registration.repository.ConfigurationTypeRepository;
import com.example.registration.service.ConfigurationService;
import com.example.registration.service.PasswordValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para la gestión de configuraciones.
 * Proporciona endpoints para obtener y actualizar configuraciones del sistema.
 */
@RestController
@RequestMapping("/api/configurations")
@Tag(name = "Configurations", description = "API para la gestión de configuraciones del sistema")
public class ConfigurationController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

    private final ConfigurationService configurationService;
    private final ConfigurationMapper configurationMapper;
    private final PasswordValidationService passwordValidationService;
    private final ConfigurationTypeRepository configurationTypeRepository;

    public ConfigurationController(ConfigurationService configurationService,
                                   ConfigurationMapper configurationMapper,
                                   PasswordValidationService passwordValidationService,
                                   ConfigurationTypeRepository configurationTypeRepository) {
        this.configurationService = configurationService;
        this.configurationMapper = configurationMapper;
        this.passwordValidationService = passwordValidationService;
        this.configurationTypeRepository = configurationTypeRepository;
    }

    /**
     * Endpoint para obtener todas las configuraciones activas.
     *
     * @return ResponseEntity con la lista de configuraciones
     */
    @GetMapping
    @Operation(summary = "Obtener todas las configuraciones",
            description = "Obtiene todas las configuraciones activas del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuraciones obtenidas exitosamente",
                    content = @Content(schema = @Schema(implementation = ConfigurationResponseDTO.class)))
    })
    public ResponseEntity<List<ConfigurationResponseDTO>> getAllConfigurations() {
        List<Configuration> configurations = configurationService.getAllActiveConfigurations();
        List<ConfigurationResponseDTO> responseList = configurations.stream()
                .map(configurationMapper::configurationToConfigurationResponseDTO)
                .toList();

        return ResponseEntity.ok(responseList);
    }

    /**
     * Endpoint para obtener una configuración por su tipo.
     *
     * @param typeKey Clave del tipo de configuración
     * @return ResponseEntity con la configuración o un mensaje de error
     */
    @GetMapping("/{typeKey}")
    @Operation(summary = "Obtener configuración por tipo",
            description = "Obtiene una configuración específica por su tipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuración obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = ConfigurationResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Configuración no encontrada",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Object> getConfigurationByType(@PathVariable String typeKey) {
        Optional<Configuration> configOpt = configurationService.getConfigurationByType(typeKey);

        if (configOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Configuración no encontrada para el tipo: " + typeKey));
        }

        ConfigurationResponseDTO responseDTO = configurationMapper
                .configurationToConfigurationResponseDTO(configOpt.get());

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Endpoint para actualizar una configuración.
     *
     * @param configurationRequestDTO DTO con la información de la configuración a actualizar
     * @return ResponseEntity con la configuración actualizada o un mensaje de error
     */
    @PutMapping
    @Operation(summary = "Actualizar configuración",
            description = "Actualiza una configuración existente o crea una nueva si no existe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuración actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = ConfigurationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de configuración inválidos",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Object> updateConfiguration(
            @Valid @RequestBody ConfigurationRequestDTO configurationRequestDTO) {

        logger.info("Actualizando configuración: {}", configurationRequestDTO);

        // Obtener el tipo de configuración
        Optional<ConfigurationType> configTypeOpt = configurationTypeRepository.findById(
                configurationRequestDTO.getConfigurationTypeId());

        if (configTypeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Tipo de configuración no encontrado con ID: " +
                            configurationRequestDTO.getConfigurationTypeId()));
        }

        // Actualizar la configuración
        Configuration updatedConfig = configurationService.updateConfiguration(
                configTypeOpt.get().getTypeKey(),
                configurationRequestDTO.getConfigValue());

        // Reinicializar el validador de contraseñas para aplicar los cambios
        passwordValidationService.initializeValidator();

        // Convertir entidad a DTO de respuesta
        ConfigurationResponseDTO responseDTO = configurationMapper
                .configurationToConfigurationResponseDTO(updatedConfig);

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Endpoint para actualizar una configuración por su tipo.
     *
     * @param typeKey Clave del tipo de configuración
     * @param value   Nuevo valor de la configuración
     * @return ResponseEntity con la configuración actualizada o un mensaje de error
     */
    @PutMapping("/{typeKey}")
    @Operation(summary = "Actualizar configuración por tipo",
            description = "Actualiza una configuración existente por su tipo o crea una nueva si no existe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuración actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = ConfigurationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de configuración inválidos",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<ConfigurationResponseDTO> updateConfigurationByType(
            @PathVariable String typeKey,
            @RequestParam(value = "value") String value) {

        logger.info("Actualizando configuración de tipo {} con valor {}", typeKey, value);

        // Actualizar la configuración
        Configuration updatedConfig = configurationService.updateConfiguration(typeKey, value);

        // Reinicializar el validador de contraseñas para aplicar los cambios
        passwordValidationService.initializeValidator();

        // Convertir entidad a DTO de respuesta
        ConfigurationResponseDTO responseDTO = configurationMapper
                .configurationToConfigurationResponseDTO(updatedConfig);

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Endpoint alternativo para actualizar una configuración por su tipo.
     * Acepta el valor como parte del cuerpo de la solicitud en lugar de como parámetro de consulta,
     * lo que facilita el envío de caracteres especiales sin problemas de codificación URL.
     *
     * @param typeKey Clave del tipo de configuración
     * @param requestBody Mapa que contiene el valor de la configuración
     * @return ResponseEntity con la configuración actualizada
     */
    @PutMapping("/{typeKey}/value")
    @Operation(summary = "Actualizar configuración por tipo (alternativo)",
            description = "Actualiza una configuración existente por su tipo usando el cuerpo de la solicitud para el valor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuración actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = ConfigurationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de configuración inválidos",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<ConfigurationResponseDTO> updateConfigurationByTypeWithBody(
            @PathVariable String typeKey,
            @RequestBody Map<String, String> requestBody) {

        String value = requestBody.get("value");
        if (value == null) {
            throw new IllegalArgumentException("El parámetro 'value' es requerido en el cuerpo de la solicitud");
        }

        logger.info("Actualizando configuración de tipo {} con valor {} (usando cuerpo de solicitud)", typeKey, value);

        // Actualizar la configuración
        Configuration updatedConfig = configurationService.updateConfiguration(typeKey, value);

        // Reinicializar el validador de contraseñas para aplicar los cambios
        passwordValidationService.initializeValidator();

        // Convertir entidad a DTO de respuesta
        ConfigurationResponseDTO responseDTO = configurationMapper
                .configurationToConfigurationResponseDTO(updatedConfig);

        return ResponseEntity.ok(responseDTO);
    }
}
