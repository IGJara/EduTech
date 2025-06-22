package com.edutech.soporte_servicio.service;

import com.edutech.soporte_servicio.model.Ticket;
import com.edutech.soporte_servicio.model.EstadoTicket;
import com.edutech.soporte_servicio.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio de Spring
public class TicketService {

    private final TicketRepository ticketRepository;

    // Inyección de dependencia del TicketRepository a través del constructor
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // Obtiene una lista de todos los tickets
    public List<Ticket> obtenerTodosLosTickets() {
        return ticketRepository.findAll();
    }

    // Obtiene un ticket específico por su ID
    public Optional<Ticket> obtenerTicketPorId(Long id) {
        return ticketRepository.findById(id);
    }

    // Crea un nuevo ticket
    public Ticket crearTicket(Ticket ticket) {
        // En una aplicación real, aquí se validarían los IDs de usuario/administrador con usuario-servicio.
        ticket.setFechaCreacion(LocalDateTime.now()); // Asigna la fecha y hora de creación actual
        if (ticket.getEstado() == null) {
            ticket.setEstado(EstadoTicket.ABIERTO); // Asigna 'ABIERTO' como estado por defecto
        }
        // Guarda el nuevo ticket en la base de datos
        return ticketRepository.save(ticket);
    }

    // Actualiza los detalles de un ticket existente por su ID
    public Ticket actualizarTicket(Long id, Ticket detallesTicket) {
        // Busca el ticket por su ID. Si no se encuentra, lanza una excepción.
        return ticketRepository.findById(id)
                .map(ticketExistente -> {
                    // Actualiza los campos del ticket existente con los nuevos detalles
                    ticketExistente.setAsunto(detallesTicket.getAsunto());
                    ticketExistente.setDescripcion(detallesTicket.getDescripcion());
                    ticketExistente.setEstado(detallesTicket.getEstado());
                    ticketExistente.setIdAdministradorAsignado(detallesTicket.getIdAdministradorAsignado());
                    ticketExistente.setFechaActualizacion(LocalDateTime.now()); // Actualiza la fecha de modificación
                    // Guarda los cambios del ticket actualizado
                    return ticketRepository.save(ticketExistente);
                })
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
    }

    // Elimina un ticket por su ID
    public void eliminarTicket(Long id) {
        // Verifica si el ticket existe antes de intentar eliminarlo
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket no encontrado con ID: " + id);
        }
        // Elimina el ticket de la base de datos
        ticketRepository.deleteById(id);
    }

    // Obtiene una lista de tickets creados por un ID de usuario específico
    public List<Ticket> obtenerTicketsPorIdUsuario(Long idUsuario) { // Antes 'getTicketsByUserId'
        return ticketRepository.findByIdUsuario(idUsuario);
    }

    // Obtiene una lista de tickets por un estado específico
    public List<Ticket> obtenerTicketsPorEstado(EstadoTicket estado) { // Antes 'getTicketsByStatus'
        return ticketRepository.findByEstado(estado);
    }

    // Obtiene una lista de tickets asignados a un ID de administrador específico
    public List<Ticket> obtenerTicketsAsignadosAAdministrador(Long idAdministrador) { // Antes 'getTicketsAsignadosAAdmin'
        return ticketRepository.findByIdAdministradorAsignado(idAdministrador);
    }
}
