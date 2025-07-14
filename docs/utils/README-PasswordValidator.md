# PasswordValidator

## Descripción

Utilidad para validar contraseñas según configuraciones almacenadas en la base de datos. Utiliza `PasswordRegexGenerator` para generar una expresión regular basada en configuraciones y luego valida contraseñas contra esa expresión.

## Métodos principales

- `isValid(String password)`: Valida si una contraseña cumple con las reglas de configuración.
- `getRegexPattern()`: Obtiene la expresión regular utilizada para validar contraseñas.

## Uso

Para utilizar las utilidades de validación, primero obtenga las configuraciones de la base de datos, luego cree un validador de contraseñas con esas configuraciones, y finalmente valide las contraseñas utilizando el método `isValid`.

Ejemplo de uso:

    // Obtener configuraciones de la base de datos
    List<Configuration> configurations = configurationRepository.findAll();

    // Crear validador de contraseñas
    PasswordValidator validator = new PasswordValidator(configurations);

    // Validar contraseña
    String password = "Contraseña123!";
    boolean isValid = validator.isValid(password);

    if (isValid) {
        System.out.println("La contraseña es válida");
    } else {
        System.out.println("La contraseña no cumple con las reglas de configuración");
    }

## Reglas de validación

El validador verifica que la contraseña cumpla con todas las siguientes reglas:

1. Tenga una longitud entre el mínimo y máximo configurados
2. Contenga al menos la cantidad mínima configurada de letras mayúsculas
3. Contenga al menos la cantidad mínima configurada de letras minúsculas
4. Contenga al menos la cantidad mínima configurada de dígitos
5. Contenga al menos la cantidad mínima configurada de caracteres especiales
6. Contenga solo caracteres permitidos (letras, dígitos y los caracteres especiales especificados)

## Notas importantes

1. La longitud mínima no puede ser mayor que la longitud máxima.
2. Si se especifica una cantidad mínima de caracteres especiales, también se debe especificar qué caracteres especiales están permitidos.
3. Si una configuración tiene un valor no numérico cuando se espera un número, se utilizará el valor por defecto.
