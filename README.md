# springboot-user-registration

## Descripción general

Este proyecto es un sistema de registro de usuarios construido con Spring Boot. Proporciona APIs RESTful para el registro, autenticación y gestión de usuarios.

## Funcionalidades
- Registro de usuarios con validación
- Autenticación de usuarios (login)
- Almacenamiento seguro de contraseñas
- Endpoints para gestión de usuarios
- Manejo de excepciones

## Tecnologías utilizadas
- Java
- Spring Boot
- Spring Data JPA
- H2 (base de datos en memoria)
- Gradle
- Lombok
- SLF4J (Logger)
- Springdoc OpenAPI (para documentación y anotaciones @Schema en los DTOs)

## Primeros pasos

### Prerrequisitos
- Java 17 o superior
- Gradle

### Ejecución de la aplicación

1. Clone el repositorio:
   ```bash
   git clone <repository-url>
   cd springboot-user-registration
   ```
2. Compile y ejecute la aplicación:
   ```bash
   ./gradlew bootRun
   ```
3. La aplicación estará disponible en `http://localhost:8080` por defecto.

### Endpoints principales

- `POST /api/register` - Registrar un nuevo usuario
- `POST /api/login` - Autenticar un usuario
- `GET /api/users` - Listar todos los usuarios (requiere autenticación)

### Configuración de la base de datos

Este proyecto utiliza H2 como base de datos en memoria para desarrollo y pruebas. No es necesario levantar ningún contenedor externo ni configurar variables de entorno para la base de datos.

La configuración de H2 se encuentra en el archivo `src/main/resources/application.properties`.

Para acceder a la consola web de H2, una vez levantada la aplicación, acceda a:

    http://localhost:8080/h2-console

Las credenciales y URL de conexión están documentadas en el archivo de propiedades.

> Nota: Los archivos `docker-compose.yml` y `.env` ya no son necesarios ni forman parte del proyecto, ya que la base de datos utilizada es H2 en memoria.

### Pruebas

Ejecute las pruebas con:
```bash
./gradlew test
```

## Estructura del proyecto

- `controller/` - Controladores REST
- `service/` - Lógica de negocio
- `repository/` - Acceso a datos
- `model/` - Entidades JPA
- `dto/` - Objetos de transferencia de datos
- `exception/` - Excepciones personalizadas y manejadores
- `config/` - Clases de configuración

## Logging

Este proyecto utiliza SLF4J para el registro de mensajes informativos, advertencias y errores. Ejemplo de uso:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YourClass {
    private static final Logger logger = LoggerFactory.getLogger(YourClass.class);

    public void ejemploLogger() {
        logger.info("Mensaje informativo");
        logger.warn("Mensaje de advertencia");
        logger.error("Mensaje de error");
    }
}
```

## Documentación de la API

Se utiliza Springdoc OpenAPI para generar y documentar la API con OpenAPI 3 y Swagger UI. Los DTOs están anotados con @Schema para mejor documentación y validación.

- Acceda a Swagger UI en: `http://localhost:8080/swagger-ui.html` (o `/swagger-ui/index.html` según la versión de Springdoc).
- Los DTOs usan @Schema para documentación de campos y anotaciones de validación.

## Ejercicio propuesto

El ejercicio propuesto para este proyecto se encuentra en:

- [`docs/ejercicio/ejercicio_propuesto_integracion.md`](docs/ejercicio/ejercicio_propuesto_integracion.md)

## Licencia

Este proyecto está licenciado bajo MIT License.

## Modelo de arquitectura

El siguiente diagrama representa la estructura y flujo de los principales componentes del proyecto:

```
[controller] --> [service] --> [repository] --> [model]
                     |
                   [util]
                     |
                [exception]
```

Esto ilustra el flujo típico de una petición y cómo se separan las responsabilidades en la base de código.

## Base de datos

La documentación y detalles de la base de datos, así como el modelo y scripts de creación, se encuentran en:

- [`docs/database/README.md`](docs/database/README.md)

Allí se explica cómo utilizar H2 como base de datos en memoria, la configuración en `application.properties` y el modelo de datos completo, incluyendo las entidades implementadas (User, Phone) y las tablas adicionales preparadas para futuras funcionalidades (configuration, role, permission, roles_permissions).

## Seguridad

La documentación sobre la implementación de seguridad y JWT (JSON Web Tokens) en el proyecto se encuentra en:

- [`docs/security/README.md`](docs/security/README.md)

Este documento describe cómo funciona la autenticación basada en tokens JWT, los endpoints de autenticación disponibles, el proceso de validación de tokens, la configuración de seguridad y cómo utilizar JWT en las peticiones a la API.

## Utilidades

El proyecto incluye varias utilidades para facilitar tareas comunes:

### Validación de contraseñas

Se ha implementado un conjunto de utilidades para la validación de contraseñas basadas en configuraciones almacenadas en la base de datos. Estas utilidades permiten generar expresiones regulares dinámicas según criterios configurables.

La documentación detallada se encuentra en:

- [`docs/utils/README.md`](docs/utils/README.md)

---

### Cambios recientes
- Se implementaron utilidades para la validación de contraseñas basadas en configuraciones almacenadas en la base de datos.
- Se agregó documentación detallada sobre las utilidades de validación de contraseñas.
- Se actualizó la documentación para reflejar el uso de H2 como base de datos en memoria, eliminando referencias a PostgreSQL, Docker y .env para la base de datos.
- Se actualizó la documentación de la base de datos para incluir información detallada sobre el esquema de la base de datos y las entidades JPA implementadas.

## Documentación completa

A continuación se presenta un índice de toda la documentación disponible en el proyecto:

### Documentación principal
- [README.md](README.md) - Este documento, con información general del proyecto

### Ejercicio propuesto
- [docs/ejercicio/ejercicio_propuesto_integracion.md](docs/ejercicio/ejercicio_propuesto_integracion.md) - Descripción del ejercicio propuesto

### Base de datos
- [docs/database/README.md](docs/database/README.md) - Documentación sobre la base de datos

### Seguridad
- [docs/security/README.md](docs/security/README.md) - Documentación sobre seguridad y JWT

### Utilidades
- [docs/utils/README.md](docs/utils/README.md) - Índice de utilidades de contraseñas
  - [docs/utils/README-PasswordConfigurationType.md](docs/utils/README-PasswordConfigurationType.md) - Documentación sobre tipos de configuración de contraseñas
  - [docs/utils/README-PasswordRegexGenerator.md](docs/utils/README-PasswordRegexGenerator.md) - Documentación sobre generación de expresiones regulares para contraseñas
  - [docs/utils/README-PasswordValidator.md](docs/utils/README-PasswordValidator.md) - Documentación sobre validación de contraseñas
  - [docs/utils/README-PasswordEncoderUtil.md](docs/utils/README-PasswordEncoderUtil.md) - Documentación sobre cifrado de contraseñas
