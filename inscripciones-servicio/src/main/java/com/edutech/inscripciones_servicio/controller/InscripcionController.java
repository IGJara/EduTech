package com.edutech.inscripciones_servicio.controller; // PAQUETE CORREGIDO

import com.edutech.inscripciones_servicio.model.Inscripcion; // IMPORT CORREGIDO
import com.edutech.inscripciones_servicio.model.EstadoInscripcion; // IMPORT CORREGIDO
import com.edutech.inscripciones_servicio.service.InscripcionService; // IMPORT CORREGIDO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap; // Necesario para Map
import java.util.Map; // Necesario para Map

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @Autowired
    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @GetMapping
    public List<Inscripcion> getAllInscripciones() {
        return inscripcionService.getAllInscripciones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> getInscripcionById(@PathVariable Long id) {
        return inscripcionService.getInscripcionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createInscripcion(@RequestBody Inscripcion inscripcion) {
        try {
            Inscripcion createdInscripcion = inscripcionService.createInscripcion(inscripcion);
            return new ResponseEntity<>(createdInscripcion, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>(); // Se usa Map para el error
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inscripcion> updateInscripcion(@PathVariable Long id, @RequestBody Inscripcion inscripcionDetails) {
        try {
            Inscripcion updatedInscripcion = inscripcionService.updateInscripcion(id, inscripcionDetails);
            return ResponseEntity.ok(updatedInscripcion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscripcion(@PathVariable Long id) {
        try {
            inscripcionService.deleteInscripcion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public List<Inscripcion> getInscripcionesByUserId(@PathVariable Long userId) {
        return inscripcionService.getInscripcionesByUserId(userId);
    }

    @GetMapping("/curso/{cursoId}")
    public List<Inscripcion> getInscripcionesByCursoId(@PathVariable Long cursoId) {
        return inscripcionService.getInscripcionesByCursoId(cursoId);
    }

    @GetMapping("/estado/{estado}")
    public List<Inscripcion> getInscripcionesByStatus(@PathVariable String estado) {
        try {
            EstadoInscripcion enrollmentStatus = EstadoInscripcion.valueOf(estado.toUpperCase());
            return inscripcionService.getInscripcionesByStatus(enrollmentStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de inscripción no válido: " + estado);
        }
    }
}
