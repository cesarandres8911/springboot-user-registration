# 🔐 Seguridad y JWT en el Proyecto

Este documento describe la implementación de seguridad y JWT (JSON Web Tokens) en el proyecto de registro de usuarios, explicando cómo funciona y cómo utilizarla.

## 📑 Índice

1. [📋 Visión General](#-visión-general)
2. [🔑 Implementación de JWT](#-implementación-de-jwt)
3. [🔌 Endpoints de Autenticación](#-endpoints-de-autenticación)
4. [✅ Proceso de Validación de Tokens](#-proceso-de-validación-de-tokens)
5. [⚙️ Configuración de Seguridad](#-configuración-de-seguridad)
6. [📤 Uso de JWT en Peticiones](#-uso-de-jwt-en-peticiones)

## 📋 Visión General

El proyecto implementa un sistema de autenticación basado en tokens JWT (JSON Web Tokens) para proteger los recursos y endpoints de la API. La seguridad está configurada utilizando Spring Security y proporciona:

- Registro de usuarios con generación automática de tokens JWT
- Autenticación de usuarios mediante email y contraseña
- Protección de endpoints mediante filtros de autenticación JWT
- Gestión de sesiones sin estado (stateless)
- Codificación segura de contraseñas con BCrypt

## 🔑 Implementación de JWT

### 🛠️ JwtService

El servicio `JwtService` es el componente central para la gestión de tokens JWT y proporciona las siguientes funcionalidades:

- **Generación de tokens**: Crea tokens JWT firmados para los usuarios autenticados
- **Validación de tokens**: Verifica la autenticidad y validez de los tokens
- **Extracción de información**: Obtiene datos como el nombre de usuario y la fecha de expiración de los tokens

Características principales:
- Algoritmo de firma: HMAC SHA-256
- Tiempo de expiración configurable (por defecto: 24 horas)
- Clave secreta configurable mediante propiedades de la aplicación

### 🔍 JwtAuthenticationFilter

El filtro `JwtAuthenticationFilter` intercepta todas las solicitudes HTTP y:

1. Extrae el token JWT del encabezado de autorización (`Authorization: Bearer <token>`)
2. Valida el token utilizando el `JwtService`
3. Carga los detalles del usuario si el token es válido
4. Establece la autenticación en el contexto de seguridad de Spring

## 🔌 Endpoints de Autenticación

### 📝 Registro de Usuario

**Endpoint**: `POST /api/users/register`

**Descripción**: Registra un nuevo usuario en el sistema y genera un token JWT.

**Cuerpo de la solicitud**:
```json
{
  "fullName": "Juan Rodriguez",
  "userEmail": "juan@rodriguez.org",
  "userPassword": "hunter2",
  "phones": [
    {
      "phoneNumber": "1234567",
      "cityCode": "1",
      "countryCode": "57"
    }
  ]
}
```

**Respuesta exitosa** (201 Created):
```json
{
  "id": "e5c6cf84-8860-4b0d-8957-3a5d55cde767",
  "fullName": "Juan Rodriguez",
  "userEmail": "juan@rodriguez.org",
  "created": "2023-07-03T15:30:45.123",
  "modified": "2023-07-03T15:30:45.123",
  "lastLogin": "2023-07-03T15:30:45.123",
  "userToken": "eyJhbGciOiJIUzI1NiJ9...",
  "isActive": true,
  "phones": [
    {
      "phoneNumber": "1234567",
      "cityCode": "1",
      "countryCode": "57"
    }
  ]
}
```

### 🔐 Inicio de Sesión

**Endpoint**: `POST /api/auth/login`

**Descripción**: Autentica un usuario con sus credenciales y genera un nuevo token JWT.

**Cuerpo de la solicitud**:
```json
{
  "email": "juan@rodriguez.org",
  "password": "hunter2"
}
```

**Respuesta exitosa** (200 OK):
```json
{
  "id": "e5c6cf84-8860-4b0d-8957-3a5d55cde767",
  "fullName": "Juan Rodriguez",
  "userEmail": "juan@rodriguez.org",
  "created": "2023-07-03T15:30:45.123",
  "modified": "2023-07-03T15:30:45.123",
  "lastLogin": "2023-07-03T15:31:20.456",
  "userToken": "eyJhbGciOiJIUzI1NiJ9...",
  "isActive": true,
  "phones": [
    {
      "phoneNumber": "1234567",
      "cityCode": "1",
      "countryCode": "57"
    }
  ]
}
```

## ✅ Proceso de Validación de Tokens

El proceso de validación de tokens JWT sigue estos pasos:

1. El cliente envía una solicitud HTTP con el token JWT en el encabezado de autorización
2. El `JwtAuthenticationFilter` intercepta la solicitud y extrae el token
3. El filtro utiliza el `JwtService` para validar el token y extraer el nombre de usuario (email)
4. Si el token es válido, el filtro carga los detalles del usuario utilizando el `UserDetailsService`
5. El filtro establece la autenticación en el contexto de seguridad de Spring
6. La solicitud continúa su procesamiento normal si la autenticación es exitosa

## ⚙️ Configuración de Seguridad

La configuración de seguridad se define en la clase `SecurityConfig` y establece:

- **Codificación de contraseñas**: Utiliza BCryptPasswordEncoder para el almacenamiento seguro de contraseñas
- **Proveedor de autenticación**: Configura DaoAuthenticationProvider para la autenticación basada en base de datos
- **Gestión de sesiones**: Configura sesiones sin estado (STATELESS) para una arquitectura RESTful
- **Filtro JWT**: Integra el filtro de autenticación JWT en la cadena de filtros de seguridad
- **Reglas de acceso**:
  - Permite acceso público a endpoints de registro y login
  - Permite acceso público a la consola H2 y recursos estáticos
  - Permite acceso público a la documentación Swagger
  - Requiere autenticación para todos los demás endpoints

## 📤 Uso de JWT en Peticiones

Para acceder a endpoints protegidos, los clientes deben incluir el token JWT en el encabezado de autorización de sus solicitudes HTTP:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

Ejemplo usando cURL:

```bash
curl -X GET http://localhost:8080/api/protected-resource \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

Ejemplo usando JavaScript (Fetch API):

```javascript
fetch('http://localhost:8080/api/protected-resource', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
```

El token JWT se obtiene durante el registro o inicio de sesión y debe ser almacenado por el cliente para su uso en solicitudes posteriores.
