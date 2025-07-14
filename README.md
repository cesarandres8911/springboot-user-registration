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


### 🔗 Endpoints principales

- `POST /api/users/register` - Registrar un nuevo usuario
- `POST /api/auth/login` - Autenticar un usuario
- `GET /api/users` - Listar todos los usuarios (requiere autenticación)
- `GET /api/configurations` - Listar todas las configuraciones activas (requiere autenticación)
- `GET /api/configurations/{typeKey}` - Obtener una configuración específica por su tipo (requiere autenticación)
- `PUT /api/configurations` - Actualizar una configuración existente o crear una nueva (requiere autenticación)
- `PUT /api/configurations/{typeKey}?value=nuevoValor` - Actualizar una configuración por su tipo (requiere autenticación)

### 📝 Uso de la aplicación

#### 🔄 Consumo de endpoints con cURL

A continuación se muestra cómo consumir los diferentes endpoints de la API utilizando cURL desde la consola:

##### 👤 Registro de usuarios

> **Requisitos de contraseña**: La contraseña debe cumplir con los siguientes criterios:
> - Longitud mínima: 8 caracteres
> - Longitud máxima: 30 caracteres
> - Al menos 1 letra mayúscula
> - Al menos 1 letra minúscula
> - Al menos 1 dígito
> - Al menos 1 carácter especial
> - Caracteres especiales permitidos: -.#$%&

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.cl",
    "password": "Prueba.123$#-",
    "phones": [
      {
        "number": "1234567",
        "citycode": "1",
        "contrycode": "57"
      }
    ]
  }'
```

##### 🔐 Autenticación de usuarios

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@rodriguez.cl",
    "password": "Prueba.123$#-"
  }'
```

> **Nota importante**: La respuesta de los endpoints de registro y autenticación incluye un token JWT en el campo `token`. Este token debe ser utilizado para acceder a los endpoints protegidos incluyéndolo en el encabezado `Authorization` con el formato `Bearer {token}`.

##### ⚙️ Sistema de configuración

El sistema permite personalizar los requisitos de contraseña a través de la API de configuraciones. Esto es especialmente útil para ajustar las políticas de seguridad según las necesidades específicas de su organización.

> **Configuraciones disponibles para contraseñas**:
> - `password.min.length`: Longitud mínima de caracteres (valor predeterminado: 8)
> - `password.max.length`: Longitud máxima de caracteres (valor predeterminado: 30)
> - `password.min.uppercase`: Cantidad mínima de letras mayúsculas (valor predeterminado: 1)
> - `password.min.lowercase`: Cantidad mínima de letras minúsculas (valor predeterminado: 1)
> - `password.min.digits`: Cantidad mínima de dígitos (valor predeterminado: 1)
> - `password.min.special`: Cantidad mínima de caracteres especiales (valor predeterminado: 1)
> - `password.allowed.special`: Caracteres especiales permitidos (valor predeterminado: "-.#$%&")

##### ⚙️ Obtener todas las configuraciones

```bash
curl -X GET http://localhost:8080/api/configurations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

##### ⚙️ Obtener una configuración específica

```bash
curl -X GET http://localhost:8080/api/configurations/password.min.length \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

##### ⚙️ Actualizar una configuración

```bash
curl -X PUT http://localhost:8080/api/configurations/password.min.length?value=10 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

##### ⚙️ Actualizar una configuración usando JSON

```bash
curl -X PUT http://localhost:8080/api/configurations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "configurationTypeId": 1,
    "configValue": "10"
  }'
```

> **Nota**: Al modificar las configuraciones de contraseña, los nuevos valores se aplicarán inmediatamente a todos los nuevos registros de usuarios. Esto permite ajustar dinámicamente las políticas de seguridad sin necesidad de reiniciar la aplicación.

#### 🔄 Consumo de endpoints con Postman

A continuación se muestra cómo consumir todos los endpoints de la API utilizando Postman:

##### 👤 Registro de usuarios

> **Requisitos de contraseña**: La contraseña debe cumplir con los siguientes criterios:
> - Longitud mínima: 8 caracteres
> - Longitud máxima: 30 caracteres
> - Al menos 1 letra mayúscula
> - Al menos 1 letra minúscula
> - Al menos 1 dígito
> - Al menos 1 carácter especial
> - Caracteres especiales permitidos: -.#$%&

1. Abra Postman
2. Cree una nueva solicitud POST a `http://localhost:8080/api/users/register`
3. En la pestaña "Headers", agregue `Content-Type: application/json`
4. En la pestaña "Body", seleccione "raw" y "JSON", y agregue el siguiente contenido:
   ```json
   {
     "name": "Juan Rodriguez",
     "email": "juan@rodriguez.cl",
     "password": "Prueba.123$#-",
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

##### 🔐 Autenticación de usuarios

1. Abra Postman
2. Cree una nueva solicitud POST a `http://localhost:8080/api/auth/login`
3. En la pestaña "Headers", agregue `Content-Type: application/json`
4. En la pestaña "Body", seleccione "raw" y "JSON", y agregue el siguiente contenido:
   ```json
   {
     "email": "juan@rodriguez.cl",
     "password": "Prueba.123$#-"
   }
   ```
5. Haga clic en "Send" para enviar la solicitud

##### ⚙️ Sistema de configuración

El sistema permite personalizar los requisitos de contraseña a través de la API de configuraciones. Esto es especialmente útil para ajustar las políticas de seguridad según las necesidades específicas de su organización.

> **Configuraciones disponibles para contraseñas**:
> - `password.min.length`: Longitud mínima de caracteres (valor predeterminado: 8)
> - `password.max.length`: Longitud máxima de caracteres (valor predeterminado: 30)
> - `password.min.uppercase`: Cantidad mínima de letras mayúsculas (valor predeterminado: 1)
> - `password.min.lowercase`: Cantidad mínima de letras minúsculas (valor predeterminado: 1)
> - `password.min.digits`: Cantidad mínima de dígitos (valor predeterminado: 1)
> - `password.min.special`: Cantidad mínima de caracteres especiales (valor predeterminado: 1)
> - `password.allowed.special`: Caracteres especiales permitidos (valor predeterminado: "-.#$%&")

##### ⚙️ Obtener todas las configuraciones

1. Abra Postman
2. Cree una nueva solicitud GET a `http://localhost:8080/api/configurations`
3. En la pestaña "Headers", agregue `Authorization: Bearer YOUR_JWT_TOKEN` (reemplace YOUR_JWT_TOKEN con el token obtenido al autenticarse)
4. Haga clic en "Send" para enviar la solicitud

##### ⚙️ Obtener una configuración específica

1. Abra Postman
2. Cree una nueva solicitud GET a `http://localhost:8080/api/configurations/password.min.length`
3. En la pestaña "Headers", agregue `Authorization: Bearer YOUR_JWT_TOKEN` (reemplace YOUR_JWT_TOKEN con el token obtenido al autenticarse)
4. Haga clic en "Send" para enviar la solicitud

##### ⚙️ Actualizar una configuración

1. Abra Postman
2. Cree una nueva solicitud PUT a `http://localhost:8080/api/configurations/password.min.length?value=10`
3. En la pestaña "Headers", agregue `Authorization: Bearer YOUR_JWT_TOKEN` (reemplace YOUR_JWT_TOKEN con el token obtenido al autenticarse)
4. Haga clic en "Send" para enviar la solicitud

##### ⚙️ Actualizar una configuración usando JSON

1. Abra Postman
2. Cree una nueva solicitud PUT a `http://localhost:8080/api/configurations`
3. En la pestaña "Headers", agregue `Content-Type: application/json`
4. En la pestaña "Headers", agregue `Authorization: Bearer YOUR_JWT_TOKEN` (reemplace YOUR_JWT_TOKEN con el token obtenido al autenticarse)
5. En la pestaña "Body", seleccione "raw" y "JSON", y agregue el siguiente contenido:
   ```json
   {
     "configurationTypeId": 1,
     "configValue": "10"
   }
   ```
6. Haga clic en "Send" para enviar la solicitud

> **Nota**: Al modificar las configuraciones de contraseña, los nuevos valores se aplicarán inmediatamente a todos los nuevos registros de usuarios. Esto permite ajustar dinámicamente las políticas de seguridad sin necesidad de reiniciar la aplicación.

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
- `mapper/` - Mapeo entre entidades y DTOs
- `security/` - Configuración y utilidades de seguridad
- `utils/` - Utilidades generales

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
- 🔐 Se agregó documentación sobre las limitaciones actuales y mejoras futuras en seguridad, especificando que la generación de refresh tokens no está dentro del alcance actual del proyecto pero debería implementarse en el futuro.
- 🔧 Se implementó un controlador para la gestión de configuraciones del sistema, permitiendo editar los valores de validación de contraseñas.
- 🔄 Se crearon mappers para las entidades Configuration y ConfigurationType utilizando MapStruct.
- 🛡️ Se agregó documentación detallada sobre el manejo de excepciones y posibles mejoras futuras.
- ✅ Se implementaron utilidades para la validación de contraseñas basadas en configuraciones almacenadas en la base de datos.
- 📚 Se agregó documentación detallada sobre las utilidades de validación de contraseñas.
- 🔄 Se actualizó la documentación para reflejar el uso de H2 como base de datos en memoria, eliminando referencias a PostgreSQL, Docker y .env para la base de datos.
- 📋 Se actualizó la documentación de la base de datos para incluir información detallada sobre el esquema de la base de datos y las entidades JPA implementadas.
- 🔄 Se agregó documentación sobre el uso de MapStruct como biblioteca para el mapeo entre entidades y DTOs.
