# Documentación de la Base de Datos

## Descripción
Este proyecto utiliza H2 como sistema de gestión de base de datos en memoria, ideal para desarrollo y pruebas.

## Configuración
- **Base de datos:** H2 (en memoria)
- **Persistencia:** No persistente por defecto. Los datos se pierden al reiniciar la aplicación.
- **Configuración:** Toda la configuración relevante se encuentra en `src/main/resources/application.properties`.

## Acceso a la consola H2
Una vez levantada la aplicación, puede acceder a la consola web de H2 en:

    http://localhost:8080/h2-console

Las credenciales y la URL de conexión están documentadas en el archivo de propiedades.

## Modelo de la base de datos

El modelo inicial contempla las siguientes entidades principales:
- **User**: Representa a los usuarios registrados.
- **Phone**: Representa los teléfonos asociados a cada usuario.
- **PasswordPattern**: Tabla para almacenar el patrón de validación de contraseñas configurable en tiempo de ejecución.

---
> No es necesario levantar contenedores Docker ni configurar archivos `.env` para la base de datos H2.
