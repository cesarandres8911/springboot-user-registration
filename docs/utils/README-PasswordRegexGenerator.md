# PasswordRegexGenerator

## Descripción

Utilidad para generar expresiones regulares para validación de contraseñas basadas en configuraciones almacenadas en la base de datos.

## Métodos principales

- `generateRegex(List<Configuration> configurations)`: Genera una expresión regular para validar contraseñas basada en una lista de configuraciones.

## Uso

Esta utilidad se utiliza internamente por el `PasswordValidator` para generar expresiones regulares dinámicas basadas en las configuraciones de contraseñas almacenadas en la base de datos.

## Expresiones regulares generadas

Las expresiones regulares generadas por esta utilidad validan que la contraseña:

1. Tenga una longitud entre el mínimo y máximo configurados
2. Contenga al menos la cantidad mínima configurada de letras mayúsculas
3. Contenga al menos la cantidad mínima configurada de letras minúsculas
4. Contenga al menos la cantidad mínima configurada de dígitos
5. Contenga al menos la cantidad mínima configurada de caracteres especiales
6. Contenga solo caracteres permitidos (letras, dígitos y los caracteres especiales especificados)

## Notas importantes

1. La expresión regular generada valida que la contraseña contenga solo caracteres permitidos (letras, dígitos y los caracteres especiales especificados).
2. Si se especifica una cantidad mínima de caracteres especiales, también se debe especificar qué caracteres especiales están permitidos.