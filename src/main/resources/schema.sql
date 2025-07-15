-- Script de inicialización del esquema para la base de datos H2
-- Este script crea las tablas principales para el sistema de registro de usuarios
-- Toda la documentación está en español, los nombres de tablas y columnas en inglés

-- Crear esquema si no existe (en H2 el esquema por defecto es PUBLIC)
CREATE SCHEMA IF NOT EXISTS PUBLIC;

-- Establecer el esquema por defecto
SET SCHEMA PUBLIC;

-- Tabla: users
-- Almacena información de los usuarios
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,                  -- Identificador único (UUID)
    full_name VARCHAR(100) NOT NULL,      -- Nombre completo del usuario
    user_email VARCHAR(100) NOT NULL UNIQUE,   -- Correo electrónico
    user_password VARCHAR(255) NOT NULL,       -- Contraseña (encriptada)
    last_login TIMESTAMP NOT NULL,        -- Fecha del último ingreso
    user_token VARCHAR(512) NOT NULL,          -- Token de acceso API

    -- Campos de auditoría
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Índice para búsquedas por email
-- Mejora el rendimiento de las consultas que buscan usuarios por su correo electrónico
CREATE INDEX IF NOT EXISTS idx_users_email ON users(user_email);

-- Índice para búsquedas por token
-- Mejora el rendimiento de las consultas que buscan usuarios por su token de acceso
CREATE INDEX IF NOT EXISTS idx_users_token ON users(user_token);

-- Tabla: phones
-- Almacena los teléfonos asociados a cada usuario
CREATE TABLE IF NOT EXISTS phones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Identificador único
    phone_number VARCHAR(20) NOT NULL,          -- Número de teléfono
    city_code VARCHAR(10) NOT NULL,        -- Código de ciudad
    country_code VARCHAR(10) NOT NULL,      -- Código de país
    user_id UUID,                         -- Referencia al usuario.

    -- Restricción de clave foránea
    FOREIGN KEY (user_id) REFERENCES users(id),

    -- Campos de auditoría
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: configuration_type
-- Almacena los tipos de configuración del sistema
CREATE TABLE IF NOT EXISTS configuration_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Identificador único
    type_key VARCHAR(100) NOT NULL UNIQUE,     -- Clave del tipo de configuración
    description VARCHAR(255),             -- Descripción del tipo de configuración

    -- Campos de auditoría
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: configuration
-- Almacena valores de configuración asociados a tipos de configuración
CREATE TABLE IF NOT EXISTS configuration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Identificador único
    configuration_type_id BIGINT NOT NULL, -- Referencia al tipo de configuración
    config_value VARCHAR(255) NOT NULL,    -- Valor de configuración

    -- Restricción de clave foránea
    FOREIGN KEY (configuration_type_id) REFERENCES configuration_type(id),

    -- Campos de auditoría
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
