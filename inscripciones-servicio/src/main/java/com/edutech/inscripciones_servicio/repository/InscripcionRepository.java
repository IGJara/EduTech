package com.edutech.inscripciones_servicio.repository; // PAQUETE CORREGIDO

import com.edutech.inscripciones_servicio.model.Inscripcion; // IMPORT CORREGIDO
import com.edutech.inscripciones_servicio.model.EstadoInscripcion; // IMPORT CORREGIDO
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Indica que esta interfaz es un componente de repositorio de Spring
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    // Método para buscar inscripciones por ID de usuario
    List<Inscripcion> findByUserId(Long userId); // Nombre del método corregido a findByUserId

    // Método para buscar inscripciones por ID de curso
    List<Inscripcion> findByCursoId(Long cursoId); // Nombre del método corregido a findByCursoId

    // Método para buscar inscripciones por estado
    List<Inscripcion> findByStatus(EstadoInscripcion status); // Nombre del método corregido a findByStatus

    // Método para buscar inscripciones por ID de usuario y ID de curso (para evitar duplicados)
    List<Inscripcion> findByUserIdAndCursoId(Long userId, Long cursoId); // Nombre del método y parámetros corregidos

}
