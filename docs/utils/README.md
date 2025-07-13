# Utilidades de Validación de Contraseñas

Este directorio contiene utilidades para la validación de contraseñas basadas en configuraciones almacenadas en la base de datos.

## Descripción

Las utilidades de validación de contraseñas permiten generar expresiones regulares dinámicas basadas en configuraciones almacenadas en la base de datos. Estas expresiones regulares pueden utilizarse para validar contraseñas según criterios configurables como longitud mínima y máxima, cantidad mínima de letras mayúsculas, minúsculas, dígitos y caracteres especiales, así como los caracteres especiales permitidos.

## Componentes

### PasswordConfigurationType

Enumeración que representa los tipos de configuración para validación de contraseñas. Mapea los valores de `type_key` en la tabla `configuration_type` relacionados con la validación de contraseñas.

Tipos disponibles:
- `MIN_LENGTH`: Longitud mínima de caracteres para la contraseña.
- `MAX_LENGTH`: Longitud máxima de caracteres para la contraseña.
- `MIN_UPPERCASE`: Cantidad mínima de letras mayúsculas en la contraseña.
- `MIN_LOWERCASE`: Cantidad mínima de letras minúsculas en la contraseña.
- `MIN_DIGITS`: Cantidad mínima de dígitos en la contraseña.
- `MIN_SPECIAL`: Cantidad mínima de caracteres especiales en la contraseña.
- `ALLOWED_SPECIAL`: Caracteres especiales permitidos en la contraseña.

### PasswordRegexGenerator

Utilidad para generar expresiones regulares para validación de contraseñas basadas en configuraciones almacenadas en la base de datos.

Métodos principales:
- `generateRegex(List<Configuration> configurations)`: Genera una expresión regular para validar contraseñas basada en una lista de configuraciones.

### PasswordValidator

Utilidad para validar contraseñas según configuraciones almacenadas en la base de datos. Utiliza `PasswordRegexGenerator` para generar una expresión regular basada en configuraciones y luego valida contraseñas contra esa expresión.

Métodos principales:
- `isValid(String password)`: Valida si una contraseña cumple con las reglas de configuración.
- `getRegexPattern()`: Obtiene la expresión regular utilizada para validar contraseñas.

## Uso

Para utilizar estas utilidades, primero obtenga las configuraciones de la base de datos, luego cree un validador de contraseñas con esas configuraciones, y finalmente valide las contraseñas utilizando el método `isValid`.

## Configuraciones por defecto

Si alguna configuración no está presente en la lista proporcionada, se utilizarán los siguientes valores por defecto:

- `MIN_LENGTH`: 8 caracteres
- `MAX_LENGTH`: 30 caracteres
- `MIN_UPPERCASE`: 0 (no requerido)
- `MIN_LOWERCASE`: 0 (no requerido)
- `MIN_DIGITS`: 0 (no requerido)
- `MIN_SPECIAL`: 0 (no requerido)
- `ALLOWED_SPECIAL`: "-.#$%&"

## Notas importantes

1. La longitud mínima no puede ser mayor que la longitud máxima.
2. Si se especifica una cantidad mínima de caracteres especiales, también se debe especificar qué caracteres especiales están permitidos.
3. La expresión regular generada valida que la contraseña contenga solo caracteres permitidos (letras, dígitos y los caracteres especiales especificados).
4. Si una configuración tiene un valor no numérico cuando se espera un número, se utilizará el valor por defecto.
