package com.example.registration.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro para la autenticación JWT.
 * Este filtro intercepta todas las solicitudes HTTP y verifica si contienen un token JWT válido.
 * Si el token es válido, establece la autenticación en el contexto de seguridad de Spring.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Si no hay encabezado de autorización o no comienza con "Bearer ", continuar con la cadena de filtros
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el token JWT del encabezado de autorización
        jwt = authHeader.substring(7);

        try {
            // Extraer el nombre de usuario (email) del token
            userEmail = jwtService.extractUsername(jwt);

            // Si el email no es nulo y no hay autenticación en el contexto de seguridad
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Cargar los detalles del usuario desde la base de datos
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Verificar si el token es válido para este usuario
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Crear un token de autenticación
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // Establecer los detalles de la autenticación
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception _) {
            // En caso de error al procesar el token, simplemente continuar con la cadena de filtros
            // No establecer autenticación
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}