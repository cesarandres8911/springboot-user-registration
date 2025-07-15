package com.example.registration.service;

import com.example.registration.dto.PhoneRequestDTO;
import com.example.registration.dto.UserRequestDTO;
import com.example.registration.dto.UserResponseDTO;
import com.example.registration.exception.InvalidPasswordException;
import com.example.registration.mapper.PhoneMapper;
import com.example.registration.mapper.UserMapper;
import com.example.registration.model.Phone;
import com.example.registration.model.User;
import com.example.registration.repository.UserRepository;
import com.example.registration.security.jwt.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Servicio para la gestión de usuarios.
 * Proporciona métodos para registrar usuarios, autenticarlos y obtener información de usuarios.
 */
@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PhoneMapper phoneMapper;
    private final PasswordValidationService passwordValidationService;

    public UserService(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      JwtService jwtService,
                      UserMapper userMapper,
                      PhoneMapper phoneMapper,
                      @Lazy PasswordValidationService passwordValidationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.phoneMapper = phoneMapper;
        this.passwordValidationService = passwordValidationService;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param userRequestDTO DTO con la información del usuario a registrar
     * @return DTO con la información del usuario registrado, incluyendo su token JWT
     * @throws IllegalArgumentException si el correo ya está registrado
     * @throws InvalidPasswordException si la contraseña no cumple con los requisitos de validación
     */
    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        // Verificar si el correo ya está registrado
        if (userRepository.existsByUserEmail(userRequestDTO.getEmail())) {
            throw new IllegalArgumentException("El correo ya registrado");
        }

        // Validar la contraseña
        String password = userRequestDTO.getPassword();
        if (!passwordValidationService.isPasswordValid(password)) {
            logger.warn("Intento de registro con contraseña inválida para el email: {}", userRequestDTO.getEmail());
            throw new InvalidPasswordException("La contraseña no cumple con el patrón requerido");
        }

        // Crear el usuario usando el mapper
        User user = userMapper.userRequestDTOToUser(userRequestDTO);

        // Establecer campos adicionales
        LocalDateTime now = LocalDateTime.now();
        user.setUserPassword(passwordEncoder.encode(password));
        user.setLastLogin(now);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setActive(true);
        user.setPhones(new ArrayList<>());

        // Generar token JWT
        String token = jwtService.generateToken(user.getUserEmail());
        user.setUserToken(token);

        // Agregar teléfonos si existen
        if (userRequestDTO.getPhones() != null && !userRequestDTO.getPhones().isEmpty()) {
            List<Phone> phones = userRequestDTO.getPhones().stream()
                    .map(phoneDTO -> {
                        Phone phone = createPhone(phoneDTO);
                        phone.setUser(user);
                        return phone;
                    })
                    .toList();
            user.getPhones().addAll(phones);
        }

        // Guardar el usuario
        User savedUser = userRepository.save(user);

        // Convertir a DTO de respuesta usando el mapper
        return userMapper.userToUserResponseDTO(savedUser);
    }

    /**
     * Crea un objeto Phone a partir de un DTO.
     *
     * @param phoneDTO DTO con la información del teléfono
     * @return Objeto Phone creado
     */
    private Phone createPhone(PhoneRequestDTO phoneDTO) {
        return phoneMapper.phoneRequestDTOToPhone(phoneDTO);
    }


    /**
     * Carga los detalles de un usuario por su nombre de usuario (email).
     * Implementación requerida por la interfaz UserDetailsService.
     *
     * @param username El nombre de usuario (email) del usuario a cargar
     * @return Los detalles del usuario
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getUserPassword(),
                Collections.emptyList() // No roles/authorities for simplicity
        );
    }

    /**
     * Autentica un usuario con su email y contraseña.
     *
     * @param email    Email del usuario
     * @param password Contraseña del usuario
     * @return DTO con la información del usuario autenticado, incluyendo su token JWT
     * @throws BadCredentialsException   si las credenciales son inválidas
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Transactional
    public UserResponseDTO loginUser(String email, String password) {
        // Buscar el usuario por email
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // Verificar la contraseña
        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        // Actualizar último login
        LocalDateTime now = LocalDateTime.now();
        user.setLastLogin(now);

        // Generar nuevo token JWT
        String token = jwtService.generateToken(user.getUserEmail());
        user.setUserToken(token);

        // Guardar cambios
        User updatedUser = userRepository.save(user);

        // Convertir a DTO de respuesta usando el mapper
        return userMapper.userToUserResponseDTO(updatedUser);
    }
}
