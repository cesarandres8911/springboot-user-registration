package com.example.registration.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(ConfigurationControllerSecurityTest.TestConfig.class)
class ConfigurationControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Configuration
    static class TestConfig {
        @Bean
        public AuthenticationManager authenticationManager() {
            return Mockito.mock(AuthenticationManager.class);
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            return Mockito.mock(AuthenticationProvider.class);
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return Mockito.mock(UserDetailsService.class);
        }
    }

    @Test
    void getAllConfigurations_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/configurations"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getConfigurationByType_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/configurations/PASSWORD_MIN_LENGTH"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateConfiguration_withoutAuthentication_shouldReturnForbidden() throws Exception {
        mockMvc.perform(put("/api/configurations")
                        .contentType("application/json")
                        .content("{\"configurationTypeId\": 1, \"configValue\": \"8\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateConfigurationByType_withoutAuthentication_shouldReturnForbidden() throws Exception {
        mockMvc.perform(put("/api/configurations/PASSWORD_MIN_LENGTH")
                        .param("value", "8"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllConfigurations_withInvalidJwtToken_shouldReturnUnauthorized() throws Exception {
        // Invalid JWT token (invalid signature)
        String invalidToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        mockMvc.perform(get("/api/configurations")
                        .header("Authorization", invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
