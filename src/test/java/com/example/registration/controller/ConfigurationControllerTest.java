package com.example.registration.controller;

import com.example.registration.dto.ConfigurationRequestDTO;
import com.example.registration.dto.ConfigurationResponseDTO;
import com.example.registration.dto.ConfigurationTypeResponseDTO;
import com.example.registration.exception.GlobalExceptionHandler;
import com.example.registration.mapper.ConfigurationMapper;
import com.example.registration.model.Configuration;
import com.example.registration.model.ConfigurationType;
import com.example.registration.repository.ConfigurationTypeRepository;
import com.example.registration.service.ConfigurationService;
import com.example.registration.service.PasswordValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ConfigurationControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private ConfigurationMapper configurationMapper;

    @Mock
    private PasswordValidationService passwordValidationService;

    @Mock
    private ConfigurationTypeRepository configurationTypeRepository;

    @InjectMocks
    private ConfigurationController configurationController;

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    private Configuration configuration1;
    private Configuration configuration2;
    private ConfigurationType configurationType1;
    private ConfigurationResponseDTO configResponseDTO1;
    private ConfigurationResponseDTO configResponseDTO2;
    private ConfigurationRequestDTO configRequestDTO;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(configurationController)
                .setControllerAdvice(globalExceptionHandler)
                .build();

        LocalDateTime now = LocalDateTime.now();

        // Create configuration types
        configurationType1 = new ConfigurationType();
        configurationType1.setId(1L);
        configurationType1.setTypeKey("password.min.length");
        configurationType1.setDescription("Minimum password length");
        configurationType1.setActive(true);
        configurationType1.setCreatedAt(now);
        configurationType1.setUpdatedAt(now);

        ConfigurationType configurationType2 = new ConfigurationType();
        configurationType2.setId(2L);
        configurationType2.setTypeKey("password.max.length");
        configurationType2.setDescription("Maximum password length");
        configurationType2.setActive(true);
        configurationType2.setCreatedAt(now);
        configurationType2.setUpdatedAt(now);

        // Create configurations
        configuration1 = new Configuration();
        configuration1.setId(1L);
        configuration1.setConfigurationType(configurationType1);
        configuration1.setConfigValue("8");
        configuration1.setActive(true);
        configuration1.setCreatedAt(now);
        configuration1.setUpdatedAt(now);

        configuration2 = new Configuration();
        configuration2.setId(2L);
        configuration2.setConfigurationType(configurationType2);
        configuration2.setConfigValue("20");
        configuration2.setActive(true);
        configuration2.setCreatedAt(now);
        configuration2.setUpdatedAt(now);

        // Create configuration type response DTOs
        ConfigurationTypeResponseDTO configTypeResponseDTO1 = new ConfigurationTypeResponseDTO();
        configTypeResponseDTO1.setId(1L);
        configTypeResponseDTO1.setTypeKey("password.min.length");
        configTypeResponseDTO1.setActive(true);

        ConfigurationTypeResponseDTO configTypeResponseDTO2 = new ConfigurationTypeResponseDTO();
        configTypeResponseDTO2.setId(2L);
        configTypeResponseDTO2.setTypeKey("password.max.length");
        configTypeResponseDTO2.setActive(true);

        // Create configuration response DTOs
        configResponseDTO1 = new ConfigurationResponseDTO();
        configResponseDTO1.setId(1L);
        configResponseDTO1.setConfigurationType(configTypeResponseDTO1);
        configResponseDTO1.setConfigValue("8");
        configResponseDTO1.setActive(true);
        configResponseDTO1.setCreatedAt(now);
        configResponseDTO1.setUpdatedAt(now);

        configResponseDTO2 = new ConfigurationResponseDTO();
        configResponseDTO2.setId(2L);
        configResponseDTO2.setConfigurationType(configTypeResponseDTO2);
        configResponseDTO2.setConfigValue("20");
        configResponseDTO2.setActive(true);
        configResponseDTO2.setCreatedAt(now);
        configResponseDTO2.setUpdatedAt(now);

        // Create configuration request DTO
        configRequestDTO = new ConfigurationRequestDTO();
        configRequestDTO.setConfigurationTypeId(1L);
        configRequestDTO.setConfigValue("10");
    }

    @Test
    void getAllConfigurations_shouldReturnAllConfigurations() throws Exception {
        // Arrange
        List<Configuration> configurations = Arrays.asList(configuration1, configuration2);

        when(configurationService.getAllActiveConfigurations()).thenReturn(configurations);
        when(configurationMapper.configurationToConfigurationResponseDTO(configuration1)).thenReturn(configResponseDTO1);
        when(configurationMapper.configurationToConfigurationResponseDTO(configuration2)).thenReturn(configResponseDTO2);

        // Act & Assert
        mockMvc.perform(get("/api/configurations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].configValue", is("8")))
                .andExpect(jsonPath("$[0].configurationType.typeKey", is("password.min.length")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].configValue", is("20")))
                .andExpect(jsonPath("$[1].configurationType.typeKey", is("password.max.length")));

        // Verify interactions
        verify(configurationService).getAllActiveConfigurations();
        verify(configurationMapper, times(2)).configurationToConfigurationResponseDTO(any(Configuration.class));
    }

    @Test
    void getConfigurationByType_whenTypeExists_shouldReturnConfiguration() throws Exception {
        // Arrange
        String typeKey = "password.min.length";
        when(configurationService.getConfigurationByType(typeKey)).thenReturn(Optional.of(configuration1));
        when(configurationMapper.configurationToConfigurationResponseDTO(configuration1)).thenReturn(configResponseDTO1);

        // Act & Assert
        mockMvc.perform(get("/api/configurations/{typeKey}", typeKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.configValue", is("8")))
                .andExpect(jsonPath("$.configurationType.typeKey", is("password.min.length")));

        // Verify interactions
        verify(configurationService).getConfigurationByType(typeKey);
        verify(configurationMapper).configurationToConfigurationResponseDTO(configuration1);
    }

    @Test
    void getConfigurationByType_whenTypeDoesNotExist_shouldReturnNotFound() throws Exception {
        // Arrange
        String typeKey = "non.existent.type";
        when(configurationService.getConfigurationByType(typeKey)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/configurations/{typeKey}", typeKey))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje", containsString(typeKey)));

        // Verify interactions
        verify(configurationService).getConfigurationByType(typeKey);
        verify(configurationMapper, never()).configurationToConfigurationResponseDTO(any(Configuration.class));
    }

    @Test
    void updateConfiguration_withValidRequest_shouldUpdateConfiguration() throws Exception {
        // Arrange
        when(configurationTypeRepository.findById(1L)).thenReturn(Optional.of(configurationType1));
        when(configurationService.updateConfiguration(configurationType1.getTypeKey(), "10")).thenReturn(configuration1);
        when(configurationMapper.configurationToConfigurationResponseDTO(configuration1)).thenReturn(configResponseDTO1);

        // Act & Assert
        mockMvc.perform(put("/api/configurations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.configValue", is("8")))
                .andExpect(jsonPath("$.configurationType.typeKey", is("password.min.length")));

        // Verify interactions
        verify(configurationTypeRepository).findById(1L);
        verify(configurationService).updateConfiguration(configurationType1.getTypeKey(), "10");
        verify(passwordValidationService).initializeValidator();
        verify(configurationMapper).configurationToConfigurationResponseDTO(configuration1);
    }

    @Test
    void updateConfiguration_withNonExistentType_shouldReturnNotFound() throws Exception {
        // Arrange
        when(configurationTypeRepository.findById(999L)).thenReturn(Optional.empty());

        ConfigurationRequestDTO invalidRequest = new ConfigurationRequestDTO();
        invalidRequest.setConfigurationTypeId(999L);
        invalidRequest.setConfigValue("10");

        // Act & Assert
        mockMvc.perform(put("/api/configurations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje", containsString("999")));

        // Verify interactions
        verify(configurationTypeRepository).findById(999L);
        verify(configurationService, never()).updateConfiguration(anyString(), anyString());
        verify(passwordValidationService, never()).initializeValidator();
    }

    @Test
    void updateConfiguration_withInvalidRequest_shouldReturnBadRequest() throws Exception {
        // Arrange
        ConfigurationRequestDTO invalidRequest = new ConfigurationRequestDTO();
        // Missing required fields

        // Act & Assert
        mockMvc.perform(put("/api/configurations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());

        // Verify no interactions with services
        verify(configurationTypeRepository, never()).findById(any());
        verify(configurationService, never()).updateConfiguration(anyString(), anyString());
        verify(passwordValidationService, never()).initializeValidator();
    }

    @Test
    void updateConfigurationByType_withValidRequest_shouldUpdateConfiguration() throws Exception {
        // Arrange
        String typeKey = "password.min.length";
        String value = "10";

        when(configurationService.updateConfiguration(typeKey, value)).thenReturn(configuration1);
        when(configurationMapper.configurationToConfigurationResponseDTO(configuration1)).thenReturn(configResponseDTO1);

        // Act & Assert
        mockMvc.perform(put("/api/configurations/{typeKey}", typeKey)
                        .param("value", value))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.configValue", is("8")))
                .andExpect(jsonPath("$.configurationType.typeKey", is("password.min.length")));

        // Verify interactions
        verify(configurationService).updateConfiguration(typeKey, value);
        verify(passwordValidationService).initializeValidator();
        verify(configurationMapper).configurationToConfigurationResponseDTO(configuration1);
    }
}