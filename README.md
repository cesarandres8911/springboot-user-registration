# springboot-user-registration

## 📋 Descripción general

Este proyecto es un sistema de registro de usuarios construido con Spring Boot. Proporciona APIs RESTful para el registro, autenticación y gestión de usuarios.

## ✨ Funcionalidades
- Registro de usuarios con validación
- Autenticación de usuarios (login)
- Almacenamiento seguro de contraseñas
- Endpoints para gestión de usuarios
- Manejo de excepciones

## 🛠️ Tecnologías utilizadas
- Java
- Spring Boot
- Spring Data JPA
- H2 (base de datos en memoria)
- Gradle
- Lombok
- SLF4J (Logger)
- Springdoc OpenAPI (para documentación y anotaciones @Schema en los DTOs)
- MapStruct (para mapeo entre entidades y DTOs)

## 🚀 Primeros pasos

### 📋 Prerrequisitos
- Java 17 o superior
- Gradle

### ▶️ Ejecución de la aplicación

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


### 📝 Uso de la aplicación

#### 👤 Registro de usuarios

Para registrar un nuevo usuario, puede utilizar los siguientes métodos:

##### Usando cURL (desde la consola)

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "password": "hunter2",
    "phones": [
      {
        "number": "1234567",
        "citycode": "1",
        "contrycode": "57"
      }
    ]
  }'
```

##### Usando Postman

1. Abra Postman
2. Cree una nueva solicitud POST a `http://localhost:8080/api/users/register`
3. En la pestaña "Headers", agregue `Content-Type: application/json`
4. En la pestaña "Body", seleccione "raw" y "JSON", y agregue el siguiente contenido:
   ```json
   {
     "name": "Juan Rodriguez",
     "email": "juan@rodriguez.org",
     "password": "hunter2",
     "phones": [
       {
         "number": "1234567",
         "citycode": "1",
         "contrycode": "57"
       }
     ]
   }
   ```
5. Haga clic en "Send" para enviar la solicitud

#### 🔐 Autenticación de usuarios

Para autenticar un usuario existente, puede utilizar los siguientes métodos:

##### Usando cURL (desde la consola)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@rodriguez.org",
    "password": "hunter2"
  }'
```

##### Usando Postman

1. Abra Postman
2. Cree una nueva solicitud POST a `http://localhost:8080/api/auth/login`
3. En la pestaña "Headers", agregue `Content-Type: application/json`
4. En la pestaña "Body", seleccione "raw" y "JSON", y agregue el siguiente contenido:
   ```json
   {
     "email": "juan@rodriguez.org",
     "password": "hunter2"
   }
   ```
5. Haga clic en "Send" para enviar la solicitud

### 🔗 Endpoints principales

- `POST /api/users/register` - Registrar un nuevo usuario
- `POST /api/auth/login` - Autenticar un usuario
- `GET /api/users` - Listar todos los usuarios (requiere autenticación)

### 💾 Configuración de la base de datos

Este proyecto utiliza H2 como base de datos en memoria para desarrollo y pruebas. No es necesario levantar ningún contenedor externo ni configurar variables de entorno para la base de datos.

La configuración de H2 se encuentra en el archivo `src/main/resources/application.properties`.

Para acceder a la consola web de H2, una vez levantada la aplicación, acceda a:

    http://localhost:8080/h2-console

Las credenciales y URL de conexión están documentadas en el archivo de propiedades.


### 🧪 Pruebas

Ejecute las pruebas con:
```bash
./gradlew test
```

## 📂 Estructura del proyecto

- `controller/` - Controladores REST
- `service/` - Lógica de negocio
- `repository/` - Acceso a datos
- `model/` - Entidades JPA
- `dto/` - Objetos de transferencia de datos
- `exception/` - Excepciones personalizadas y manejadores
- `config/` - Clases de configuración

## 📝 Logging

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


## 📄 Licencia

Este proyecto está licenciado bajo MIT License.

## 🏗️ Modelo de arquitectura

El siguiente diagrama representa la estructura y flujo de los principales componentes del proyecto:

```
[controller] --> [service] --> [repository] --> [model]
                     |
                   [util]
                     |
                [exception]
```

Esto ilustra el flujo típico de una petición y cómo se separan las responsabilidades en la base de código.


---

## 📚 Documentación

A continuación se presentan los enlaces a la documentación disponible en el proyecto:

### 📘 Documentación de la API
- ✅ **Swagger UI:** [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui.html)

### 🗃️ Base de datos
- 📊 **Documentación de la base de datos:** [`docs/database/README.md`](docs/database/README.md)

### 🔒 Seguridad
- 🔐 **Documentación de seguridad y JWT:** [`docs/security/README.md`](docs/security/README.md)

### 🛠️ Utilidades
- 📚 **Índice de utilidades:** [`docs/utils/README.md`](docs/utils/README.md)
- 📝 **Tipos de configuración de contraseñas:** [`docs/utils/README-PasswordConfigurationType.md`](docs/utils/README-PasswordConfigurationType.md)
- 🔍 **Generación de expresiones regulares:** [`docs/utils/README-PasswordRegexGenerator.md`](docs/utils/README-PasswordRegexGenerator.md)
- ✅ **Validación de contraseñas:** [`docs/utils/README-PasswordValidator.md`](docs/utils/README-PasswordValidator.md)
- 🔐 **Cifrado de contraseñas:** [`docs/utils/README-PasswordEncoderUtil.md`](docs/utils/README-PasswordEncoderUtil.md)

### 🔄 Mappers
- 🔄 **Documentación de mappers:** [`docs/mappers/README.md`](docs/mappers/README.md)

### 🛡️ Excepciones
- 🛡️ **Documentación de manejo de excepciones:** [`docs/exceptions/README.md`](docs/exceptions/README.md)

### 📝 Ejercicio propuesto
- 📋 **Descripción del ejercicio:** [`docs/exercise/integration_proposed_exercise.md`](docs/exercise/integration_proposed_exercise.md)

---

### 🔄 Cambios recientes
- 🛡️ Se agregó documentación detallada sobre el manejo de excepciones y posibles mejoras futuras.
- ✅ Se implementaron utilidades para la validación de contraseñas basadas en configuraciones almacenadas en la base de datos.
- 📚 Se agregó documentación detallada sobre las utilidades de validación de contraseñas.
- 🔄 Se actualizó la documentación para reflejar el uso de H2 como base de datos en memoria, eliminando referencias a PostgreSQL, Docker y .env para la base de datos.
- 📋 Se actualizó la documentación de la base de datos para incluir información detallada sobre el esquema de la base de datos y las entidades JPA implementadas.
- 🔄 Se agregó documentación sobre el uso de MapStruct como biblioteca para el mapeo entre entidades y DTOs.
