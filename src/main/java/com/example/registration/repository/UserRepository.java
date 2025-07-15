package com.example.registration.repository;

import com.example.registration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para la entidad User.
 * Proporciona métodos para acceder y manipular datos de usuarios en la base de datos.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param email El correo electrónico del usuario a buscar
     * @return Un Optional que contiene el usuario si existe, o vacío si no existe
     */
    Optional<User> findByUserEmail(String email);
    
    /**
     * Verifica si existe un usuario con el correo electrónico especificado.
     * 
     * @param email El correo electrónico a verificar
     * @return true si existe un usuario con ese correo, false en caso contrario
     */
    boolean existsByUserEmail(String email);
}