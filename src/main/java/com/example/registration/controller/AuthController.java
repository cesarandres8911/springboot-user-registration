package com.example.registration.controller;

import com.example.registration.dto.LoginRequestDTO;
import com.example.registration.dto.UserResponseDTO;
import com.example.registration.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador para la autenticación de usuarios.
 * Proporciona endpoints para el inicio de sesión.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API para la autenticación de usuarios")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para el inicio de sesión de usuarios.
     *
     * @param loginRequestDTO DTO con las credenciales del usuario
     * @return ResponseEntity con el DTO del usuario autenticado o un mensaje de error
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con sus credenciales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        UserResponseDTO userResponseDTO = userService.loginUser(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()
        );
        return ResponseEntity.ok(userResponseDTO);
    }
}
