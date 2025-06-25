package com.edutech.usuario_servicio.repository; // Paquete correcto para coincidir con la carpeta: usuario_servicio

import com.edutech.usuario_servicio.model.Usuario; // Importa la clase Usuario (coincide con Usuario.java)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Indica que esta interfaz es un componente de repositorio de Spring
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { // INTERFACE: UsuarioRepository (coincide con UsuarioRepository.java)
    // JpaRepository proporciona métodos CRUD básicos: save, findById, findAll, deleteById, etc.

    // Métodos de consulta personalizados si los necesitas
    Optional<Usuario> findByUsername(String username); // Ejemplo: encontrar usuario por nombre de usuario
    boolean existsByUsername(String username); // Verifica si un username ya existe
    boolean existsByEmail(String email); // Verifica si un email ya existe
}
