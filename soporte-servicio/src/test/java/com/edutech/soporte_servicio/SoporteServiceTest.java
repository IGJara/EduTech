package com.edutech.soporte_servicio;

// Importaciones de JUnit y Mockito
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

// Importaciones de las clases de tu proyecto
import com.edutech.soporte_servicio.model.TicketSoporte;
import com.edutech.soporte_servicio.model.EstadoTicket;
import com.edutech.soporte_servicio.repository.TicketSoporteRepository;
import com.edutech.soporte_servicio.client.UsuarioServiceClient;
import com.edutech.soporte_servicio.service.SoporteService;

// Importaciones estáticas para aserciones y métodos de Mockito
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class SoporteServiceTest {

    @Mock
    private TicketSoporteRepository ticketSoporteRepository;

    @Mock
    private UsuarioServiceClient usuarioServiceClient;

    @InjectMocks
    private SoporteService soporteService;

    private TicketSoporte ticketValido;
    private TicketSoporte ticketExistente;

    @BeforeEach
    void setUp() {
        ticketValido = new TicketSoporte(null, 1L, "Problema con acceso a curso", "No puedo iniciar sesión en el curso de Java.", null, null, null, null, null);
        ticketExistente = new TicketSoporte(10L, 2L, "Error en plataforma", "La página de perfil no carga.", LocalDateTime.now().minusDays(2), EstadoTicket.ABIERTO, "Media", LocalDateTime.now().minusDays(1), "Agente Inicial"); // Constructor completo
    }

    @Test
    void testCreateTicket_Success() {
        when(usuarioServiceClient.existeUsuario(ticketValido.getUserId())).thenReturn(true);
        ArgumentCaptor<TicketSoporte> ticketCaptor = ArgumentCaptor.forClass(TicketSoporte.class);
        when(ticketSoporteRepository.save(ticketCaptor.capture())).thenAnswer(invocation -> {
            TicketSoporte savedTicket = invocation.getArgument(0);
            savedTicket.setId(1L);
            return savedTicket;
        });

        TicketSoporte createdTicket = soporteService.createTicket(ticketValido);

        assertNotNull(createdTicket);
        assertNotNull(createdTicket.getId());
        assertEquals(ticketValido.getUserId(), createdTicket.getUserId());
        assertEquals("Problema con acceso a curso", createdTicket.getAsunto());
        assertNotNull(createdTicket.getFechaCreacion());
        assertNotNull(createdTicket.getFechaActualizacion());
        assertEquals(EstadoTicket.ABIERTO, createdTicket.getEstado());
        assertEquals("Media", createdTicket.getPrioridad());

        verify(usuarioServiceClient, times(1)).existeUsuario(ticketValido.getUserId());
        verify(ticketSoporteRepository, times(1)).save(any(TicketSoporte.class));

        TicketSoporte capturedTicket = ticketCaptor.getValue();
        assertNotNull(capturedTicket.getFechaCreacion());
        assertNotNull(capturedTicket.getFechaActualizacion());
        assertEquals(EstadoTicket.ABIERTO, capturedTicket.getEstado());
        assertEquals("Media", capturedTicket.getPrioridad());
    }

    @Test
    void testCreateTicket_UserDoesNotExist() {
        when(usuarioServiceClient.existeUsuario(ticketValido.getUserId())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            soporteService.createTicket(ticketValido);
        });

        assertEquals("El usuario con ID " + ticketValido.getUserId() + " no existe.", exception.getMessage());
        verify(ticketSoporteRepository, never()).save(any(TicketSoporte.class));
        verify(usuarioServiceClient, times(1)).existeUsuario(ticketValido.getUserId());
    }

    @Test
    void testGetAllTickets() {
        when(ticketSoporteRepository.findAll()).thenReturn(Arrays.asList(ticketValido, ticketExistente));

        List<TicketSoporte> tickets = soporteService.getAllTickets();

        assertNotNull(tickets);
        assertEquals(2, tickets.size());
        verify(ticketSoporteRepository, times(1)).findAll();
    }

    @Test
    void testGetTicketById_Found() {
        when(ticketSoporteRepository.findById(ticketExistente.getId())).thenReturn(Optional.of(ticketExistente));

        Optional<TicketSoporte> foundTicket = soporteService.getTicketById(ticketExistente.getId());

        assertTrue(foundTicket.isPresent());
        assertEquals(ticketExistente.getId(), foundTicket.get().getId());
        verify(ticketSoporteRepository, times(1)).findById(ticketExistente.getId());
    }

    @Test
    void testGetTicketById_NotFound() {
        when(ticketSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<TicketSoporte> foundTicket = soporteService.getTicketById(99L);

        assertFalse(foundTicket.isPresent());
        verify(ticketSoporteRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateTicket_Success() {
        // CORRECCIÓN AQUÍ: La llamada al constructor debe coincidir con la firma de TicketSoporte
        TicketSoporte updatedDetails = new TicketSoporte(
                ticketExistente.getId(),
                ticketExistente.getUserId(),
                "Asunto Actualizado",
                "Descripción Actualizada.",
                ticketExistente.getFechaCreacion(),
                EstadoTicket.EN_PROGRESO,
                "Alta",
                LocalDateTime.now(), // Fecha de actualización
                "AgenteX" // Agente asignado
        );

        when(ticketSoporteRepository.findById(ticketExistente.getId())).thenReturn(Optional.of(ticketExistente));
        when(ticketSoporteRepository.save(any(TicketSoporte.class))).thenAnswer(invocation -> {
            TicketSoporte savedTicket = invocation.getArgument(0);
            assertNotNull(savedTicket.getFechaActualizacion());
            return savedTicket;
        });

        TicketSoporte result = soporteService.updateTicket(ticketExistente.getId(), updatedDetails);

        assertNotNull(result);
        assertEquals("Asunto Actualizado", result.getAsunto());
        assertEquals(EstadoTicket.EN_PROGRESO, result.getEstado());
        assertEquals("Alta", result.getPrioridad());
        assertEquals("AgenteX", result.getAgenteAsignado());
        assertNotNull(result.getFechaActualizacion());

        verify(ticketSoporteRepository, times(1)).findById(ticketExistente.getId());
        verify(ticketSoporteRepository, times(1)).save(ticketExistente);
    }

    @Test
    void testUpdateTicket_NotFound() {
        TicketSoporte updatedDetails = new TicketSoporte(
                99L, 1L, "Asunto Falso", "Descripción Falsa.",
                LocalDateTime.now(), EstadoTicket.CERRADO, "Baja", LocalDateTime.now(), null
        );
        when(ticketSoporteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            soporteService.updateTicket(99L, updatedDetails);
        });

        assertEquals("Ticket de soporte no encontrado con ID: 99", exception.getMessage());
        verify(ticketSoporteRepository, never()).save(any(TicketSoporte.class));
    }

    @Test
    void testDeleteTicket_Success() {
        when(ticketSoporteRepository.existsById(ticketExistente.getId())).thenReturn(true);
        doNothing().when(ticketSoporteRepository).deleteById(ticketExistente.getId());

        soporteService.deleteTicket(ticketExistente.getId());

        verify(ticketSoporteRepository, times(1)).existsById(ticketExistente.getId());
        verify(ticketSoporteRepository, times(1)).deleteById(ticketExistente.getId());
    }

    @Test
    void testDeleteTicket_NotFound() {
        when(ticketSoporteRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            soporteService.deleteTicket(99L);
        });

        assertEquals("Ticket de soporte no encontrado con ID: 99", exception.getMessage());
        verify(ticketSoporteRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetTicketsByUserId() {
        when(ticketSoporteRepository.findByUserId(ticketExistente.getUserId())).thenReturn(Arrays.asList(ticketExistente));

        List<TicketSoporte> tickets = soporteService.getTicketsByUserId(ticketExistente.getUserId());

        assertNotNull(tickets);
        assertEquals(1, tickets.size());
        assertEquals(ticketExistente.getUserId(), tickets.get(0).getUserId());
        verify(ticketSoporteRepository, times(1)).findByUserId(ticketExistente.getUserId());
    }

    @Test
    void testGetTicketsByEstado() {
        when(ticketSoporteRepository.findByEstado(EstadoTicket.ABIERTO)).thenReturn(Arrays.asList(ticketExistente));

        List<TicketSoporte> tickets = soporteService.getTicketsByEstado(EstadoTicket.ABIERTO);

        assertNotNull(tickets);
        assertEquals(1, tickets.size());
        assertEquals(EstadoTicket.ABIERTO, tickets.get(0).getEstado());
        verify(ticketSoporteRepository, times(1)).findByEstado(EstadoTicket.ABIERTO);
    }

    @Test
    void testGetTicketsByPrioridad() {
        when(ticketSoporteRepository.findByPrioridad("Media")).thenReturn(Arrays.asList(ticketExistente));

        List<TicketSoporte> tickets = soporteService.getTicketsByPrioridad("Media");

        assertNotNull(tickets);
        assertEquals(1, tickets.size());
        assertEquals("Media", tickets.get(0).getPrioridad());
        verify(ticketSoporteRepository, times(1)).findByPrioridad("Media");
    }
}
