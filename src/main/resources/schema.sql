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

-- Tabla: phones
-- Almacena los teléfonos asociados a cada usuario
CREATE TABLE IF NOT EXISTS phones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Identificador único
    phone_number VARCHAR(20) NOT NULL,          -- Número de teléfono
    city_code VARCHAR(10) NOT NULL,        -- Código de ciudad
    country_code VARCHAR(10) NOT NULL,      -- Código de país
    user_id UUID,                         -- Llave foránea a users
    FOREIGN KEY (user_id) REFERENCES users(id),

    -- Campos de auditoría
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: configuration
-- Almacena parámetros de configuración del sistema
CREATE TABLE IF NOT EXISTS configuration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Identificador único
    config_key VARCHAR(100) NOT NULL UNIQUE,     -- Clave de configuración
    config_value VARCHAR(255) NOT NULL,          -- Valor de configuración
    description VARCHAR(255),             -- Descripción de la configuración

    -- Campos de auditoría
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: role
-- Almacena los roles de usuario (ejemplo: administrador)
CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Identificador único
    role_name VARCHAR(50) NOT NULL UNIQUE,     -- Nombre del rol
    description VARCHAR(255),             -- Descripción del rol

    -- Campos de auditoría
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: permission
-- Almacena los permisos que pueden asignarse a los roles
CREATE TABLE IF NOT EXISTS permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Identificador único
    permission_name VARCHAR(100) NOT NULL UNIQUE,    -- Nombre del permiso
    description VARCHAR(255),             -- Descripción del permiso

    -- Campos de auditoría
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: roles_permissions
-- Relaciona roles con permisos (muchos a muchos)
CREATE TABLE IF NOT EXISTS roles_permissions (
    role_id BIGINT NOT NULL,              -- Llave foránea a role
    permission_id BIGINT NOT NULL,        -- Llave foránea a permission
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role(id),
    FOREIGN KEY (permission_id) REFERENCES permission(id)
);
