# Documentación de la Base de Datos

## Entorno de desarrollo y pruebas
Este proyecto utiliza H2 como base de datos en memoria para desarrollo y pruebas. La configuración se encuentra en `src/main/resources/application.properties` y no requiere archivos `.env` ni variables de entorno para usuario o contraseña.

## Entorno productivo (recomendación)
Para entornos productivos, se recomienda utilizar una base de datos robusta como PostgreSQL, MySQL, Oracle, etc. En estos casos:

- **No utilice H2 en memoria para producción.**
- Gestione usuario, contraseña y otros datos sensibles mediante servicios especializados y seguros de gestión de secretos, como AWS Secrets Manager, AWS Parameter Store, Azure Key Vault, Google Secret Manager, HashiCorp Vault, entre otros.
- Configure su `application.properties` para leer los valores de estos servicios de forma segura y nunca almacene credenciales sensibles en archivos de texto plano ni en el repositorio.

## Acceso a la consola H2
Para desarrollo, acceda a la consola web de H2 en:

    http://localhost:8080/h2-console

Las credenciales y la URL de conexión están documentadas en el archivo de propiedades.

Al conectarse a la consola H2, utilice la URL `jdbc:h2:mem:testdb` en lugar de la URL por defecto `jdbc:h2:~/test` que aparece en el formulario de conexión.

## Zona horaria recomendada
Para asegurar la correcta gestión de fechas y horas en Colombia, la aplicación y la base de datos deben operar en la zona horaria `America/Bogota`.

En el archivo `application.properties` se debe incluir:

```properties
spring.jackson.time-zone=America/Bogota
```

Esto garantiza que todos los registros de auditoría y operaciones con fechas se almacenen y consulten en la zona horaria local de Colombia.

## Inicialización de la base de datos

La base de datos se inicializa automáticamente al arrancar la aplicación mediante dos archivos:

1. **schema.sql**: Define la estructura de las tablas (DDL - Data Definition Language)
   - Ubicación: `src/main/resources/schema.sql`
   - Crea explícitamente el esquema PUBLIC (esquema por defecto en H2) con `CREATE SCHEMA IF NOT EXISTS PUBLIC;`
   - Establece el esquema activo con `SET SCHEMA PUBLIC;`
   - Contiene las sentencias CREATE TABLE para crear la estructura de la base de datos
   - Se ejecuta primero durante la inicialización

2. **data.sql**: Contiene los datos iniciales para poblar las tablas (DML - Data Manipulation Language)
   - Ubicación: `src/main/resources/data.sql`
   - Establece el esquema activo con `SET SCHEMA PUBLIC;` para asegurar que los datos se insertan en el esquema correcto
   - Contiene las sentencias INSERT para cargar datos iniciales
   - Se ejecuta después de schema.sql
   - Actualmente inserta configuraciones para validación de contraseñas

La configuración para la carga de estos archivos se encuentra en `application.properties`:
```properties
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql
```

> **Nota**: En H2, la base de datos se crea automáticamente según la URL de conexión especificada en `application.properties`. Para este proyecto, la base de datos se llama "testdb" según la URL: `jdbc:h2:mem:testdb`.

## Estructura de la base de datos

El esquema de la base de datos está definido en el archivo `src/main/resources/schema.sql` y consta de las siguientes tablas:

### Tablas principales

#### users
Almacena información de los usuarios registrados en el sistema.
- `id` (UUID): Identificador único del usuario
- `full_name` (VARCHAR): Nombre completo del usuario
- `user_email` (VARCHAR): Correo electrónico (único)
- `user_password` (VARCHAR): Contraseña encriptada
- `last_login` (TIMESTAMP): Fecha del último ingreso
- `user_token` (VARCHAR): Token de acceso API
- Campos de auditoría: `is_active`, `created_at`, `updated_at`

#### phones
Almacena los teléfonos asociados a cada usuario.
- `id` (BIGINT): Identificador único autoincrementable
- `phone_number` (VARCHAR): Número de teléfono
- `citycode` (VARCHAR): Código de ciudad
- `contrycode` (VARCHAR): Código de país
- `user_id` (UUID): Llave foránea a la tabla users
- Campos de auditoría: `is_active`, `created_at`, `updated_at`

### Tablas adicionales (preparadas para futuras funcionalidades)

#### configuration
Almacena parámetros de configuración del sistema.
- `id` (BIGINT): Identificador único autoincrementable
- `config_key` (VARCHAR): Clave de configuración (única)
- `config_value` (VARCHAR): Valor de configuración
- `description` (VARCHAR): Descripción de la configuración
- Campos de auditoría: `is_active`, `created_at`, `updated_at`

#### role
Almacena los roles de usuario (ejemplo: administrador).
- `id` (BIGINT): Identificador único autoincrementable
- `role_name` (VARCHAR): Nombre del rol (único)
- `description` (VARCHAR): Descripción del rol
- Campos de auditoría: `is_active`, `created_at`, `updated_at`

#### permission
Almacena los permisos que pueden asignarse a los roles.
- `id` (BIGINT): Identificador único autoincrementable
- `permission_name` (VARCHAR): Nombre del permiso (único)
- `description` (VARCHAR): Descripción del permiso
- Campos de auditoría: `is_active`, `created_at`, `updated_at`

#### roles_permissions
Relaciona roles con permisos (muchos a muchos).
- `role_id` (BIGINT): Llave foránea a la tabla role
- `permission_id` (BIGINT): Llave foránea a la tabla permission
- Llave primaria compuesta: (role_id, permission_id)

## Entidades JPA implementadas

Actualmente, el sistema tiene implementadas las siguientes entidades JPA:

### User
Representa un usuario en el sistema y mapea a la tabla `users`.
- Relación One-to-Many con Phone: Un usuario puede tener múltiples teléfonos.

### Phone
Representa un teléfono asociado a un usuario y mapea a la tabla `phones`.
- Relación Many-to-One con User: Múltiples teléfonos pueden pertenecer a un usuario.

Las demás tablas (configuration, role, permission, roles_permissions) están definidas en el esquema de la base de datos pero aún no tienen entidades JPA correspondientes, ya que serán implementadas en futuras versiones del sistema.

---
> H2 es solo para desarrollo y pruebas. Para producción, utilice una base de datos real y gestione las credenciales mediante servicios seguros de gestión de secretos.
