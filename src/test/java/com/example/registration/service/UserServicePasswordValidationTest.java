package com.example.registration.service;

import com.example.registration.dto.PhoneRequestDTO;
import com.example.registration.dto.UserRequestDTO;
import com.example.registration.exception.InvalidPasswordException;
import com.example.registration.mapper.PhoneMapper;
import com.example.registration.mapper.UserMapper;
import com.example.registration.model.Phone;
import com.example.registration.model.User;
import com.example.registration.repository.UserRepository;
import com.example.registration.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la validación de contraseñas en el servicio de usuarios.
 */
class UserServicePasswordValidationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PhoneMapper phoneMapper;

    @Mock
    private PasswordValidationService passwordValidationService;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO validUserRequest;

    /**
     * Configura las pruebas con datos predeterminados.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Configurar un usuario válido para las pruebas
        validUserRequest = new UserRequestDTO();
        validUserRequest.setName("Test User");
        validUserRequest.setEmail("test@example.com");
        validUserRequest.setPassword("ValidPassword1#");

        List<PhoneRequestDTO> phones = new ArrayList<>();
        PhoneRequestDTO phone = new PhoneRequestDTO();
        phone.setNumber("1234567890");
        phone.setCitycode("1");
        phone.setContrycode("57");
        phones.add(phone);
        validUserRequest.setPhones(phones);

        // Configurar mocks
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(anyString())).thenReturn("jwt-token");

        User mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setFullName(validUserRequest.getName());
        mockUser.setUserEmail(validUserRequest.getEmail());

        when(userMapper.userRequestDTOToUser(any(UserRequestDTO.class))).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Mock PhoneMapper to return a Phone object
        when(phoneMapper.phoneRequestDTOToPhone(any(PhoneRequestDTO.class))).thenAnswer(invocation -> {
            PhoneRequestDTO phoneDTO = invocation.getArgument(0);
            Phone mockPhone = new Phone();
            mockPhone.setPhoneNumber(phoneDTO.getNumber());
            mockPhone.setCityCode(phoneDTO.getCitycode());
            mockPhone.setCountryCode(phoneDTO.getContrycode());
            return mockPhone;
        });
    }

    /**
     * Prueba el registro de un usuario con una contraseña válida.
     */
    @Test
    void testRegisterUserWithValidPassword() {
        // Configurar el validador para aceptar la contraseña
        when(passwordValidationService.isPasswordValid(validUserRequest.getPassword())).thenReturn(true);

        // Ejecutar la función a probar
        userService.registerUser(validUserRequest);

        // Verificar que se llamó la función isPasswordValid
        verify(passwordValidationService, times(1)).isPasswordValid(validUserRequest.getPassword());

        // Verificar que se guardó el usuario
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Prueba el registro de un usuario con una contraseña inválida.
     */
    @Test
    void testRegisterUserWithInvalidPassword() {
        // Configurar el validador para rechazar la contraseña
        when(passwordValidationService.isPasswordValid(validUserRequest.getPassword())).thenReturn(false);
        when(passwordValidationService.getPasswordRegexPattern()).thenReturn("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#$%&])");

        // Ejecutar la función a probar y verificar que lanza la excepción esperada
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> userService.registerUser(validUserRequest));

        // Verificar el mensaje de la excepción
        assertTrue(exception.getMessage().contains("La contraseña no cumple con el patrón requerido"));

        // Verificar que se llamó la función isPasswordValid
        verify(passwordValidationService, times(1)).isPasswordValid(validUserRequest.getPassword());

        // Verificar que NO se guardó el usuario
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Prueba el registro de un usuario con un correo ya existente.
     */
    @Test
    void testRegisterUserWithExistingEmail() {
        // Configurar el repositorio para indicar que el correo ya existe
        when(userRepository.existsByUserEmail(validUserRequest.getEmail())).thenReturn(true);

        // Ejecutar la función a probar y verificar que lanza la excepción esperada
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(validUserRequest));

        // Verificar el mensaje de la excepción
        assertEquals("El correo ya registrado", exception.getMessage());

        // Verificar que NO se llamó la función isPasswordValid (porque falla antes)
        verify(passwordValidationService, never()).isPasswordValid(anyString());

        // Verificar que NO se guardó el usuario
        verify(userRepository, never()).save(any(User.class));
    }
}
