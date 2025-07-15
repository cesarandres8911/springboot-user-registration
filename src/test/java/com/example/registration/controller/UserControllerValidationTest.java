package com.example.registration.controller;

import com.example.registration.dto.PhoneRequestDTO;
import com.example.registration.dto.UserRequestDTO;
import com.example.registration.exception.GlobalExceptionHandler;
import com.example.registration.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerValidationTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void testValidationErrorMessagesDoNotIncludeFieldNames() throws Exception {
        // Create a user request with empty fields to trigger validation errors
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("");
        userRequestDTO.setEmail("");
        userRequestDTO.setPassword("");
        userRequestDTO.setPhones(Collections.emptyList());

        // Perform the request and get the result
        MvcResult result = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Get the response body
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("[DEBUG_LOG] Response body: " + responseBody);

        // Check that the response does not contain field names followed by error messages
        assertFalse(responseBody.contains("name El nombre es obligatorio."),
                "Response should not contain 'name El nombre es obligatorio.'");
        assertFalse(responseBody.contains("email El correo es obligatorio."),
                "Response should not contain 'email El correo es obligatorio.'");
        assertFalse(responseBody.contains("password La contraseña es obligatoria."),
                "Response should not contain 'password La contraseña es obligatoria.'");
        assertFalse(responseBody.contains("phones Debe proporcionar al menos un teléfono."),
                "Response should not contain 'phones Debe proporcionar al menos un teléfono.'");

        // Check that the response contains the error messages without field names
        assertTrue(responseBody.contains("El nombre es obligatorio."),
                "Response should contain 'El nombre es obligatorio.'");
        assertTrue(responseBody.contains("El correo es obligatorio."),
                "Response should contain 'El correo es obligatorio.'");
        assertTrue(responseBody.contains("La contraseña es obligatoria."),
                "Response should contain 'La contraseña es obligatoria.'");
        assertTrue(responseBody.contains("Debe proporcionar al menos un teléfono."),
                "Response should contain 'Debe proporcionar al menos un teléfono.'");
    }

    @Test
    void testGeneralExceptionDoesNotExposeDetails() {
        // Test the GlobalExceptionHandler directly
        String sensitiveErrorMessage = "could not execute statement [NULL not allowed for column \"PHONE_NUMBER\"; SQL statement:\ninsert into phones (city_code,country_code,phone_number,user_id,id) values (?,?,?,?,default) [23502-224]]";
        Exception exception = new Exception(sensitiveErrorMessage);

        // Call the handler directly
        org.springframework.http.ResponseEntity<Object> response = globalExceptionHandler.handleGeneralException(exception);

        // Convert the response body to a string for testing
        @SuppressWarnings("unchecked")
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        assertNotNull(responseMap);
        String responseMessage = responseMap.get("mensaje");
        System.out.println("[DEBUG_LOG] Response message: " + responseMessage);

        // Check that the response does not contain sensitive information
        assertFalse(responseMessage.contains(sensitiveErrorMessage),
                "Response should not contain sensitive error details");
        assertFalse(responseMessage.contains("PHONE_NUMBER"),
                "Response should not contain database column names");
        assertFalse(responseMessage.contains("SQL statement"),
                "Response should not contain SQL statements");

        // Check that the response contains only a generic error message
        assertEquals("Error en el servidor. Contacte al administrador del sistema.", responseMessage, "Response should contain a generic error message");
    }

    @Test
    void testDuplicateEmailReturnsSpecificErrorMessage() throws Exception {
        // Create a valid user request with valid email (.cl domain) and phone
        UserRequestDTO userRequestDTO = getUserRequestDTO();

        // Mock the service to throw an IllegalArgumentException with the specific message
        Mockito.when(userService.registerUser(Mockito.any(UserRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("El correo ya registrado"));

        // Perform the request and get the result
        MvcResult result = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Get the response body
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("[DEBUG_LOG] Response body for duplicate email: " + responseBody);

        // Check that the response contains the specific error message
        assertTrue(responseBody.contains("El correo ya registrado"),
                "Response should contain 'El correo ya registrado'");
    }

    private static UserRequestDTO getUserRequestDTO() {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Test User");
        userRequestDTO.setEmail("test@example.cl");
        userRequestDTO.setPassword("Password123");

        // Create a valid phone
        PhoneRequestDTO phoneRequestDTO = new PhoneRequestDTO();
        phoneRequestDTO.setNumber("1234567");
        phoneRequestDTO.setCitycode("1");
        phoneRequestDTO.setContrycode("57");
        userRequestDTO.setPhones(Collections.singletonList(phoneRequestDTO));
        return userRequestDTO;
    }
}
