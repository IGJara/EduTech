package com.edutech.inscripciones_servicio; // PAQUETE CORREGIDO

import com.edutech.inscripciones_servicio.model.Inscripcion; // IMPORT CORREGIDO
import com.edutech.inscripciones_servicio.model.EstadoInscripcion; // IMPORT CORREGIDO
import com.edutech.inscripciones_servicio.repository.InscripcionRepository; // IMPORT CORREGIDO
import com.edutech.inscripciones_servicio.service.InscripcionService; // IMPORT CORREGIDO

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor; // Importar ArgumentCaptor
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscripcionServiceTest {

    @Mock // Crea un objeto "mock" (simulado) de InscripcionRepository
    private InscripcionRepository inscripcionRepository;

    @InjectMocks // Inyecta los mocks creados (inscripcionRepository) en una instancia de InscripcionService
    private InscripcionService inscripcionService;

    private Inscripcion inscripcionExistente;
    private Inscripcion nuevaInscripcion;

    @BeforeEach // Este método se ejecuta antes de cada prueba
    void setUp() {
        inscripcionExistente = new Inscripcion(1L, 101L, 201L, LocalDate.of(2025, 1, 15), EstadoInscripcion.ACTIVA);
        // CORRECCIÓN: Inicializamos el estado como null para que el servicio lo establezca a ACTIVA
        nuevaInscripcion = new Inscripcion(null, 102L, 202L, LocalDate.now(), null);
    }

    @Test
    void testGetAllInscripciones() {
        // Configurar el mock: cuando se llame a findAll, devolver una lista de inscripciones
        when(inscripcionRepository.findAll()).thenReturn(Arrays.asList(inscripcionExistente, nuevaInscripcion));

        // Llamar al método a probar
        List<Inscripcion> inscripciones = inscripcionService.getAllInscripciones();

        // Verificar los resultados
        assertNotNull(inscripciones);
        assertEquals(2, inscripciones.size());
        // Aquí, como el mock devuelve `nuevaInscripcion` tal cual, su estado será `null`
        // Si el servicio no modifica `nuevaInscripcion` antes de devolverla en este método,
        // entonces el assert debería ser `assertNull(inscripciones.get(1).getStatus());`
        // Sin embargo, para `testGetAllInscripciones`, no se espera que el servicio modifique el estado
        // de las inscripciones existentes, por lo que este assert es más para `inscripcionExistente`.
        assertEquals("ACTIVA", inscripciones.get(0).getStatus().name());
        verify(inscripcionRepository, times(1)).findAll(); // Verificar que findAll fue llamado una vez
    }

    @Test
    void testGetInscripcionById_Found() {
        // Configurar el mock: cuando se llame a findById con el ID 1L, devolver la inscripcionExistente
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcionExistente));

        // Llamar al método a probar
        Optional<Inscripcion> foundInscripcion = inscripcionService.getInscripcionById(1L);

        // Verificar los resultados
        assertTrue(foundInscripcion.isPresent());
        assertEquals(1L, foundInscripcion.get().getId());
        verify(inscripcionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetInscripcionById_NotFound() {
        // Configurar el mock: cuando se llame a findById con cualquier ID, devolver un Optional vacío
        when(inscripcionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Llamar al método a probar
        Optional<Inscripcion> foundInscripcion = inscripcionService.getInscripcionById(99L);

        // Verificar los resultados
        assertFalse(foundInscripcion.isPresent());
        verify(inscripcionRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateInscripcion_Success() {
        // Configurar el mock: cuando se llame a findByUserIdAndCursoId, devolver una lista vacía (no existe la inscripción)
        when(inscripcionRepository.findByUserIdAndCursoId(anyLong(), anyLong())).thenReturn(Arrays.asList());

        // Usar ArgumentCaptor para capturar el objeto Inscripcion que se pasa al método save
        ArgumentCaptor<Inscripcion> inscripcionCaptor = ArgumentCaptor.forClass(Inscripcion.class);

        // Configurar el mock: cuando se llame a save, capturar el argumento y luego devolver el mismo objeto capturado
        // con un ID simulado. Esto simula el comportamiento real de un repositorio que asigna un ID.
        when(inscripcionRepository.save(inscripcionCaptor.capture())).thenAnswer(invocation -> {
            Inscripcion savedInscripcion = invocation.getArgument(0); // Obtener el objeto Inscripcion pasado a save
            savedInscripcion.setId(1L); // Simular la asignación de ID por el repositorio
            return savedInscripcion; // Devolver el objeto modificado (con ID)
        });

        // Llamar al método a probar
        Inscripcion createdInscripcion = inscripcionService.createInscripcion(nuevaInscripcion);

        // Verificar los resultados
        assertNotNull(createdInscripcion.getId());
        assertEquals(1L, createdInscripcion.getId()); // Verificar el ID asignado por el mock

        // Obtener el objeto Inscripcion que fue realmente pasado al método save
        Inscripcion capturedInscripcion = inscripcionCaptor.getValue();

        // Asegurarse de que el objeto capturado (el que el servicio intentó guardar) tiene el estado ACTIVA
        assertEquals(EstadoInscripcion.ACTIVA, capturedInscripcion.getStatus());

        // Asegurarse de que el objeto devuelto por el servicio también tiene el estado ACTIVA
        assertEquals(EstadoInscripcion.ACTIVA, createdInscripcion.getStatus());

        verify(inscripcionRepository, times(1)).findByUserIdAndCursoId(anyLong(), anyLong());
        verify(inscripcionRepository, times(1)).save(any(Inscripcion.class));
    }

    @Test
    void testCreateInscripcion_AlreadyEnrolled() {
        // Configurar el mock: cuando se llame a findByUserIdAndCursoId, devolver una lista con una inscripción (ya existe)
        when(inscripcionRepository.findByUserIdAndCursoId(anyLong(), anyLong())).thenReturn(Arrays.asList(inscripcionExistente));

        // Verificar que se lanza una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inscripcionService.createInscripcion(inscripcionExistente);
        });

        // Verificar el mensaje de error
        assertEquals("El usuario ya está inscrito en este curso.", exception.getMessage());
        verify(inscripcionRepository, times(1)).findByUserIdAndCursoId(anyLong(), anyLong());
        verify(inscripcionRepository, never()).save(any(Inscripcion.class)); // Verificar que save NUNCA fue llamado
    }

    @Test
    void testUpdateInscripcion_Success() {
        // Configurar los detalles de la actualización
        Inscripcion updatedDetails = new Inscripcion(1L, 101L, 201L, LocalDate.of(2025, 1, 15), EstadoInscripcion.COMPLETADA);

        // Configurar el mock: cuando se llame a findById, devolver la inscripcionExistente
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcionExistente));
        // Configurar el mock: cuando se llame a save, devolver la inscripcionExistente (simulando la actualización)
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(updatedDetails); // Retorna los detalles actualizados

        // Llamar al método a probar
        Inscripcion result = inscripcionService.updateInscripcion(1L, updatedDetails);

        // Verificar los resultados
        assertNotNull(result);
        assertEquals(EstadoInscripcion.COMPLETADA, result.getStatus());
        verify(inscripcionRepository, times(1)).findById(1L);
        verify(inscripcionRepository, times(1)).save(any(Inscripcion.class));
    }

    @Test
    void testUpdateInscripcion_NotFound() {
        // Configurar el mock: cuando se llame a findById, devolver un Optional vacío
        when(inscripcionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Verificar que se lanza una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inscripcionService.updateInscripcion(99L, new Inscripcion());
        });

        // Verificar el mensaje de error
        assertEquals("Inscripción no encontrada con ID: 99", exception.getMessage());
        verify(inscripcionRepository, times(1)).findById(99L);
        verify(inscripcionRepository, never()).save(any(Inscripcion.class));
    }

    @Test
    void testDeleteInscripcion_Success() {
        // Configurar el mock: cuando se llame a existsById, devolver true (la inscripción existe)
        when(inscripcionRepository.existsById(1L)).thenReturn(true);
        // No necesitamos simular el comportamiento de deleteById porque es un método void

        // Llamar al método a probar
        inscripcionService.deleteInscripcion(1L);

        // Verificar que los métodos del mock fueron llamados
        verify(inscripcionRepository, times(1)).existsById(1L);
        verify(inscripcionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteInscripcion_NotFound() {
        // Configurar el mock: cuando se llame a existsById, devolver false (la inscripción no existe)
        when(inscripcionRepository.existsById(anyLong())).thenReturn(false);

        // Verificar que se lanza una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inscripcionService.deleteInscripcion(99L);
        });

        // Verificar el mensaje de error
        assertEquals("Inscripción no encontrada con ID: 99", exception.getMessage());
        verify(inscripcionRepository, times(1)).existsById(99L);
        verify(inscripcionRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetInscripcionesByUserId() {
        // Configurar el mock
        when(inscripcionRepository.findByUserId(101L)).thenReturn(Arrays.asList(inscripcionExistente));

        // Llamar al método
        List<Inscripcion> inscripciones = inscripcionService.getInscripcionesByUserId(101L);

        // Verificar
        assertNotNull(inscripciones);
        assertEquals(1, inscripciones.size());
        assertEquals(101L, inscripciones.get(0).getUserId());
        verify(inscripcionRepository, times(1)).findByUserId(101L);
    }

    @Test
    void testGetInscripcionesByCursoId() {
        // Configurar el mock
        when(inscripcionRepository.findByCursoId(201L)).thenReturn(Arrays.asList(inscripcionExistente));

        // Llamar al método
        List<Inscripcion> inscripciones = inscripcionService.getInscripcionesByCursoId(201L);

        // Verificar
        assertNotNull(inscripciones);
        assertEquals(1, inscripciones.size());
        assertEquals(201L, inscripciones.get(0).getCursoId());
        verify(inscripcionRepository, times(1)).findByCursoId(201L);
    }

    @Test
    void testGetInscripcionesByStatus_Found() {
        // Configurar el mock
        when(inscripcionRepository.findByStatus(EstadoInscripcion.ACTIVA)).thenReturn(Arrays.asList(inscripcionExistente));

        // Llamar al método
        List<Inscripcion> inscripciones = inscripcionService.getInscripcionesByStatus(EstadoInscripcion.ACTIVA);

        // Verificar
        assertNotNull(inscripciones);
        assertEquals(1, inscripciones.size());
        assertEquals(EstadoInscripcion.ACTIVA, inscripciones.get(0).getStatus());
        verify(inscripcionRepository, times(1)).findByStatus(EstadoInscripcion.ACTIVA);
    }

    @Test
    void testGetInscripcionesByStatus_InvalidStatus() {
        // Este test es más relevante para el controlador si la validación del enum se hace antes de llegar al servicio.
        // Si el servicio recibiera un String y lo convirtiera a enum, se podría probar aquí la conversión fallida.
        // Dado que el servicio espera un `EstadoInscripcion` (un enum ya válido), no es necesario un test específico aquí
        // para un "estado inválido" en el contexto de cómo se está usando `findByStatus`.
    }
}
