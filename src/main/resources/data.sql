-- Script de inicialización de datos para la base de datos H2
-- Este script inserta datos iniciales en las tablas creadas por schema.sql
-- Se ejecuta automáticamente después de schema.sql

-- Establecer el esquema por defecto (en H2 el esquema por defecto es PUBLIC)
SET SCHEMA PUBLIC;

-- Insertar tipos de configuración para validación de contraseña
INSERT INTO configuration_type (type_key, description) VALUES
('password.min.length', 'Longitud mínima de caracteres para la contraseña'),
('password.max.length', 'Longitud máxima de caracteres para la contraseña'),
('password.min.uppercase', 'Cantidad mínima de letras mayúsculas en la contraseña'),
('password.min.lowercase', 'Cantidad mínima de letras minúsculas en la contraseña'),
('password.min.digits', 'Cantidad mínima de dígitos en la contraseña'),
('password.min.special', 'Cantidad mínima de caracteres especiales en la contraseña'),
('password.allowed.special', 'Caracteres especiales permitidos en la contraseña');

-- Insertar configuraciones asociadas a los tipos de configuración
INSERT INTO configuration (configuration_type_id, config_value) VALUES
(1, '8'),    -- password.min.length = 8
(2, '30'),   -- password.max.length = 30
(3, '1'),    -- password.min.uppercase = 1
(4, '1'),    -- password.min.lowercase = 1
(5, '1'),    -- password.min.digits = 1
(6, '1'),    -- password.min.special = 1
(7, '-.#$%&');  -- password.allowed.special (temporary placeholder)
