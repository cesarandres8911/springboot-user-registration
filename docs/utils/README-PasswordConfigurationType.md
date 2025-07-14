# 🔧 PasswordConfigurationType

## 📋 Descripción

Enumeración que representa los tipos de configuración para validación de contraseñas. Mapea los valores de `type_key` en la tabla `configuration_type` relacionados con la validación de contraseñas.

## 📝 Tipos disponibles

- `MIN_LENGTH`: Longitud mínima de caracteres para la contraseña.
- `MAX_LENGTH`: Longitud máxima de caracteres para la contraseña.
- `MIN_UPPERCASE`: Cantidad mínima de letras mayúsculas en la contraseña.
- `MIN_LOWERCASE`: Cantidad mínima de letras minúsculas en la contraseña.
- `MIN_DIGITS`: Cantidad mínima de dígitos en la contraseña.
- `MIN_SPECIAL`: Cantidad mínima de caracteres especiales en la contraseña.
- `ALLOWED_SPECIAL`: Caracteres especiales permitidos en la contraseña.

## ⚙️ Configuraciones por defecto

Si alguna configuración no está presente en la lista proporcionada, se utilizarán los siguientes valores por defecto:

- `MIN_LENGTH`: 8 caracteres
- `MAX_LENGTH`: 30 caracteres
- `MIN_UPPERCASE`: 0 (no requerido)
- `MIN_LOWERCASE`: 0 (no requerido)
- `MIN_DIGITS`: 0 (no requerido)
- `MIN_SPECIAL`: 0 (no requerido)
- `ALLOWED_SPECIAL`: "-.#$%&"

## ⚠️ Notas importantes

1. La longitud mínima no puede ser mayor que la longitud máxima.
2. Si se especifica una cantidad mínima de caracteres especiales, también se debe especificar qué caracteres especiales están permitidos.
3. Si una configuración tiene un valor no numérico cuando se espera un número, se utilizará el valor por defecto.
