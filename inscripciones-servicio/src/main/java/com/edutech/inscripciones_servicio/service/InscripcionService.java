package Edutech.inscripciones-servicio.src.main.java.com.edutech.inscripciones_servicio.service;

public class InscripcionService {
    
}package com.edutech.inscripcion_servicio.controller;

import com.edutech.inscripcion_servicio.model.Inscripcion;
import com.edutech.inscripcion_servicio.model.EstadoInscripcion;
import com.edutech.inscripcion_servicio.service.InscripcionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/inscripciones") // Define la URL base para todos los endpoints de este controlador
public class InscripcionController {

    private final InscripcionService inscripcionService;

    // Inyección de dependencia del InscripcionService a través del constructor
    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @GetMapping // Maneja solicitudes GET a /api/inscripciones para obtener todas las inscripciones
    public List<Inscripcion> obtenerTodasLasInscripciones() {
        return inscripcionService.obtenerTodasLasInscripciones();
    }

    @GetMapping("/{id}") // Maneja solicitudes GET a /api/inscripciones/{id} para obtener una inscripción por su ID
    public ResponseEntity<Inscripcion> obtenerInscripcionPorId(@PathVariable Long id) {
        return inscripcionService.obtenerInscripcionPorId(id)
                .map(ResponseEntity::ok) // Si la inscripción se encuentra, devuelve una respuesta 200 OK con la inscripción
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, devuelve una respuesta 404 Not Found
    }

    @PostMapping // Maneja solicitudes POST a /api/inscripciones para crear una nueva inscripción
    public ResponseEntity<?> crearInscripcion(@RequestBody Inscripcion inscripcion) {
        try {
            Inscripcion inscripcionCreada = inscripcionService.crearInscripcion(inscripcion);
            return new ResponseEntity<>(inscripcionCreada, HttpStatus.CREATED); // Devuelve una respuesta 201 Created con la inscripción creada
        } catch (RuntimeException e) {
            Map<String, String> respuestaError = new HashMap<>();
            respuestaError.put("error", e.getMessage());
            return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST); // Devuelve una respuesta 400 Bad Request en caso de error
        }
    }

    @PutMapping("/{id}") // Maneja solicitudes PUT a /api/inscripciones/{id} para actualizar una inscripción existente
    public ResponseEntity<Inscripcion> actualizarInscripcion(@PathVariable Long id, @RequestBody Inscripcion detallesInscripcion) {
        try {
            Inscripcion inscripcionActualizada = inscripcionService.actualizarInscripcion(id, detallesInscripcion);
            return ResponseEntity.ok(inscripcionActualizada); // Devuelve una respuesta 200 OK con la inscripción actualizada
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si la inscripción no se encuentra, devuelve una respuesta 404 Not Found
        }
    }

    @DeleteMapping("/{id}") // Maneja solicitudes DELETE a /api/inscripciones/{id} para eliminar una inscripción
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        try {
            inscripcionService.eliminarInscripcion(id);
            return ResponseEntity.noContent().build(); // Devuelve una respuesta 204 No Content para indicar eliminación exitosa
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si la inscripción no se encuentra, devuelve una respuesta 404 Not Found
        }
    }

    @GetMapping("/usuario/{idUsuario}") // Maneja solicitudes GET a /api/inscripciones/usuario/{idUsuario}
    public List<Inscripcion> obtenerInscripcionesPorIdUsuario(@PathVariable Long idUsuario) {
        return inscripcionService.obtenerInscripcionesPorIdUsuario(idUsuario);
    }

    @GetMapping("/curso/{idCurso}") // Maneja solicitudes GET a /api/inscripciones/curso/{idCurso}
    public List<Inscripcion> obtenerInscripcionesPorIdCurso(@PathVariable Long idCurso) {
        return inscripcionService.obtenerInscripcionesPorIdCurso(idCurso);
    }

    @GetMapping("/estado/{estado}") // Maneja solicitudes GET a /api/inscripciones/estado/{estado}
    public List<Inscripcion> obtenerInscripcionesPorEstado(@PathVariable String estado) {
        try {
            EstadoInscripcion estadoInscripcion = EstadoInscripcion.valueOf(estado.toUpperCase());
            return inscripcionService.obtenerInscripcionesPorEstado(estadoInscripcion);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de inscripción no válido: " + estado);
        }
    }
}
