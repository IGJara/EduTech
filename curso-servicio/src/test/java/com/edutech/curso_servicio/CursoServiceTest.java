package com.edutech.curso_servicio; // Asegúrate de que este paquete coincida con la ubicación del servicio

// Importaciones de JUnit y Mockito
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Importaciones de las clases de tu proyecto (¡CRUCIALES!)
import com.edutech.curso_servicio.model.Curso; // Importa tu clase Curso del nuevo paquete
import com.edutech.curso_servicio.repository.CursoRepository; // Importa tu interfaz CursoRepository del nuevo paquete
import com.edutech.curso_servicio.service.CursoService; // Importa tu clase CursoService del nuevo paquete

// Importaciones estáticas para aserciones y métodos de Mockito
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate; // Importar LocalDate para las fechas
import java.util.Optional; // ¡IMPORTACIÓN AÑADIDA PARA Optional!

@ExtendWith(MockitoExtension.class) // Habilita la integración de Mockito con JUnit 5
public class CursoServiceTest { // El nombre de la clase DEBE coincidir con el nombre del archivo (CursoServiceTest.java)

    @Mock // Crea un objeto "mock" (simulado) de CursoRepository
    private CursoRepository cursoRepository;

    @InjectMocks // Inyecta los mocks creados (cursoRepository) en una instancia de CursoService
    private CursoService cursoService; // Asegúrate de que CursoService sea el nombre correcto de tu clase de servicio

    // Este método se ejecuta antes de cada prueba
    @BeforeEach
    void setUp() {
        // Aquí podrías resetear mocks o configurar un estado inicial común si fuera necesario
    }

    @Test // Marca este método como una prueba unitaria
    void testCrearCurso_Success() {
        // 1. Configurar el escenario (Given)
        // Creamos un nuevo curso sin ID, usando los campos de la entidad Curso en español
        Curso nuevoCurso = new Curso(
            null, // id
            "Programación Java Avanzada", // nombre
            "Curso completo de Java para desarrolladores intermedios.", // descripcion
            "Programación", // categoria
            99.99, // precio
            LocalDate.of(2025, 9, 1), // fechaInicio
            LocalDate.of(2025, 9, 30), // fechaFin
            "profesor_tech" // nombreUsuarioProfesor
        );

        // Simular el comportamiento del cursoRepository:
        // Cuando se llame a save con cualquier objeto Curso, que devuelva el mismo objeto (simulando que se guarda)
        when(cursoRepository.save(any(Curso.class))).thenReturn(nuevoCurso);

        // 2. Ejecutar la acción a probar (When)
        Curso cursoCreado = cursoService.crearCurso(nuevoCurso);

        // 3. Verificar los resultados (Then)
        assertNotNull(cursoCreado, "El curso creado no debería ser nulo");
        assertEquals("Programación Java Avanzada", cursoCreado.getNombre(), "El nombre del curso debería coincidir");
        assertEquals("Programación", cursoCreado.getCategoria(), "La categoría debería coincidir");
        assertEquals(99.99, cursoCreado.getPrecio(), 0.001, "El precio debería coincidir"); // Usar delta para doubles
        assertEquals(LocalDate.of(2025, 9, 1), cursoCreado.getFechaInicio(), "La fecha de inicio debería coincidir");
        assertEquals("profesor_tech", cursoCreado.getNombreUsuarioProfesor(), "El nombre de usuario del profesor debería coincidir");


        // Verificar que el método save del mock fue llamado exactamente una vez
        verify(cursoRepository, times(1)).save(any(Curso.class));
        // Verificar que findByNombre también fue llamado para la validación de unicidad
        verify(cursoRepository, times(1)).findByNombre("Programación Java Avanzada");
    }

    @Test
    void testCrearCurso_NombreVacio() {
        // 1. Configurar el escenario (Given)
        // Creamos un curso con el nombre vacío
        Curso cursoInvalido = new Curso(
            null, // id
            "", // nombre (vacío)
            "Descripción de un curso inválido", // descripcion
            "General", // categoria
            10.0, // precio
            LocalDate.of(2025, 10, 1), // fechaInicio
            LocalDate.of(2025, 10, 15), // fechaFin
            "profesor_invalido" // nombreUsuarioProfesor
        );

        // 2. Ejecutar la acción a probar y verificar que lanza una excepción (When & Then)
        // Verificamos que se lanza una RuntimeException con el mensaje específico
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.crearCurso(cursoInvalido);
        });

        // Verificar el mensaje de la excepción
        assertEquals("El nombre del curso no puede estar vacío.", exception.getMessage(), "El mensaje de error debería coincidir");

        // Verificar que el método save NO fue llamado (porque el curso es inválido)
        verify(cursoRepository, never()).save(any(Curso.class));
        // findByNombre no debería ser llamado si el nombre es vacío, ya que la validación de vacío es la primera
        verify(cursoRepository, never()).findByNombre(anyString());
    }

    @Test
    void testCrearCurso_NombreExistente() {
        // 1. Configurar el escenario (Given)
        Curso cursoExistente = new Curso(
            null,
            "Curso Existente",
            "Descripción",
            "Categoria",
            50.0,
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 1, 31),
            "profesor_existente"
        );

        // Simular que el nombre del curso ya existe en la base de datos
        when(cursoRepository.findByNombre("Curso Existente")).thenReturn(Optional.of(new Curso())); // Devuelve un Optional con un curso

        // 2. Ejecutar la acción a probar y verificar que lanza una excepción (When & Then)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.crearCurso(cursoExistente);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Ya existe un curso con el nombre: Curso Existente", exception.getMessage(), "El mensaje de error debería indicar que el curso ya existe");

        // Verificar que save NO fue llamado
        verify(cursoRepository, never()).save(any(Curso.class));
        // Verificar que findByNombre fue llamado una vez
        verify(cursoRepository, times(1)).findByNombre("Curso Existente");
    }
}
