package com.edutech.inscripciones_servicio.service;

import com.edutech.inscripciones_servicio.model.Inscripcion;
import com.edutech.inscripciones_servicio.model.EstadoInscripcion;
import com.edutech.inscripciones_servicio.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio de Spring
public class InscripcionService {

    @Autowired // Inyecta una instancia de InscripcionRepository
    private InscripcionRepository inscripcionRepository;

    // Obtiene todas las inscripciones
    public List<Inscripcion> getAllInscripciones() {
        return inscripcionRepository.findAll();
    }

    // Obtiene una inscripción por su ID
    public Optional<Inscripcion> getInscripcionById(Long id) {
        return inscripcionRepository.findById(id);
    }

    // Crea una nueva inscripción
    public Inscripcion createInscripcion(Inscripcion inscripcion) {
        // Verifica si ya existe una inscripción para el mismo usuario y curso
        List<Inscripcion> existingInscripciones = inscripcionRepository.findByUserIdAndCursoId(
                inscripcion.getUserId(), inscripcion.getCursoId()); // Uso de getUserId() y getCursoId()

        if (!existingInscripciones.isEmpty()) {
            throw new RuntimeException("El usuario ya está inscrito en este curso.");
        }

        // Establece la fecha de inscripción si no está definida
        if (inscripcion.getFechaInscripcion() == null) {
            inscripcion.setFechaInscripcion(LocalDate.now());
        }
        // Establece el estado inicial como ACTIVA por defecto
        if (inscripcion.getStatus() == null) {
            inscripcion.setStatus(EstadoInscripcion.ACTIVA); // Uso de setStatus()
        }
        return inscripcionRepository.save(inscripcion);
    }

    // Actualiza una inscripción existente
    public Inscripcion updateInscripcion(Long id, Inscripcion inscripcionDetails) {
        // Busca la inscripción por ID
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + id));

        // Actualiza los campos relevantes
        inscripcion.setUserId(inscripcionDetails.getUserId()); // Uso de setUserId() y getUserId()
        inscripcion.setCursoId(inscripcionDetails.getCursoId()); // Uso de setCursoId() y getCursoId()
        inscripcion.setFechaInscripcion(inscripcionDetails.getFechaInscripcion());
        inscripcion.setStatus(inscripcionDetails.getStatus()); // Uso de setStatus() y getStatus()

        // Guarda y devuelve la inscripción actualizada
        return inscripcionRepository.save(inscripcion);
    }

    // Elimina una inscripción por su ID
    public void deleteInscripcion(Long id) {
        // Verifica si la inscripción existe antes de eliminarla
        if (!inscripcionRepository.existsById(id)) {
            throw new RuntimeException("Inscripción no encontrada con ID: " + id);
        }
        inscripcionRepository.deleteById(id);
    }

    // Métodos para buscar inscripciones por atributos específicos

    // Busca inscripciones por ID de usuario
    public List<Inscripcion> getInscripcionesByUserId(Long userId) {
        return inscripcionRepository.findByUserId(userId); // Uso de findByUserId()
    }

    // Busca inscripciones por ID de curso
    public List<Inscripcion> getInscripcionesByCursoId(Long cursoId) {
        return inscripcionRepository.findByCursoId(cursoId); // Uso de findByCursoId()
    }

    // Busca inscripciones por estado
    public List<Inscripcion> getInscripcionesByStatus(EstadoInscripcion status) {
        return inscripcionRepository.findByStatus(status); // Uso de findByStatus()
    }
}
