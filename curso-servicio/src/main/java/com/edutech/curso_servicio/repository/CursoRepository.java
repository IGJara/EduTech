package com.edutech.curso_servicio.repository;

import com.edutech.curso_servicio.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Indica que esta interfaz es un componente de repositorio de Spring Data JPA
public interface CursoRepository extends JpaRepository<Curso, Long> {
    // JpaRepository proporciona métodos CRUD básicos: save, findById, findAll, deleteById, etc.

    // Métodos de consulta personalizados:
    // Encuentra un curso por su nombre (campo 'nombre' en la entidad Curso)
    Optional<Curso> findByNombre(String nombre);
    // Encuentra cursos por categoría (campo 'categoria' en la entidad Curso)
    List<Curso> findByCategoria(String categoria);
    // Encuentra cursos por el nombre de usuario del profesor (campo 'nombreUsuarioProfesor' en la entidad Curso)
    List<Curso> findByNombreUsuarioProfesor(String nombreUsuarioProfesor);
}
