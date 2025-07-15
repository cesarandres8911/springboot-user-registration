package com.example.registration.service;

import com.example.registration.dto.PhoneRequestDTO;
import com.example.registration.dto.UserRequestDTO;
import com.example.registration.dto.UserResponseDTO;
import com.example.registration.mapper.PhoneMapper;
import com.example.registration.mapper.UserMapper;
import com.example.registration.model.Phone;
import com.example.registration.model.User;
import com.example.registration.repository.UserRepository;
import com.example.registration.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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

    private UserRequestDTO userRequestDTO;
    private User user;
    private UserResponseDTO userResponseDTO;
    private String jwtToken;
    private UUID userId;

    @BeforeEach
    void setUp() {
        // Setup test data
        userId = UUID.randomUUID();
        jwtToken = "test-jwt-token";

        // Create UserRequestDTO
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Test User");
        userRequestDTO.setEmail("test@example.com");
        userRequestDTO.setPassword("ValidPassword1#");

        List<PhoneRequestDTO> phones = new ArrayList<>();
        PhoneRequestDTO phoneDTO = new PhoneRequestDTO();
        phoneDTO.setNumber("1234567890");
        phoneDTO.setCitycode("1");
        phoneDTO.setContrycode("57");
        phones.add(phoneDTO);
        userRequestDTO.setPhones(phones);

        // Create User
        user = new User();
        user.setId(userId);
        user.setFullName(userRequestDTO.getName());
        user.setUserEmail(userRequestDTO.getEmail());
        user.setUserPassword("encodedPassword");
        user.setUserToken(jwtToken);
        user.setActive(true);
        user.setLastLogin(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPhones(new ArrayList<>());

        // Create UserResponseDTO
        userResponseDTO = UserResponseDTO.builder()
                .id(userId)
                .created(user.getCreatedAt())
                .modified(user.getUpdatedAt())
                .last_login(user.getLastLogin())
                .token(jwtToken)
                .isactive(true)
                .build();
    }

    @Test
    void registerUser_shouldRegisterUserWithPhones() {
        // Arrange
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(passwordValidationService.isPasswordValid(anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(anyString())).thenReturn(jwtToken);
        when(userMapper.userRequestDTOToUser(any(UserRequestDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        // Mock phone mapping
        Phone phone = new Phone();
        phone.setPhoneNumber("1234567890");
        phone.setCityCode("1");
        phone.setCountryCode("57");
        when(phoneMapper.phoneRequestDTOToPhone(any(PhoneRequestDTO.class))).thenReturn(phone);

        // Act
        UserResponseDTO result = userService.registerUser(userRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(jwtToken, result.getToken());
        assertTrue(result.isIsactive());
        assertNotNull(result.getCreated());
        assertNotNull(result.getLast_login());

        // Verify interactions
        verify(userRepository).existsByUserEmail(userRequestDTO.getEmail());
        verify(passwordValidationService).isPasswordValid(userRequestDTO.getPassword());
        verify(passwordEncoder).encode(userRequestDTO.getPassword());
        verify(jwtService).generateToken(userRequestDTO.getEmail());
        verify(userMapper).userRequestDTOToUser(userRequestDTO);
        verify(userRepository).save(any(User.class));
        verify(userMapper).userToUserResponseDTO(user);
        verify(phoneMapper).phoneRequestDTOToPhone(any(PhoneRequestDTO.class));
    }

    @Test
    void loadUserByUsername_whenUserExists_shouldReturnUserDetails() {
        // Arrange
        when(userRepository.findByUserEmail(userRequestDTO.getEmail())).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(userRequestDTO.getEmail());

        // Assert
        assertNotNull(userDetails);
        assertEquals(userRequestDTO.getEmail(), userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());

        // Verify interactions
        verify(userRepository).findByUserEmail(userRequestDTO.getEmail());
    }

    @Test
    void loadUserByUsername_whenUserDoesNotExist_shouldThrowException() {
        // Arrange
        String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByUserEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> userService.loadUserByUsername(nonExistentEmail)
        );

        // Verify message and interactions
        assertTrue(exception.getMessage().contains(nonExistentEmail));
        verify(userRepository).findByUserEmail(nonExistentEmail);
    }

    @Test
    void loginUser_withValidCredentials_shouldReturnUserResponse() {
        // Arrange
        String email = userRequestDTO.getEmail();
        String password = userRequestDTO.getPassword();

        when(userRepository.findByUserEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getUserPassword())).thenReturn(true);
        when(jwtService.generateToken(email)).thenReturn(jwtToken);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserResponseDTO(user)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.loginUser(email, password);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(jwtToken, result.getToken());
        assertTrue(result.isIsactive());
        assertNotNull(result.getLast_login());

        // Verify interactions
        verify(userRepository).findByUserEmail(email);
        verify(passwordEncoder).matches(password, user.getUserPassword());
        verify(jwtService).generateToken(email);

        // Verify user was updated with new login time and token
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertNotNull(savedUser.getLastLogin());
        assertEquals(jwtToken, savedUser.getUserToken());

        verify(userMapper).userToUserResponseDTO(user);
    }

    @Test
    void loginUser_withInvalidPassword_shouldThrowException() {
        // Arrange
        String email = userRequestDTO.getEmail();
        String wrongPassword = "WrongPassword1#";

        when(userRepository.findByUserEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(wrongPassword, user.getUserPassword())).thenReturn(false);

        // Act & Assert
        BadCredentialsException exception = assertThrows(
            BadCredentialsException.class,
            () -> userService.loginUser(email, wrongPassword)
        );

        // Verify message and interactions
        assertEquals("Credenciales inválidas", exception.getMessage());
        verify(userRepository).findByUserEmail(email);
        verify(passwordEncoder).matches(wrongPassword, user.getUserPassword());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUser_withNonExistentUser_shouldThrowException() {
        // Arrange
        String nonExistentEmail = "nonexistent@example.com";
        String password = userRequestDTO.getPassword();

        when(userRepository.findByUserEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> userService.loginUser(nonExistentEmail, password)
        );

        // Verify message and interactions
        assertTrue(exception.getMessage().contains(nonExistentEmail));
        verify(userRepository).findByUserEmail(nonExistentEmail);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
