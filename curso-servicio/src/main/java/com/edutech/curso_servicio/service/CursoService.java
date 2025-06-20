package com.edutech.curso_servicio.service;

import com.edutech.curso_servicio.model.Curso;
import com.edutech.curso_servicio.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio de Spring
public class CursoService {

    private final CursoRepository cursoRepository;

    // Inyección de dependencia del CursoRepository a través del constructor
    // (práctica recomendada en Spring Boot 3+).
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    // Obtiene todos los cursos disponibles
    public List<Curso> obtenerTodosLosCursos() {
        return cursoRepository.findAll();
    }

    // Obtiene un curso por su ID
    public Optional<Curso> obtenerCursoPorId(Long id) {
        return cursoRepository.findById(id);
    }

    // Crea un nuevo curso
    public Curso crearCurso(Curso curso) {
        // Validación de negocio: Asegurarse de que el nombre del curso sea único.
        // Si ya existe un curso con el mismo nombre, lanza una excepción.
        if (cursoRepository.findByNombre(curso.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un curso con el nombre: " + curso.getNombre());
        }
        // Guarda el nuevo curso en la base de datos
        return cursoRepository.save(curso);
    }

    // Actualiza los detalles de un curso existente por su ID
    public Curso actualizarCurso(Long id, Curso detallesCurso) {
        // Busca el curso por su ID. Si no se encuentra, lanza una excepción.
        return cursoRepository.findById(id)
                .map(cursoExistente -> {
                    // Actualiza los campos del curso existente con los nuevos detalles
                    cursoExistente.setNombre(detallesCurso.getNombre());
                    cursoExistente.setDescripcion(detallesCurso.getDescripcion());
                    cursoExistente.setCategoria(detallesCurso.getCategoria());
                    cursoExistente.setPrecio(detallesCurso.getPrecio());
                    cursoExistente.setFechaInicio(detallesCurso.getFechaInicio());
                    cursoExistente.setFechaFin(detallesCurso.getFechaFin());
                    cursoExistente.setNombreUsuarioProfesor(detallesCurso.getNombreUsuarioProfesor());
                    // Guarda los cambios del curso actualizado en la base de datos
                    return cursoRepository.save(cursoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));
    }

    // Elimina un curso por su ID
    public void eliminarCurso(Long id) {
        // Verifica si el curso existe antes de intentar eliminarlo.
        if (!cursoRepository.existsById(id)) {
            throw new RuntimeException("Curso no encontrado con ID: " + id);
        }
        // Elimina el curso de la base de datos
        cursoRepository.deleteById(id);
    }

    // Obtiene una lista de cursos por una categoría específica
    public List<Curso> obtenerCursosPorCategoria(String categoria) {
        return cursoRepository.findByCategoria(categoria);
    }

    // Obtiene una lista de cursos asignados a un nombre de usuario de profesor específico
    public List<Curso> obtenerCursosPorNombreUsuarioProfesor(String nombreUsuarioProfesor) {
        return cursoRepository.findByNombreUsuarioProfesor(nombreUsuarioProfesor);
    }
}
