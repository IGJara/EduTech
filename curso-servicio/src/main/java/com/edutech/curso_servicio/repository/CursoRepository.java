package com.edutech.curso_servicio.repository; // Paquete actualizado para coincidir con la estructura

import com.edutech.curso_servicio.model.Curso; // Importa la clase Curso del nuevo paquete
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Indica que esta interfaz es un componente de repositorio de Spring Data JPA
public interface CursoRepository extends JpaRepository<Curso, Long> {
    // JpaRepository proporciona métodos CRUD básicos: save, findById, findAll, deleteById, etc.

    // Métodos de consulta personalizados:
    Optional<Curso> findByNombre(String nombre); // Encuentra un curso por su nombre (campo 'nombre')
    List<Curso> findByCategoria(String categoria); // Encuentra cursos por categoría (campo 'categoria')
    List<Curso> findByNombreUsuarioProfesor(String nombreUsuarioProfesor); // Encuentra cursos de un profesor específico (campo 'nombreUsuarioProfesor')
}
