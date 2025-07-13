# Utilidades de Contraseñas

Este directorio contiene utilidades para la validación y cifrado de contraseñas.

## Descripción

Las utilidades de contraseñas incluyen:

1. **Validación de contraseñas**: Permiten generar expresiones regulares dinámicas basadas en configuraciones almacenadas en la base de datos. Estas expresiones regulares pueden utilizarse para validar contraseñas según criterios configurables como longitud mínima y máxima, cantidad mínima de letras mayúsculas, minúsculas, dígitos y caracteres especiales, así como los caracteres especiales permitidos.

2. **Cifrado de contraseñas**: Proporcionan funcionalidad para cifrar contraseñas utilizando el algoritmo BCrypt de Spring Security, que es considerado una de las mejores prácticas para el almacenamiento seguro de contraseñas.

## Componentes

Este directorio contiene documentación detallada para cada una de las utilidades de contraseñas:

### [PasswordConfigurationType](README-PasswordConfigurationType.md)

Enumeración que representa los tipos de configuración para validación de contraseñas. Mapea los valores de `type_key` en la tabla `configuration_type` relacionados con la validación de contraseñas.

### [PasswordRegexGenerator](README-PasswordRegexGenerator.md)

Utilidad para generar expresiones regulares para validación de contraseñas basadas en configuraciones almacenadas en la base de datos.

### [PasswordValidator](README-PasswordValidator.md)

Utilidad para validar contraseñas según configuraciones almacenadas en la base de datos. Utiliza `PasswordRegexGenerator` para generar una expresión regular basada en configuraciones y luego valida contraseñas contra esa expresión.

### [PasswordEncoderUtil](README-PasswordEncoderUtil.md)

Utilidad para el cifrado de contraseñas utilizando BCrypt. Esta clase proporciona métodos para cifrar contraseñas y verificar si una contraseña coincide con su versión cifrada.

## Uso general

Para obtener información detallada sobre el uso de cada utilidad, consulte la documentación específica de cada componente haciendo clic en los enlaces anteriores.
