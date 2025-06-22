package com.edutech.inscripciones_servicio.repository; // PAQUETE CORREGIDO

import com.edutech.inscripciones_servicio.model.Inscripcion; // IMPORT CORREGIDO
import com.edutech.inscripciones_servicio.model.EstadoInscripcion; // IMPORT CORREGIDO
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByUserId(Long userId);
    List<Inscripcion> findByCursoId(Long cursoId);
    List<Inscripcion> findByUserIdAndCursoId(Long userId, Long cursoId);
    List<Inscripcion> findByStatus(EstadoInscripcion status);
}
