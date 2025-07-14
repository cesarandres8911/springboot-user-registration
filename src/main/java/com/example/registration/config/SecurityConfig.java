package com.example.registration.config;

import com.example.registration.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad para la aplicación.
 * Esta clase configura Spring Security para permitir acceso a endpoints específicos.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura el codificador de contraseñas.
     *
     * @return PasswordEncoder configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el proveedor de autenticación para la aplicación.
     *
     * @param userDetailsService Servicio para cargar detalles de usuario
     * @param passwordEncoder    Codificador de contraseñas
     * @return AuthenticationProvider configurado
     */
    @Bean
    @SuppressWarnings("deprecation")
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Configura el administrador de autenticación.
     *
     * @param config Configuración de autenticación
     * @return AuthenticationManager configurado
     * @throws Exception Sí ocurre un error durante la configuración
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura las reglas de seguridad para la aplicación.
     *
     * @param http                   Configuración de seguridad HTTP
     * @param jwtAuthFilter          Filtro de autenticación JWT
     * @param authenticationProvider Proveedor de autenticación
     * @return SecurityFilterChain configurado
     * @throws Exception Sí ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthFilter,
            AuthenticationProvider authenticationProvider) throws Exception {
        http
                // Deshabilitar CSRF para la consola H2 y endpoints de API
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        // Permitir frames para la consola H2
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // Configurar manejo de sesiones sin estado (stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configurar proveedor de autenticación
                .authenticationProvider(authenticationProvider)
                // Agregar filtro JWT antes del filtro de autenticación de usuario y contraseña
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        // Permitir acceso a la consola H2
                        .requestMatchers("/h2-console/**").permitAll()
                        // Permitir acceso a recursos estáticos
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // Permitir acceso a endpoints públicos
                        .requestMatchers("/", "/api/users/register", "/api/auth/login").permitAll()
                        // Permitir acceso a documentación Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Requerir autenticación para cualquier otra solicitud
                        .anyRequest().authenticated());

        return http.build();
    }

}
