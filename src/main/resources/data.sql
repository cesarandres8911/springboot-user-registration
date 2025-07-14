-- Script de inicialización de datos para la base de datos H2
-- Este script inserta datos iniciales en las tablas creadas por schema.sql
-- Se ejecuta automáticamente después de schema.sql

-- Establecer el esquema por defecto (en H2 el esquema por defecto es PUBLIC)
SET SCHEMA PUBLIC;

-- Insertar tipos de configuración para validación de contraseña solo si no existen
MERGE INTO configuration_type (type_key, description, is_active, created_at, updated_at) KEY(type_key) VALUES
('password.min.length', 'Longitud mínima de caracteres para la contraseña', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('password.max.length', 'Longitud máxima de caracteres para la contraseña', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('password.min.uppercase', 'Cantidad mínima de letras mayúsculas en la contraseña', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('password.min.lowercase', 'Cantidad mínima de letras minúsculas en la contraseña', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('password.min.digits', 'Cantidad mínima de dígitos en la contraseña', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('password.min.special', 'Cantidad mínima de caracteres especiales en la contraseña', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('password.allowed.special', 'Caracteres especiales permitidos en la contraseña', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insertar configuraciones asociadas a los tipos de configuración solo si no existen
-- Primero, asegurarse de que los IDs de configuration_type existen
-- Usar sub-consultas para obtener los IDs basados en type_key
INSERT INTO configuration (configuration_type_id, config_value, is_active, created_at, updated_at)
SELECT ct.id, '8', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()
FROM configuration_type ct 
WHERE ct.type_key = 'password.min.length'
AND NOT EXISTS (SELECT 1 FROM configuration c WHERE c.configuration_type_id = ct.id);

INSERT INTO configuration (configuration_type_id, config_value, is_active, created_at, updated_at)
SELECT ct.id, '30', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()
FROM configuration_type ct 
WHERE ct.type_key = 'password.max.length'
AND NOT EXISTS (SELECT 1 FROM configuration c WHERE c.configuration_type_id = ct.id);

INSERT INTO configuration (configuration_type_id, config_value, is_active, created_at, updated_at)
SELECT ct.id, '1', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()
FROM configuration_type ct 
WHERE ct.type_key = 'password.min.uppercase'
AND NOT EXISTS (SELECT 1 FROM configuration c WHERE c.configuration_type_id = ct.id);

INSERT INTO configuration (configuration_type_id, config_value, is_active, created_at, updated_at)
SELECT ct.id, '1', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()
FROM configuration_type ct 
WHERE ct.type_key = 'password.min.lowercase'
AND NOT EXISTS (SELECT 1 FROM configuration c WHERE c.configuration_type_id = ct.id);

INSERT INTO configuration (configuration_type_id, config_value, is_active, created_at, updated_at)
SELECT ct.id, '1', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()
FROM configuration_type ct 
WHERE ct.type_key = 'password.min.digits'
AND NOT EXISTS (SELECT 1 FROM configuration c WHERE c.configuration_type_id = ct.id);

INSERT INTO configuration (configuration_type_id, config_value, is_active, created_at, updated_at)
SELECT ct.id, '1', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()
FROM configuration_type ct 
WHERE ct.type_key = 'password.min.special'
AND NOT EXISTS (SELECT 1 FROM configuration c WHERE c.configuration_type_id = ct.id);

INSERT INTO configuration (configuration_type_id, config_value, is_active, created_at, updated_at)
SELECT ct.id, '-.#$%&', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()
FROM configuration_type ct 
WHERE ct.type_key = 'password.allowed.special'
AND NOT EXISTS (SELECT 1 FROM configuration c WHERE c.configuration_type_id = ct.id);
