package com.edutech.usuario_servicio.repository;

import com.edutech.usuario_servicio.model.Usuario;
import com.edutech.usuario_servicio.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Indica que esta interfaz es un componente de repositorio de Spring Data JPA
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // JpaRepository proporciona métodos CRUD básicos.

    // Busca un usuario por su dirección de email
    Optional<Usuario> findByEmail(String email);

    // Busca usuarios por su tipo (ESTUDIANTE, PROFESOR, ADMINISTRADOR)
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);

    // Busca usuarios por nombre y apellido
    List<Usuario> findByNombreContainingIgnoreCaseAndApellidoContainingIgnoreCase(String nombre, String apellido);
}
