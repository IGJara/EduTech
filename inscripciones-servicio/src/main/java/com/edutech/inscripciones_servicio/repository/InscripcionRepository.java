package com.edutech.inscripcion_servicio.repository;

import com.edutech.inscripcion_servicio.model.Inscripcion;
import com.edutech.inscripcion_servicio.model.EstadoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Indica que esta interfaz es un componente de repositorio de Spring Data JPA
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    // Métodos de consulta personalizados definidos por Spring Data JPA

    // Busca todas las inscripciones asociadas a un ID de usuario específico
    List<Inscripcion> findByIdUsuario(Long idUsuario);
    // Busca todas las inscripciones asociadas a un ID de curso específico
    List<Inscripcion> findByIdCurso(Long idCurso);
    // Busca inscripciones por ID de usuario y ID de curso
    List<Inscripcion> findByIdUsuarioAndIdCurso(Long idUsuario, Long idCurso);
    // Busca inscripciones por un estado de inscripción específico
    List<Inscripcion> findByEstado(EstadoInscripcion estado);
}
