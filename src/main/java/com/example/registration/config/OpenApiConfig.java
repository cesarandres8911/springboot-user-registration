package com.example.registration.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración para la documentación OpenAPI/Swagger.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura la información de la API para Swagger UI.
     *
     * @return Configuración OpenAPI
     */
    @Bean
    public OpenAPI userRegistrationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Registro de Usuarios")
                        .description("API para el registro y gestión de usuarios")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Soporte")
                                .email("soporte@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo local")
                ));
    }
}