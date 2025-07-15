# 🛡️ Manejo de Excepciones

## 📋 Descripción general

Este documento describe el enfoque de manejo de excepciones implementado en el proyecto de registro de usuarios. El manejo de excepciones es una parte crucial de cualquier aplicación robusta, ya que permite manejar situaciones inesperadas de manera elegante y proporcionar respuestas significativas a los usuarios.

## 🏗️ Arquitectura actual

Actualmente, el proyecto utiliza un enfoque centralizado para el manejo de excepciones a través de la clase `GlobalExceptionHandler`. Esta clase está anotada con `@ControllerAdvice`, lo que permite interceptar excepciones lanzadas en cualquier parte de la aplicación y proporcionar respuestas HTTP consistentes.

### 🔍 GlobalExceptionHandler

La clase `GlobalExceptionHandler` se encuentra en el paquete `com.example.registration.exception` y proporciona manejadores específicos para diferentes tipos de excepciones:

1. **MethodArgumentNotValidException**: Maneja errores de validación de los DTOs, devolviendo un mapa de errores de validación con código HTTP 400 (BAD_REQUEST).
2. **IllegalArgumentException**: Maneja argumentos inválidos, como intentar registrar un usuario con un correo ya existente, devolviendo código HTTP 400 (BAD_REQUEST).
3. **BadCredentialsException**: Maneja errores de autenticación cuando las credenciales son incorrectas, devolviendo código HTTP 401 (UNAUTHORIZED).
4. **UsernameNotFoundException**: Maneja casos donde no se encuentra un usuario, devolviendo código HTTP 404 (NOT_FOUND).
5. **InvalidPasswordException**: Maneja errores cuando una contraseña no cumple con los requisitos de validación, devolviendo código HTTP 400 (BAD_REQUEST).
6. **JwtValidationException**: Maneja errores en la validación de un token JWT, devolviendo código HTTP 401 (UNAUTHORIZED).
7. **AccessDeniedException**: Maneja errores cuando un usuario no tiene los permisos necesarios para acceder a un recurso, devolviendo código HTTP 403 (FORBIDDEN).
8. **Exception**: Manejador genérico para cualquier otra excepción no manejada específicamente, devolviendo código HTTP 500 (INTERNAL_SERVER_ERROR).

Cada manejador devuelve una respuesta JSON con un mensaje descriptivo del error, lo que facilita la comprensión del problema por parte del cliente.

### 📊 Formato de respuesta

Las respuestas de error siguen un formato consistente:

```json
{
  "mensaje": "Descripción del error",
  "errores": {
    "campo1": "Error específico del campo1",
    "campo2": "Error específico del campo2"
  }
}
```

El campo `errores` solo está presente en las respuestas a `MethodArgumentNotValidException`, donde se detallan los errores de validación por campo.

## 🔄 Flujo de manejo de excepciones

1. Una excepción es lanzada en alguna parte de la aplicación (controlador, servicio, etc.).
2. Spring intercepta la excepción y la dirige al manejador correspondiente en `GlobalExceptionHandler`.
3. El manejador procesa la excepción y construye una respuesta HTTP con el formato adecuado.
4. La respuesta es enviada al cliente.

## 📋 Tipos de excepciones utilizadas

El proyecto utiliza principalmente excepciones estándar de Java y Spring:

- **IllegalArgumentException**: Para validaciones de negocio, como correos duplicados.
- **BadCredentialsException**: Para errores de autenticación.
- **UsernameNotFoundException**: Para usuarios no encontrados.
- **MethodArgumentNotValidException**: Lanzada automáticamente por Spring cuando fallan las validaciones de Bean Validation.

## 🚀 Mejoras futuras

### 🔍 Separación de clases de excepciones por responsabilidad

Una mejora significativa sería implementar excepciones personalizadas específicas para cada dominio de la aplicación. Esto permitiría:

1. **Mayor claridad en el código**: Al lanzar excepciones con nombres descriptivos, el código se vuelve más legible y autoexplicativo.
2. **Manejo más granular**: Permite responder de manera específica a cada tipo de error.
3. **Mejor documentación**: Las excepciones personalizadas pueden incluir información detallada sobre por qué ocurrió el error y cómo solucionarlo.

#### Propuesta de estructura de excepciones

```
com.example.registration.exception/
├── GlobalExceptionHandler.java
├── base/
│   ├── BaseException.java
│   ├── ResourceNotFoundException.java
│   └── ValidationException.java
├── user/
│   ├── UserNotFoundException.java
│   ├── DuplicateEmailException.java
│   └── InvalidCredentialsException.java
├── security/
│   ├── AuthenticationException.java
│   └── AuthorizationException.java
└── validation/
    └── InvalidInputException.java
```

### 📝 Mejora en la documentación de errores

Otra mejora sería implementar un sistema más detallado de códigos de error, que incluya:

1. **Códigos de error únicos**: Para facilitar la referencia y búsqueda en la documentación.
2. **Mensajes más descriptivos**: Con sugerencias sobre cómo resolver el problema.
3. **Enlaces a documentación**: Para errores complejos, incluir enlaces a documentación más detallada.

### 🔄 Integración con sistema de monitoreo

Implementar integración con herramientas de monitoreo como Sentry, New Relic o ELK Stack para:

1. **Seguimiento de errores en producción**: Detectar y resolver problemas rápidamente.
2. **Análisis de tendencias**: Identificar patrones de errores recurrentes.
3. **Alertas**: Configurar notificaciones para errores críticos.

## 📚 Conclusión

El enfoque actual de manejo de excepciones proporciona una base sólida para gestionar errores de manera consistente. Las mejoras propuestas permitirían una mayor granularidad, mejor documentación y monitoreo más efectivo, lo que resultaría en una experiencia de usuario mejorada y un sistema más mantenible.
