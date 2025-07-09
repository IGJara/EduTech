package com.edutech.curso_servicio.service;

import com.edutech.curso_servicio.model.Curso;
import com.edutech.curso_servicio.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indicates that this is a service component
public class CursoService {

    private final CursoRepository cursoRepository;

    @Autowired // Dependency injection
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    /**
     * Retrieves all courses.
     * @return A list of all courses.
     */
    public List<Curso> getAllCursos() {
        return cursoRepository.findAll();
    }

    /**
     * Retrieves a course by its ID.
     * @param id The ID of the course.
     * @return An Optional containing the course if found, or empty if not.
     */
    public Optional<Curso> getCursoById(Long id) {
        return cursoRepository.findById(id);
    }

    /**
     * Creates a new course.
     * @param curso The course object to create.
     * @return The created course.
     * @throws RuntimeException if a course with the same name already exists or if essential fields are missing.
     */
    public Curso createCurso(Curso curso) {
        // Basic validation for required fields
        if (curso.getNombre() == null || curso.getNombre().trim().isEmpty() ||
            curso.getDescripcion() == null || curso.getDescripcion().trim().isEmpty() ||
            curso.getCategoria() == null || curso.getCategoria().trim().isEmpty()) {
            throw new RuntimeException("Nombre, descripción y categoría son campos obligatorios.");
        }

        // Check if a course with the same name already exists
        if (cursoRepository.findByNombre(curso.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un curso con el nombre: " + curso.getNombre());
        }
        return cursoRepository.save(curso);
    }

    /**
     * Updates an existing course.
     * @param id The ID of the course to update.
     * @param cursoDetails The updated course details.
     * @return The updated course.
     * @throws RuntimeException if the course is not found or if the new name conflicts with an existing course.
     */
    public Curso updateCurso(Long id, Curso cursoDetails) {
        return cursoRepository.findById(id).map(curso -> {
            // Basic validation for required fields
            if (cursoDetails.getNombre() == null || cursoDetails.getNombre().trim().isEmpty() ||
                cursoDetails.getDescripcion() == null || cursoDetails.getDescripcion().trim().isEmpty() ||
                cursoDetails.getCategoria() == null || cursoDetails.getCategoria().trim().isEmpty()) {
                throw new RuntimeException("Nombre, descripción y categoría son campos obligatorios.");
            }

            // Check if the new name conflicts with another existing course (excluding itself)
            Optional<Curso> existingCursoWithName = cursoRepository.findByNombre(cursoDetails.getNombre());
            if (existingCursoWithName.isPresent() && !existingCursoWithName.get().getId().equals(id)) {
                throw new RuntimeException("Ya existe otro curso con el nombre: " + cursoDetails.getNombre());
            }

            curso.setNombre(cursoDetails.getNombre());
            curso.setDescripcion(cursoDetails.getDescripcion());
            curso.setCategoria(cursoDetails.getCategoria());
            curso.setDuracionHoras(cursoDetails.getDuracionHoras());
            curso.setPrecio(cursoDetails.getPrecio());
            return cursoRepository.save(curso);
        }).orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));
    }

    /**
     * Deletes a course by its ID.
     * @param id The ID of the course to delete.
     * @throws RuntimeException if the course is not found.
     */
    public void deleteCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RuntimeException("Curso no encontrado con ID: " + id);
        }
        cursoRepository.deleteById(id);
    }

    /**
     * Finds courses by category.
     * @param categoria The category to search for.
     * @return A list of courses belonging to the specified category.
     */
    public List<Curso> getCursosByCategoria(String categoria) {
        return cursoRepository.findByCategoria(categoria);
    }
}