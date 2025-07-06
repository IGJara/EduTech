package com.edutech.curso_servicio.controller; // Paquete actualizado para coincidir con la estructura

import com.edutech.curso_servicio.model.Curso; // Importa la clase Curso del nuevo paquete
import com.edutech.curso_servicio.service.CursoService; // Importa la clase CursoService del nuevo paquete
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/cursos") // Define la URL base para todos los endpoints de este controlador
public class CursoController {

    private final CursoService cursoService;

    // Inyección de dependencia del CursoService a través del constructor
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping // Maneja solicitudes GET a /api/cursos para obtener todos los cursos
    public List<Curso> obtenerTodosLosCursos() {
        return cursoService.obtenerTodosLosCursos();
    }

    @GetMapping("/{id}") // Maneja solicitudes GET a /api/cursos/{id} para obtener un curso por su ID
    public ResponseEntity<Curso> obtenerCursoPorId(@PathVariable Long id) {
        return cursoService.obtenerCursoPorId(id)
                .map(ResponseEntity::ok) // Si el curso se encuentra, devuelve una respuesta 200 OK con el curso
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, devuelve una respuesta 404 Not Found
    }

    @PostMapping // Maneja solicitudes POST a /api/cursos para crear un nuevo curso
    public ResponseEntity<?> crearCurso(@RequestBody Curso curso) {
        try {
            Curso cursoCreado = cursoService.crearCurso(curso);
            return new ResponseEntity<>(cursoCreado, HttpStatus.CREATED); // Devuelve una respuesta 201 Created con el curso creado
        } catch (RuntimeException e) {
            Map<String, String> respuestaError = new HashMap<>();
            respuestaError.put("error", e.getMessage());
            return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST); // Devuelve una respuesta 400 Bad Request en caso de error
        }
    }

    @PutMapping("/{id}") // Maneja solicitudes PUT a /api/cursos/{id} para actualizar un curso existente
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Long id, @RequestBody Curso detallesCurso) {
        try {
            Curso cursoActualizado = cursoService.actualizarCurso(id, detallesCurso);
            return ResponseEntity.ok(cursoActualizado); // Devuelve una respuesta 200 OK con el curso actualizado
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si el curso no se encuentra, devuelve una respuesta 404 Not Found
        }
    }

    @DeleteMapping("/{id}") // Maneja solicitudes DELETE a /api/cursos/{id} para eliminar un curso
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        try {
            cursoService.eliminarCurso(id);
            return ResponseEntity.noContent().build(); // Devuelve una respuesta 204 No Content para indicar eliminación exitosa
        }
        catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si el curso no se encuentra, devuelve una respuesta 404 Not Found
        }
    }

    @GetMapping("/categoria/{categoria}") // Maneja solicitudes GET a /api/cursos/categoria/{nombreCategoria}
    public List<Curso> obtenerCursosPorCategoria(@PathVariable String categoria) {
        return cursoService.obtenerCursosPorCategoria(categoria);
    }

    @GetMapping("/profesor/{nombreUsuarioProfesor}") // Maneja solicitudes GET a /api/cursos/profesor/{nombreUsuarioProfesor}
    public List<Curso> obtenerCursosPorNombreUsuarioProfesor(@PathVariable String nombreUsuarioProfesor) {
        return cursoService.obtenerCursosPorNombreUsuarioProfesor(nombreUsuarioProfesor);
    }
}
