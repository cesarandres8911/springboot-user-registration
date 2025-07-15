package com.example.registration.repository;

import com.example.registration.model.Configuration;
import com.example.registration.model.ConfigurationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Configuration.
 * Proporciona métodos para acceder y manipular datos de configuración en la base de datos.
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    /**
     * Busca configuraciones activas por su tipo de configuración.
     * 
     * @param configurationType El tipo de configuración a buscar
     * @return Lista de configuraciones activas que coinciden con el tipo especificado
     */
    List<Configuration> findByConfigurationTypeAndActiveTrue(ConfigurationType configurationType);

    /**
     * Busca todas las configuraciones activas.
     * 
     * @return Lista de todas las configuraciones activas
     */
    List<Configuration> findByActiveTrue();
}
