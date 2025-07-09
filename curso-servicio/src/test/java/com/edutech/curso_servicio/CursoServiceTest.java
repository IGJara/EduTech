package com.edutech.curso_servicio; // Asegúrate de que el paquete coincida con tu estructura de proyecto

import com.edutech.curso_servicio.model.Curso;
import com.edutech.curso_servicio.repository.CursoRepository;
import com.edutech.curso_servicio.service.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita las extensiones de Mockito para JUnit 5
public class CursoServiceTest {

    @Mock // Mockea la dependencia CursoRepository
    private CursoRepository cursoRepository;

    @InjectMocks // Inyecta los mocks en CursoService
    private CursoService cursoService;

    private Curso curso1;
    private Curso curso2;

    @BeforeEach // Se ejecuta antes de cada método de prueba
    void setUp() {
        // Inicializa objetos Curso para usar en las pruebas
        curso1 = new Curso(1L, "Programación Java", "Curso completo de Java", "Desarrollo", 40, 199.99);
        curso2 = new Curso(2L, "Diseño Web con React", "Aprende a crear interfaces modernas", "Diseño", 30, 149.99);
    }

    // --- Pruebas para CREATE ---

    @Test
    @DisplayName("Crear curso - Éxito")
    void createCurso_Success() {
        // Configura el mock para que findByNombre devuelva vacío (no existe el curso)
        when(cursoRepository.findByNombre(curso1.getNombre())).thenReturn(Optional.empty());
        // Configura el mock para que save devuelva el curso que se va a guardar
        when(cursoRepository.save(curso1)).thenReturn(curso1);

        // Llama al método del servicio
        Curso createdCurso = cursoService.createCurso(curso1);

        // Verifica que el curso creado no sea nulo y tenga el mismo nombre
        assertNotNull(createdCurso);
        assertEquals(curso1.getNombre(), createdCurso.getNombre());
        // Verifica que findByNombre y save fueron llamados una vez
        verify(cursoRepository, times(1)).findByNombre(curso1.getNombre());
        verify(cursoRepository, times(1)).save(curso1);
    }

    @Test
    @DisplayName("Crear curso - Nombre existente")
    void createCurso_NombreExistente() {
        // Configura el mock para que findByNombre devuelva el curso (ya existe)
        when(cursoRepository.findByNombre(curso1.getNombre())).thenReturn(Optional.of(curso1));

        // Verifica que se lance una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.createCurso(curso1);
        });

        // Verifica el mensaje de la excepción
        assertEquals("Ya existe un curso con el nombre: " + curso1.getNombre(), exception.getMessage());
        // Verifica que save nunca fue llamado
        verify(cursoRepository, times(1)).findByNombre(curso1.getNombre());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    @DisplayName("Crear curso - Campos obligatorios faltantes (nombre vacío)")
    void createCurso_InvalidInput_EmptyName() {
        Curso invalidCurso = new Curso(null, " ", "Descripción válida", "Categoría", 10, 50.0);

        // Verifica que se lance una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.createCurso(invalidCurso);
        });

        // Verifica el mensaje de la excepción
        assertEquals("Nombre, descripción y categoría son campos obligatorios.", exception.getMessage());
        // Verifica que no se interactuó con el repositorio
        verify(cursoRepository, never()).findByNombre(anyString());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    @DisplayName("Crear curso - Campos obligatorios faltantes (descripción nula)")
    void createCurso_InvalidInput_NullDescription() {
        Curso invalidCurso = new Curso(null, "Nombre Válido", null, "Categoría", 10, 50.0);

        // Verifica que se lance una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.createCurso(invalidCurso);
        });

        // Verifica el mensaje de la excepción
        assertEquals("Nombre, descripción y categoría son campos obligatorios.", exception.getMessage());
        verify(cursoRepository, never()).findByNombre(anyString());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    // --- Pruebas para READ ---

    @Test
    @DisplayName("Obtener todos los cursos - Éxito")
    void getAllCursos_Success() {
        // Configura el mock para que findAll devuelva una lista de cursos
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso1, curso2));

        // Llama al método del servicio
        List<Curso> cursos = cursoService.getAllCursos();

        // Verifica que la lista no sea nula, tenga el tamaño correcto y contenga los cursos esperados
        assertNotNull(cursos);
        assertEquals(2, cursos.size());
        assertTrue(cursos.contains(curso1));
        assertTrue(cursos.contains(curso2));
        // Verifica que findAll fue llamado una vez
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener curso por ID - Encontrado")
    void getCursoById_Success() {
        // Configura el mock para que findById devuelva el curso1
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso1));

        // Llama al método del servicio
        Optional<Curso> foundCurso = cursoService.getCursoById(1L);

        // Verifica que el curso esté presente y sea el esperado
        assertTrue(foundCurso.isPresent());
        assertEquals(curso1.getNombre(), foundCurso.get().getNombre());
        // Verifica que findById fue llamado una vez
        verify(cursoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener curso por ID - No encontrado")
    void getCursoById_NotFound() {
        // Configura el mock para que findById devuelva vacío (curso no encontrado)
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        // Llama al método del servicio
        Optional<Curso> foundCurso = cursoService.getCursoById(99L);

        // Verifica que el curso no esté presente
        assertFalse(foundCurso.isPresent());
        // Verifica que findById fue llamado una vez
        verify(cursoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Obtener cursos por categoría - Éxito")
    void getCursosByCategoria_Success() {
        // Configura el mock para que findByCategoria devuelva una lista de cursos
        when(cursoRepository.findByCategoria("Desarrollo")).thenReturn(Arrays.asList(curso1));

        // Llama al método del servicio
        List<Curso> cursos = cursoService.getCursosByCategoria("Desarrollo");

        // Verifica que la lista no sea nula, tenga el tamaño correcto y contenga el curso esperado
        assertNotNull(cursos);
        assertEquals(1, cursos.size());
        assertTrue(cursos.contains(curso1));
        assertFalse(cursos.contains(curso2));
        // Verifica que findByCategoria fue llamado una vez
        verify(cursoRepository, times(1)).findByCategoria("Desarrollo");
    }

    // --- Pruebas para UPDATE ---

    @Test
    @DisplayName("Actualizar curso - Éxito")
    void updateCurso_Success() {
        Curso updatedDetails = new Curso(1L, "Programación Java Avanzada", "Curso actualizado", "Desarrollo", 50, 249.99);

        // Configura el mock para findById (encuentra el curso original)
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso1));
        // Configura el mock para findByNombre (no hay conflicto de nombre)
        when(cursoRepository.findByNombre(updatedDetails.getNombre())).thenReturn(Optional.empty());
        // Configura el mock para save (devuelve el curso actualizado)
        when(cursoRepository.save(any(Curso.class))).thenReturn(updatedDetails);

        // Llama al método del servicio
        Curso result = cursoService.updateCurso(1L, updatedDetails);

        // Verifica que el resultado no sea nulo y los campos se hayan actualizado
        assertNotNull(result);
        assertEquals(updatedDetails.getNombre(), result.getNombre());
        assertEquals(updatedDetails.getDescripcion(), result.getDescripcion());
        assertEquals(updatedDetails.getDuracionHoras(), result.getDuracionHoras());
        // Verifica que findById, findByNombre y save fueron llamados una vez
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).findByNombre(updatedDetails.getNombre());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    @DisplayName("Actualizar curso - No encontrado")
    void updateCurso_NotFound() {
        Curso updatedDetails = new Curso(99L, "Nombre Falso", "Descripción", "Cat", 10, 10.0);

        // Configura el mock para findById (no encuentra el curso)
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        // Verifica que se lance una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.updateCurso(99L, updatedDetails);
        });

        // Verifica el mensaje de la excepción
        assertEquals("Curso no encontrado con ID: " + 99L, exception.getMessage());
        // Verifica que save nunca fue llamado
        verify(cursoRepository, times(1)).findById(99L);
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    @DisplayName("Actualizar curso - Conflicto de nombre con otro curso")
    void updateCurso_NombreConflicto() {
        Curso updatedDetails = new Curso(1L, "Diseño Web con React", "Descripción", "Cat", 10, 10.0); // Nombre de curso2

        // Configura el mock para findById (encuentra curso1)
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso1));
        // Configura el mock para findByNombre (encuentra curso2 con el mismo nombre)
        when(cursoRepository.findByNombre(updatedDetails.getNombre())).thenReturn(Optional.of(curso2));

        // Verifica que se lance una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.updateCurso(1L, updatedDetails);
        });

        // Verifica el mensaje de la excepción
        assertEquals("Ya existe otro curso con el nombre: " + updatedDetails.getNombre(), exception.getMessage());
        // Verifica que save nunca fue llamado
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).findByNombre(updatedDetails.getNombre());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    @DisplayName("Actualizar curso - Campos obligatorios faltantes (nombre nulo)")
    void updateCurso_InvalidInput_NullName() {
        Curso updatedDetails = new Curso(1L, null, "Descripción válida", "Categoría", 10, 50.0);

        // Configura el mock para findById (encuentra el curso original)
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso1));

        // Verifica que se lance una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.updateCurso(1L, updatedDetails);
        });

        // Verifica el mensaje de la excepción
        assertEquals("Nombre, descripción y categoría son campos obligatorios.", exception.getMessage());
        // Verifica que save nunca fue llamado
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, never()).findByNombre(anyString());
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    // --- Pruebas para DELETE ---

    @Test
    @DisplayName("Eliminar curso - Éxito")
    void deleteCurso_Success() {
        // Configura el mock para que existsById devuelva true (el curso existe)
        when(cursoRepository.existsById(1L)).thenReturn(true);
        // No necesitamos configurar deleteById porque es un método void

        // Llama al método del servicio
        assertDoesNotThrow(() -> cursoService.deleteCurso(1L));

        // Verifica que existsById y deleteById fueron llamados una vez
        verify(cursoRepository, times(1)).existsById(1L);
        verify(cursoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar curso - No encontrado")
    void deleteCurso_NotFound() {
        // Configura el mock para que existsById devuelva false (el curso no existe)
        when(cursoRepository.existsById(99L)).thenReturn(false);

        // Verifica que se lance una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.deleteCurso(99L);
        });

        // Verifica el mensaje de la excepción
        assertEquals("Curso no encontrado con ID: " + 99L, exception.getMessage());
        // Verifica que deleteById nunca fue llamado
        verify(cursoRepository, times(1)).existsById(99L);
        verify(cursoRepository, never()).deleteById(anyLong());
    }
}
