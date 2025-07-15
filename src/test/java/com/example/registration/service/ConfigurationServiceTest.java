package com.example.registration.service;

import com.example.registration.model.Configuration;
import com.example.registration.model.ConfigurationType;
import com.example.registration.repository.ConfigurationRepository;
import com.example.registration.repository.ConfigurationTypeRepository;
import com.example.registration.utils.PasswordConfigurationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    @Mock
    private ConfigurationTypeRepository configurationTypeRepository;

    @InjectMocks
    private ConfigurationService configurationService;

    private ConfigurationType passwordMinLengthType;
    private Configuration passwordMinLengthConfig;
    private Configuration otherConfig;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        // Create configuration types
        passwordMinLengthType = ConfigurationType.builder()
                .id(1L)
                .typeKey(PasswordConfigurationType.MIN_LENGTH.getTypeKey())
                .description("Minimum password length")
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        ConfigurationType otherConfigType = ConfigurationType.builder()
                .id(2L)
                .typeKey("other.config.type")
                .description("Other configuration type")
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Create configurations
        passwordMinLengthConfig = Configuration.builder()
                .id(1L)
                .configurationType(passwordMinLengthType)
                .configValue("8")
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        otherConfig = Configuration.builder()
                .id(2L)
                .configurationType(otherConfigType)
                .configValue("some value")
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void getAllActiveConfigurations_shouldReturnAllActiveConfigurations() {
        // Arrange
        List<Configuration> expectedConfigurations = Arrays.asList(passwordMinLengthConfig, otherConfig);
        when(configurationRepository.findByIsActiveTrue()).thenReturn(expectedConfigurations);

        // Act
        List<Configuration> actualConfigurations = configurationService.getAllActiveConfigurations();

        // Assert
        assertEquals(expectedConfigurations.size(), actualConfigurations.size());
        assertEquals(expectedConfigurations, actualConfigurations);
        verify(configurationRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    void getPasswordConfigurations_shouldReturnOnlyPasswordConfigurations() {
        // Arrange
        List<Configuration> allConfigurations = Arrays.asList(passwordMinLengthConfig, otherConfig);
        when(configurationRepository.findByIsActiveTrue()).thenReturn(allConfigurations);

        // Act
        List<Configuration> passwordConfigurations = configurationService.getPasswordConfigurations();

        // Assert
        assertEquals(1, passwordConfigurations.size());
        assertEquals(passwordMinLengthConfig, passwordConfigurations.get(0));
        verify(configurationRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    void getConfigurationByType_whenTypeExists_shouldReturnConfiguration() {
        // Arrange
        String typeKey = passwordMinLengthType.getTypeKey();
        when(configurationTypeRepository.findByTypeKey(typeKey)).thenReturn(Optional.of(passwordMinLengthType));
        when(configurationRepository.findByConfigurationTypeAndIsActiveTrue(passwordMinLengthType))
                .thenReturn(Collections.singletonList(passwordMinLengthConfig));

        // Act
        Optional<Configuration> result = configurationService.getConfigurationByType(typeKey);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(passwordMinLengthConfig, result.get());
        verify(configurationTypeRepository, times(1)).findByTypeKey(typeKey);
        verify(configurationRepository, times(1)).findByConfigurationTypeAndIsActiveTrue(passwordMinLengthType);
    }

    @Test
    void getConfigurationByType_whenTypeDoesNotExist_shouldReturnEmpty() {
        // Arrange
        String typeKey = "non.existent.type";
        when(configurationTypeRepository.findByTypeKey(typeKey)).thenReturn(Optional.empty());

        // Act
        Optional<Configuration> result = configurationService.getConfigurationByType(typeKey);

        // Assert
        assertFalse(result.isPresent());
        verify(configurationTypeRepository, times(1)).findByTypeKey(typeKey);
        verify(configurationRepository, never()).findByConfigurationTypeAndIsActiveTrue(any());
    }

    @Test
    void getConfigurationByType_whenTypeExistsButNoConfiguration_shouldReturnEmpty() {
        // Arrange
        String typeKey = passwordMinLengthType.getTypeKey();
        when(configurationTypeRepository.findByTypeKey(typeKey)).thenReturn(Optional.of(passwordMinLengthType));
        when(configurationRepository.findByConfigurationTypeAndIsActiveTrue(passwordMinLengthType))
                .thenReturn(Collections.emptyList());

        // Act
        Optional<Configuration> result = configurationService.getConfigurationByType(typeKey);

        // Assert
        assertFalse(result.isPresent());
        verify(configurationTypeRepository, times(1)).findByTypeKey(typeKey);
        verify(configurationRepository, times(1)).findByConfigurationTypeAndIsActiveTrue(passwordMinLengthType);
    }

    @Test
    void updateConfiguration_whenConfigurationExists_shouldUpdateIt() {
        // Arrange
        String typeKey = passwordMinLengthType.getTypeKey();
        String newValue = "10";

        when(configurationTypeRepository.findByTypeKey(typeKey)).thenReturn(Optional.of(passwordMinLengthType));
        when(configurationRepository.findByConfigurationTypeAndIsActiveTrue(passwordMinLengthType))
                .thenReturn(Collections.singletonList(passwordMinLengthConfig));

        // Use a captor to capture the saved configuration
        ArgumentCaptor<Configuration> configCaptor = ArgumentCaptor.forClass(Configuration.class);

        // Mock the save method to return the captured configuration
        when(configurationRepository.save(configCaptor.capture())).thenAnswer(invocation -> {
            Configuration savedConfig = configCaptor.getValue();
            return Configuration.builder()
                    .id(savedConfig.getId())
                    .configurationType(savedConfig.getConfigurationType())
                    .configValue(savedConfig.getConfigValue())
                    .isActive(savedConfig.isActive())
                    .createdAt(savedConfig.getCreatedAt())
                    .updatedAt(savedConfig.getUpdatedAt())
                    .build();
        });

        // Act
        Configuration result = configurationService.updateConfiguration(typeKey, newValue);

        // Assert
        assertEquals(newValue, result.getConfigValue());
        verify(configurationTypeRepository, times(1)).findByTypeKey(typeKey);
        verify(configurationRepository, times(1)).findByConfigurationTypeAndIsActiveTrue(passwordMinLengthType);
        verify(configurationRepository, times(1)).save(any(Configuration.class));
    }

    @Test
    void updateConfiguration_whenConfigurationDoesNotExistButTypeExists_shouldCreateNewConfiguration() {
        // Arrange
        String typeKey = passwordMinLengthType.getTypeKey();
        String value = "10";

        when(configurationTypeRepository.findByTypeKey(typeKey)).thenReturn(Optional.of(passwordMinLengthType));
        when(configurationRepository.findByConfigurationTypeAndIsActiveTrue(passwordMinLengthType))
                .thenReturn(Collections.emptyList());

        // Use a captor to capture the saved configuration
        ArgumentCaptor<Configuration> configCaptor = ArgumentCaptor.forClass(Configuration.class);

        // Mock the save method to return a new configuration with the captured values
        when(configurationRepository.save(configCaptor.capture())).thenAnswer(invocation -> {
            Configuration savedConfig = configCaptor.getValue();
            return Configuration.builder()
                    .id(3L)
                    .configurationType(savedConfig.getConfigurationType())
                    .configValue(savedConfig.getConfigValue())
                    .isActive(savedConfig.isActive())
                    .createdAt(savedConfig.getCreatedAt())
                    .updatedAt(savedConfig.getUpdatedAt())
                    .build();
        });

        // Act
        Configuration result = configurationService.updateConfiguration(typeKey, value);

        // Assert
        assertEquals(value, result.getConfigValue());
        assertEquals(passwordMinLengthType, result.getConfigurationType());
        verify(configurationTypeRepository, times(1)).findByTypeKey(typeKey);
        verify(configurationRepository, times(1)).findByConfigurationTypeAndIsActiveTrue(passwordMinLengthType);
        verify(configurationRepository, times(1)).save(any(Configuration.class));
    }

    @Test
    void updateConfiguration_whenTypeDoesNotExist_shouldCreateTypeAndConfiguration() {
        // Arrange
        String typeKey = "new.config.type";
        String value = "new value";

        when(configurationTypeRepository.findByTypeKey(typeKey)).thenReturn(Optional.empty());

        // Use captors to capture the saved objects
        ArgumentCaptor<ConfigurationType> typeCaptor = ArgumentCaptor.forClass(ConfigurationType.class);
        ArgumentCaptor<Configuration> configCaptor = ArgumentCaptor.forClass(Configuration.class);

        // Create a new type to be returned when saving
        ConfigurationType newType = ConfigurationType.builder()
                .id(3L)
                .typeKey(typeKey)
                .description("Tipo de configuración para " + typeKey)
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Mock the save method for ConfigurationType
        when(configurationTypeRepository.save(typeCaptor.capture())).thenReturn(newType);

        // Mock the save method for Configuration
        when(configurationRepository.save(configCaptor.capture())).thenAnswer(invocation -> {
            Configuration savedConfig = configCaptor.getValue();
            return Configuration.builder()
                    .id(3L)
                    .configurationType(newType)
                    .configValue(savedConfig.getConfigValue())
                    .isActive(savedConfig.isActive())
                    .createdAt(savedConfig.getCreatedAt())
                    .updatedAt(savedConfig.getUpdatedAt())
                    .build();
        });

        // Mock the findByConfigurationTypeAndIsActiveTrue method
        when(configurationRepository.findByConfigurationTypeAndIsActiveTrue(any(ConfigurationType.class)))
                .thenReturn(Collections.emptyList());

        // Act
        Configuration result = configurationService.updateConfiguration(typeKey, value);

        // Assert
        assertEquals(value, result.getConfigValue());
        assertEquals(typeKey, result.getConfigurationType().getTypeKey());
        verify(configurationTypeRepository, times(1)).findByTypeKey(typeKey);
        verify(configurationTypeRepository, times(1)).save(any(ConfigurationType.class));
        verify(configurationRepository, times(1)).findByConfigurationTypeAndIsActiveTrue(any(ConfigurationType.class));
        verify(configurationRepository, times(1)).save(any(Configuration.class));
    }
}
