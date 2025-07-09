package com.edutech.curso_servicio.controller;

import com.edutech.curso_servicio.model.Curso;
import com.edutech.curso_servicio.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController // Indicates that this is a REST controller
@RequestMapping("/api/cursos") // Base path for course endpoints
public class CursoController {

    private final CursoService cursoService;

    @Autowired // Dependency injection
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    /**
     * GET /api/cursos
     * Retrieves all courses.
     * @return A list of all courses with HTTP status OK.
     */
    @GetMapping
    public ResponseEntity<List<Curso>> getAllCursos() {
        List<Curso> cursos = cursoService.getAllCursos();
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }

    /**
     * GET /api/cursos/{id}
     * Retrieves a course by its ID.
     * @param id The ID of the course.
     * @return The course if found with HTTP status OK, or NOT_FOUND if not.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Curso> getCursoById(@PathVariable Long id) {
        return cursoService.getCursoById(id)
                .map(curso -> new ResponseEntity<>(curso, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * POST /api/cursos
     * Creates a new course.
     * @param curso The course object to create.
     * @return The created course with HTTP status CREATED, or BAD_REQUEST if an error occurs.
     */
    @PostMapping
    public ResponseEntity<?> createCurso(@RequestBody Curso curso) {
        try {
            Curso createdCurso = cursoService.createCurso(curso);
            return new ResponseEntity<>(createdCurso, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * PUT /api/cursos/{id}
     * Updates an existing course.
     * @param id The ID of the course to update.
     * @param cursoDetails The updated course details.
     * @return The updated course with HTTP status OK, or NOT_FOUND/BAD_REQUEST if an error occurs.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCurso(@PathVariable Long id, @RequestBody Curso cursoDetails) {
        try {
            Curso updatedCurso = cursoService.updateCurso(id, cursoDetails);
            return new ResponseEntity<>(updatedCurso, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            if (e.getMessage().contains("no encontrado")) {
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * DELETE /api/cursos/{id}
     * Deletes a course by its ID.
     * @param id The ID of the course to delete.
     * @return HTTP status NO_CONTENT if successful, or NOT_FOUND if the course is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCurso(@PathVariable Long id) {
        try {
            cursoService.deleteCurso(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /api/cursos/categoria/{categoria}
     * Retrieves courses by category.
     * @param categoria The category to search for.
     * @return A list of courses in the specified category with HTTP status OK.
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Curso>> getCursosByCategoria(@PathVariable String categoria) {
        List<Curso> cursos = cursoService.getCursosByCategoria(categoria);
        return new ResponseEntity<>(cursos, HttpStatus.OK);
    }
}