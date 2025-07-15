package com.example.registration.security.jwt;

import com.example.registration.exception.JwtValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Filtro para la autenticación JWT.
 * Este filtro intercepta todas las solicitudes HTTP y verifica si contienen un token JWT válido.
 * Si el token es válido, establece la autenticación en el contexto de seguridad de Spring.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger loggerJwt = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String ERROR_DETAILS_MESSAGE = "Detalles del error:";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Envía una respuesta de error en formato JSON.
     *
     * @param response La respuesta HTTP
     * @throws IOException Si ocurre un error al escribir la respuesta
     */
    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("mensaje", "La firma JWT no coincide con la firma calculada localmente");

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
        response.flushBuffer(); // Ensure the response is committed
    }

    /**
     * Extrae el token JWT del encabezado de autorización.
     *
     * @param authHeader El encabezado de autorización
     * @return El token JWT extraído
     */
    private String extractJwtFromHeader(String authHeader) {
        return authHeader.substring(7);
    }

    /**
     * Verifica si una ruta es pública (no requiere autenticación).
     *
     * @param requestURI La URI de la solicitud
     * @return true si la ruta es pública, false en caso contrario
     */
    private boolean isPublicRoute(String requestURI) {
        return requestURI.equals("/api/users/register") || requestURI.equals("/api/auth/login");
    }

    /**
     * Validar el formato básico del token JWT.
     *
     * @param jwt         El token JWT
     * @param requestURI  La URI de la solicitud
     * @param filterChain La cadena de filtros
     * @param request     La solicitud HTTP
     * @param response    La respuesta HTTP
     * @return true si el token es válido, false en caso contrario
     * @throws ServletException       Si ocurre un error al procesar la solicitud
     * @throws IOException            Si ocurre un error de E/S
     * @throws JwtValidationException Si el token JWT no es válido
     */
    private boolean validateJwtFormat(String jwt, String requestURI, FilterChain filterChain,
                                      HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JwtValidationException {
        // Validar que el token no sea nulo o vacío
        if (jwt == null || jwt.trim().isEmpty()) {
            loggerJwt.error("Token JWT vacío o nulo");
            if (isPublicRoute(requestURI)) {
                filterChain.doFilter(request, response);
                return false;
            }
            throw new JwtValidationException("Token JWT vacío o nulo");
        }

        // Validar que el token tenga el formato correcto (2 puntos)
        long dotCount = jwt.chars().filter(ch -> ch == '.').count();
        if (dotCount != 2) {
            loggerJwt.error("Token JWT malformado: debe contener exactamente 2 puntos. Encontrados: {}", dotCount);
            if (isPublicRoute(requestURI)) {
                filterChain.doFilter(request, response);
                return false;
            }
            throw new JwtValidationException("Token JWT malformado: debe contener exactamente 2 puntos. Encontrados: " + dotCount);
        }

        return true;
    }

    /**
     * Autentica al usuario utilizando el token JWT.
     *
     * @param jwt     El token JWT
     * @param request La solicitud HTTP
     */
    private void authenticateUser(String jwt, HttpServletRequest request) {
        try {
            // Extraer el nombre de usuario (email) del token
            String userEmail = jwtService.extractUsername(jwt);
            loggerJwt.debug("Email extraído del token: {}", userEmail);

            // Si el email no es nulo y no hay autenticación en el contexto de seguridad
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                processAuthentication(jwt, userEmail, request);
            } else {
                logAuthenticationStatus(userEmail);
            }
        } catch (SignatureException e) {
            // Propagar la excepción de firma para que sea manejada en doFilterInternal
            loggerJwt.error("Error de firma JWT: {}", e.getMessage());
            loggerJwt.debug(ERROR_DETAILS_MESSAGE, e);
            throw new SignatureException("Error de firma JWT");
        } catch (Exception e) {
            loggerJwt.error("Error durante la autenticación: {}", e.getMessage());
            loggerJwt.debug(ERROR_DETAILS_MESSAGE, e);
        }
    }

    /**
     * Procesa la autenticación del usuario.
     *
     * @param jwt       El token JWT
     * @param userEmail El email del usuario
     * @param request   La solicitud HTTP
     */
    private void processAuthentication(String jwt, String userEmail, HttpServletRequest request) {
        loggerJwt.debug("Cargando detalles del usuario para email: {}", userEmail);
        // Cargar los detalles del usuario desde la base de datos
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        loggerJwt.debug("Detalles del usuario cargados correctamente para: {}", userEmail);

        // Verificar si el token es válido para este usuario
        loggerJwt.debug("Validando token JWT para usuario: {}", userEmail);
        if (jwtService.isTokenValid(jwt, userDetails)) {
            setAuthentication(userDetails, request);
        } else {
            loggerJwt.error("Token JWT no válido para el usuario: {}", userEmail);
        }
    }

    /**
     * Establece la autenticación en el contexto de seguridad.
     *
     * @param userDetails Los detalles del usuario
     * @param request     La solicitud HTTP
     */
    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        loggerJwt.debug("Token JWT válido para usuario: {}", userDetails.getUsername());
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
        loggerJwt.debug("Autenticación establecida en el contexto de seguridad para usuario: {}", userDetails.getUsername());
    }

    /**
     * Registra el estado de la autenticación.
     *
     * @param userEmail El email del usuario
     */
    private void logAuthenticationStatus(String userEmail) {
        if (userEmail == null) {
            loggerJwt.error("No se pudo extraer el email del token JWT");
        } else {
            loggerJwt.debug("Ya existe una autenticación en el contexto de seguridad");
        }
    }

    /**
     * Maneja las excepciones de firma JWT.
     *
     * @param e        La excepción
     * @param response La respuesta HTTP
     * @throws IOException Si ocurre un error al escribir la respuesta
     */
    private void handleJwtSignatureException(SignatureException e, HttpServletResponse response) throws IOException {
        loggerJwt.error("Error al procesar token JWT: {}", e.getMessage());
        loggerJwt.debug(ERROR_DETAILS_MESSAGE, e);

        // Enviar respuesta de error
        sendErrorResponse(response);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        final String authHeader = request.getHeader("Authorization");

        loggerJwt.debug("Procesando solicitud para URI: {}", requestURI);

        // Si no hay encabezado de autorización o no comienza con "Bearer ", continuar con la cadena de filtros
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            loggerJwt.debug("No se encontró encabezado de autorización válido para URI: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        loggerJwt.debug("Encabezado de autorización encontrado para URI: {}", requestURI);

        // Extraer el token JWT del encabezado de autorización
        final String jwt = extractJwtFromHeader(authHeader);

        // Validar formato básico del token JWT
        if (!validateJwtFormat(jwt, requestURI, filterChain, request, response)) {
            return;
        }

        try {
            // Autenticar al usuario
            authenticateUser(jwt, request);
        } catch (SignatureException e) {
            // Manejar específicamente el error de firma JWT
            handleJwtSignatureException(e, response);
            return; // Detener el procesamiento del filtro
        } catch (Exception e) {
            // Registrar el error para facilitar la depuración
            loggerJwt.error("Error al procesar el token JWT: {}", e.getMessage());
            loggerJwt.debug(ERROR_DETAILS_MESSAGE, e);
            // No establecer autenticación
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
