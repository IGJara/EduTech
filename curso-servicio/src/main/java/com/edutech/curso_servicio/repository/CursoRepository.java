package com.edutech.curso_servicio.repository;

import com.edutech.curso_servicio.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository // Indicates that this is a repository component
public interface CursoRepository extends JpaRepository<Curso, Long> {
    // Basic CRUD methods are inherited from JpaRepository.

    // Custom query method: find a course by its name
    Optional<Curso> findByNombre(String nombre);

    // Custom query method: find courses by category
    List<Curso> findByCategoria(String categoria);
}