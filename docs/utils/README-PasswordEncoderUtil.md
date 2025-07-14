# PasswordEncoderUtil

## Descripción

Utilidad para el cifrado de contraseñas utilizando BCrypt. Esta clase proporciona métodos para cifrar contraseñas y verificar si una contraseña coincide con su versión cifrada. Utiliza el algoritmo BCrypt de Spring Security, que es considerado una de las mejores prácticas para el almacenamiento seguro de contraseñas.

## Métodos principales

- `encode(String rawPassword)`: Cifra una contraseña utilizando el algoritmo BCrypt.
- `matches(String rawPassword, String encodedPassword)`: Verifica si una contraseña en texto plano coincide con una contraseña cifrada.
- `passwordEncoder()`: Proporciona el bean PasswordEncoder para ser utilizado en la aplicación.

## Uso

Para utilizar las utilidades de cifrado:

1. **Inyección de dependencias**: La clase `PasswordEncoderUtil` está anotada con `@Component`, por lo que puede ser inyectada en otros componentes de Spring.

    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;

2. **Cifrado de contraseñas**: Para cifrar una contraseña, utilice el método `encode`.

    String rawPassword = "contraseña123";
    // Verificar que la contraseña no sea nula o vacía antes de cifrarla
    if (rawPassword != null && !rawPassword.isEmpty()) {
        String encodedPassword = passwordEncoderUtil.encode(rawPassword);
        // Usar encodedPassword...
    }

3. **Verificación de contraseñas**: Para verificar si una contraseña coincide con su versión cifrada, utilice el método `matches`.

    // Verificar que ni la contraseña ni el hash sean nulos antes de verificar
    if (rawPassword != null && encodedPassword != null) {
        boolean isMatch = passwordEncoderUtil.matches(rawPassword, encodedPassword);
        // Usar isMatch...
    }

4. **Uso directo del PasswordEncoder**: También puede inyectar directamente el bean `PasswordEncoder` en sus componentes.

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Cifrar (verificando que la contraseña no sea nula)
    if (rawPassword != null && !rawPassword.isEmpty()) {
        String encoded = passwordEncoder.encode(rawPassword);

        // Verificar (verificando que ni la contraseña ni el hash sean nulos)
        if (rawPassword != null && encoded != null) {
            boolean matches = passwordEncoder.matches(rawPassword, encoded);
            // Usar matches...
        }
    }

## Características de seguridad

- Utiliza el algoritmo BCrypt, que incluye automáticamente un "salt" aleatorio para cada contraseña.
- Protege contra ataques de fuerza bruta mediante un factor de trabajo configurable.
- Las contraseñas cifradas no pueden ser descifradas, solo verificadas.
- Cada vez que se cifra la misma contraseña, se genera un hash diferente debido al salt aleatorio.

## Proceso de verificación durante el login

Cuando un usuario intenta iniciar sesión, el sistema sigue estos pasos para verificar la contraseña:

1. El usuario proporciona su nombre de usuario y contraseña en texto plano.
2. El sistema recupera el hash de la contraseña almacenado en la base de datos para ese usuario.
3. El sistema utiliza el método `matches(rawPassword, encodedPassword)` para verificar si la contraseña proporcionada coincide con el hash almacenado.
4. El método `matches` no descifra el hash almacenado, sino que aplica el mismo algoritmo de hash a la contraseña proporcionada y compara los resultados.

Este proceso es seguro porque:
- La contraseña original nunca se almacena en la base de datos.
- No es necesario descifrar el hash para verificar la contraseña.
- El algoritmo BCrypt está diseñado específicamente para esta verificación unidireccional.

## Notas importantes

1. Las contraseñas nulas o vacías generarán una excepción `IllegalArgumentException` al intentar cifrarlas.
2. El método `matches` devolverá `false` si alguno de los parámetros es nulo.
3. El cifrado BCrypt es computacionalmente intensivo por diseño, lo que proporciona protección contra ataques de fuerza bruta.
