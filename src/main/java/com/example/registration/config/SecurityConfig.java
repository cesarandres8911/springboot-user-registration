package com.example.registration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para la aplicación.
 * Esta clase configura Spring Security para permitir acceso a endpoints específicos.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura las reglas de seguridad para la aplicación.
     * 
     * @param http Configuración de seguridad HTTP
     * @return SecurityFilterChain configurado
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF para la consola H2 y endpoints de API
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                // Permitir frames para la consola H2
                .frameOptions(frameOptions -> frameOptions.disable()))
            .authorizeHttpRequests(authorize -> authorize
                // Permitir acceso a la consola H2
                .requestMatchers("/h2-console/**").permitAll()
                // Permitir acceso a recursos estáticos
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // Permitir acceso a la página principal y endpoints de API
                .requestMatchers("/", "/api/**").permitAll()
                // Requerir autenticación para cualquier otra solicitud
                .anyRequest().authenticated());

        return http.build();
    }
}
