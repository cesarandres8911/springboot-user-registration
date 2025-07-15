package com.example.registration.controller;

import com.example.registration.dto.LoginRequestDTO;
import com.example.registration.dto.UserResponseDTO;
import com.example.registration.exception.GlobalExceptionHandler;
import com.example.registration.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    private LoginRequestDTO validLoginRequest;
    private UserResponseDTO userResponseDTO;
    private UUID userId;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
                
        userId = UUID.randomUUID();
        
        // Create valid login request
        validLoginRequest = LoginRequestDTO.builder()
                .email("test@example.com")
                .password("ValidPassword1#")
                .build();
        
        // Create user response
        LocalDateTime now = LocalDateTime.now();
        userResponseDTO = UserResponseDTO.builder()
                .id(userId)
                .created(now)
                .modified(now)
                .last_login(now)
                .token("test-jwt-token")
                .isactive(true)
                .build();
    }

    @Test
    void login_withValidCredentials_shouldReturnUserResponse() throws Exception {
        // Arrange
        when(userService.loginUser(validLoginRequest.getEmail(), validLoginRequest.getPassword()))
                .thenReturn(userResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.toString())))
                .andExpect(jsonPath("$.token", is(userResponseDTO.getToken())))
                .andExpect(jsonPath("$.isactive", is(userResponseDTO.isIsactive())));
    }

    @Test
    void login_withInvalidCredentials_shouldReturnUnauthorized() throws Exception {
        // Arrange
        when(userService.loginUser(validLoginRequest.getEmail(), validLoginRequest.getPassword()))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje", is("Credenciales inválidas")));
    }

    @Test
    void login_withNonExistentUser_shouldReturnNotFound() throws Exception {
        // Arrange
        when(userService.loginUser(validLoginRequest.getEmail(), validLoginRequest.getPassword()))
                .thenThrow(new UsernameNotFoundException("Usuario no encontrado: " + validLoginRequest.getEmail()));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void login_withMissingEmail_shouldReturnBadRequest() throws Exception {
        // Arrange
        LoginRequestDTO invalidRequest = LoginRequestDTO.builder()
                .password("ValidPassword1#")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void login_withInvalidEmailFormat_shouldReturnBadRequest() throws Exception {
        // Arrange
        LoginRequestDTO invalidRequest = LoginRequestDTO.builder()
                .email("invalid-email")
                .password("ValidPassword1#")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void login_withMissingPassword_shouldReturnBadRequest() throws Exception {
        // Arrange
        LoginRequestDTO invalidRequest = LoginRequestDTO.builder()
                .email("test@example.com")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }
}