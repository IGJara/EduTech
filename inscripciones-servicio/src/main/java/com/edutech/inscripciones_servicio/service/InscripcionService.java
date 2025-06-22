package com.edutech.inscripciones_servicio.service; // PAQUETE CORREGIDO

import com.edutech.inscripciones_servicio.model.Inscripcion; // IMPORT CORREGIDO
import com.edutech.inscripciones_servicio.model.EstadoInscripcion; // IMPORT CORREGIDO
import com.edutech.inscripciones_servicio.repository.InscripcionRepository; // IMPORT CORREGIDO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;

    @Autowired
    public InscripcionService(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    public List<Inscripcion> getAllInscripciones() {
        return inscripcionRepository.findAll();
    }

    public Optional<Inscripcion> getInscripcionById(Long id) {
        return inscripcionRepository.findById(id);
    }

    public Inscripcion createInscripcion(Inscripcion inscripcion) {
        if (!inscripcionRepository.findByUserIdAndCursoId(inscripcion.getUserId(), inscripcion.getCursoId()).isEmpty()) {
            throw new RuntimeException("El usuario ya está inscrito en este curso.");
        }
        if (inscripcion.getFechaInscripcion() == null) {
            inscripcion.setFechaInscripcion(LocalDate.now());
        }
        if (inscripcion.getStatus() == null) {
            inscripcion.setStatus(EstadoInscripcion.ACTIVA);
        }
        return inscripcionRepository.save(inscripcion);
    }

    public Inscripcion updateInscripcion(Long id, Inscripcion inscripcionDetails) {
        return inscripcionRepository.findById(id)
                .map(inscripcion -> {
                    inscripcion.setUserId(inscripcionDetails.getUserId());
                    inscripcion.setCursoId(inscripcionDetails.getCursoId());
                    inscripcion.setFechaInscripcion(inscripcionDetails.getFechaInscripcion());
                    inscripcion.setStatus(inscripcionDetails.getStatus());
                    return inscripcionRepository.save(inscripcion);
                })
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + id));
    }

    public void deleteInscripcion(Long id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new RuntimeException("Inscripción no encontrada con ID: " + id);
        }
        inscripcionRepository.deleteById(id);
    }

    public List<Inscripcion> getInscripcionesByUserId(Long userId) {
        return inscripcionRepository.findByUserId(userId);
    }

    public List<Inscripcion> getInscripcionesByCursoId(Long cursoId) {
        return inscripcionRepository.findByCursoId(cursoId);
    }

    public List<Inscripcion> getInscripcionesByStatus(EstadoInscripcion status) {
        return inscripcionRepository.findByStatus(status);
    }
}
