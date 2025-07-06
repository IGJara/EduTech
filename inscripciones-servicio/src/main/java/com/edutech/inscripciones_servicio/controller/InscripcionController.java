package com.edutech.inscripciones_servicio.controller;

import com.edutech.inscripciones_servicio.model.Inscripcion;
import com.edutech.inscripciones_servicio.model.EstadoInscripcion;
import com.edutech.inscripciones_servicio.service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/inscripciones") // Define la ruta base para todos los endpoints de este controlador
public class InscripcionController {

    @Autowired // Inyecta una instancia de InscripcionService
    private InscripcionService inscripcionService;

    // Endpoint para obtener todas las inscripciones
    @GetMapping
    public ResponseEntity<List<Inscripcion>> getAllInscripciones() {
        List<Inscripcion> inscripciones = inscripcionService.getAllInscripciones();
        return new ResponseEntity<>(inscripciones, HttpStatus.OK);
    }

    // Endpoint para obtener una inscripción por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> getInscripcionById(@PathVariable Long id) {
        Optional<Inscripcion> inscripcion = inscripcionService.getInscripcionById(id);
        return inscripcion.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para crear una nueva inscripción
    @PostMapping
    public ResponseEntity<Inscripcion> createInscripcion(@RequestBody Inscripcion inscripcion) {
        try {
            Inscripcion createdInscripcion = inscripcionService.createInscripcion(inscripcion);
            return new ResponseEntity<>(createdInscripcion, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Manejo de errores específicos, por ejemplo, si la inscripción ya existe
            return new ResponseEntity<>(HttpStatus.CONFLICT); // O HttpStatus.BAD_REQUEST con un mensaje de error
        }
    }

    // Endpoint para actualizar una inscripción existente
    @PutMapping("/{id}")
    public ResponseEntity<Inscripcion> updateInscripcion(@PathVariable Long id, @RequestBody Inscripcion inscripcionDetails) {
        try {
            Inscripcion updatedInscripcion = inscripcionService.updateInscripcion(id, inscripcionDetails);
            return new ResponseEntity<>(updatedInscripcion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para eliminar una inscripción por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscripcion(@PathVariable Long id) {
        try {
            inscripcionService.deleteInscripcion(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para obtener inscripciones por ID de usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Inscripcion>> getInscripcionesByUserId(@PathVariable Long userId) {
        List<Inscripcion> inscripciones = inscripcionService.getInscripcionesByUserId(userId);
        if (inscripciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inscripciones, HttpStatus.OK);
    }

    // Endpoint para obtener inscripciones por ID de curso
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Inscripcion>> getInscripcionesByCursoId(@PathVariable Long cursoId) {
        List<Inscripcion> inscripciones = inscripcionService.getInscripcionesByCursoId(cursoId);
        if (inscripciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inscripciones, HttpStatus.OK);
    }

    // Endpoint para obtener inscripciones por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Inscripcion>> getInscripcionesByStatus(@PathVariable String status) {
        try {
            EstadoInscripcion estadoEnum = EstadoInscripcion.valueOf(status.toUpperCase());
            List<Inscripcion> inscripciones = inscripcionService.getInscripcionesByStatus(estadoEnum);
            if (inscripciones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(inscripciones, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Maneja el caso donde el estado proporcionado no es válido
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
