package com.example.registration.repository;

import com.example.registration.model.ConfigurationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad ConfigurationType.
 * Proporciona métodos para acceder y manipular datos de tipos de configuración en la base de datos.
 */
@Repository
public interface ConfigurationTypeRepository extends JpaRepository<ConfigurationType, Long> {

    /**
     * Busca un tipo de configuración por su clave.
     * 
     * @param typeKey La clave del tipo de configuración a buscar
     * @return Un Optional que contiene el tipo de configuración si existe, o vacío si no existe
     */
    Optional<ConfigurationType> findByTypeKey(String typeKey);

    /**
     * Verifica si existe un tipo de configuración con la clave especificada.
     * 
     * @param typeKey La clave del tipo de configuración a verificar
     * @return true si existe un tipo de configuración con esa clave, false en caso contrario
     */
    @SuppressWarnings("unused")
    boolean existsByTypeKey(String typeKey);

    /**
     * Busca tipos de configuración activos.
     * 
     * @return Lista de todos los tipos de configuración activos
     */
    @SuppressWarnings("unused")
    java.util.List<ConfigurationType> findByIsActiveTrue();
}
