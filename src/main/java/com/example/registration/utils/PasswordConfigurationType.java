package com.example.registration.utils;

import lombok.Getter;

/**
 * Enumeración que representa los tipos de configuración para validación de contraseñas.
 * Esta enumeración mapea los valores de type_key en la tabla configuration_type
 * relacionados con la validación de contraseñas.
 */
@Getter
public enum PasswordConfigurationType {
    /**
     * Longitud mínima de caracteres para la contraseña.
     */
    MIN_LENGTH("password.min.length"),
    
    /**
     * Longitud máxima de caracteres para la contraseña.
     */
    MAX_LENGTH("password.max.length"),
    
    /**
     * Cantidad mínima de letras mayúsculas en la contraseña.
     */
    MIN_UPPERCASE("password.min.uppercase"),
    
    /**
     * Cantidad mínima de letras minúsculas en la contraseña.
     */
    MIN_LOWERCASE("password.min.lowercase"),
    
    /**
     * Cantidad mínima de dígitos en la contraseña.
     */
    MIN_DIGITS("password.min.digits"),
    
    /**
     * Cantidad mínima de caracteres especiales en la contraseña.
     */
    MIN_SPECIAL("password.min.special"),
    
    /**
     * Caracteres especiales permitidos en la contraseña.
     */
    ALLOWED_SPECIAL("password.allowed.special");

    /**
     * -- GETTER --
     *  Obtiene la clave del tipo de configuración.
     *
     */
    private final String typeKey;
    
    /**
     * Constructor para la enumeración.
     * 
     * @param typeKey La clave del tipo de configuración en la base de datos.
     */
    PasswordConfigurationType(String typeKey) {
        this.typeKey = typeKey;
    }

    /**
     * Obtiene el tipo de configuración a partir de su clave.
     * 
     * @param typeKey La clave del tipo de configuración.
     * @return El tipo de configuración correspondiente, o null si no existe.
     */
    public static PasswordConfigurationType fromTypeKey(String typeKey) {
        for (PasswordConfigurationType type : values()) {
            if (type.getTypeKey().equals(typeKey)) {
                return type;
            }
        }
        return null;
    }
}