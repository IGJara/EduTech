package com.edutech.soporte_servicio.service;

import com.edutech.soporte_servicio.client.UsuarioServiceClient; // Importa el cliente de usuario
import com.edutech.soporte_servicio.model.TicketSoporte;
import com.edutech.soporte_servicio.model.EstadoTicket;
import com.edutech.soporte_servicio.repository.TicketSoporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio de Spring
public class SoporteService {

    private final TicketSoporteRepository ticketSoporteRepository;
    private final UsuarioServiceClient usuarioServiceClient; // Inyección del cliente de usuario

    // Constructor para inyección de dependencias
    @Autowired
    public SoporteService(TicketSoporteRepository ticketSoporteRepository,
                          UsuarioServiceClient usuarioServiceClient) {
        this.ticketSoporteRepository = ticketSoporteRepository;
        this.usuarioServiceClient = usuarioServiceClient;
    }

    // Obtiene todos los tickets de soporte
    public List<TicketSoporte> getAllTickets() {
        return ticketSoporteRepository.findAll();
    }

    // Obtiene un ticket por su ID
    public Optional<TicketSoporte> getTicketById(Long id) {
        return ticketSoporteRepository.findById(id);
    }

    // Crea un nuevo ticket de soporte
    public TicketSoporte createTicket(TicketSoporte ticket) {
        // 1. Validar que el usuario exista
        if (!usuarioServiceClient.existeUsuario(ticket.getUserId())) {
            throw new RuntimeException("El usuario con ID " + ticket.getUserId() + " no existe.");
        }

        // Asignar fecha de creación y actualización si no están ya asignadas
        if (ticket.getFechaCreacion() == null) {
            ticket.setFechaCreacion(LocalDateTime.now());
        }
        if (ticket.getFechaActualizacion() == null) {
            ticket.setFechaActualizacion(LocalDateTime.now());
        }
        // Asignar estado inicial por defecto si no está asignado
        if (ticket.getEstado() == null) {
            ticket.setEstado(EstadoTicket.ABIERTO);
        }
        // Asignar prioridad por defecto si no está asignada
        if (ticket.getPrioridad() == null || ticket.getPrioridad().trim().isEmpty()) {
            ticket.setPrioridad("Media");
        }

        // Guarda el nuevo ticket en la base de datos
        return ticketSoporteRepository.save(ticket);
    }

    // Actualiza un ticket de soporte existente
    public TicketSoporte updateTicket(Long id, TicketSoporte ticketDetails) {
        return ticketSoporteRepository.findById(id)
                .map(ticketExistente -> {
                    // Actualiza los campos del ticket existente con los nuevos detalles
                    ticketExistente.setAsunto(ticketDetails.getAsunto());
                    ticketExistente.setDescripcion(ticketDetails.getDescripcion());
                    ticketExistente.setEstado(ticketDetails.getEstado());
                    ticketExistente.setPrioridad(ticketDetails.getPrioridad());
                    ticketExistente.setAgenteAsignado(ticketDetails.getAgenteAsignado());
                    ticketExistente.setFechaActualizacion(LocalDateTime.now()); // Actualizar la fecha de actualización
                    return ticketSoporteRepository.save(ticketExistente);
                })
                .orElseThrow(() -> new RuntimeException("Ticket de soporte no encontrado con ID: " + id));
    }

    // Elimina un ticket de soporte por su ID
    public void deleteTicket(Long id) {
        if (!ticketSoporteRepository.existsById(id)) {
            throw new RuntimeException("Ticket de soporte no encontrado con ID: " + id);
        }
        ticketSoporteRepository.deleteById(id);
    }

    // Obtiene tickets por ID de usuario
    public List<TicketSoporte> getTicketsByUserId(Long userId) {
        return ticketSoporteRepository.findByUserId(userId);
    }

    // Obtiene tickets por estado
    public List<TicketSoporte> getTicketsByEstado(EstadoTicket estado) {
        return ticketSoporteRepository.findByEstado(estado);
    }

    // Obtiene tickets por prioridad
    public List<TicketSoporte> getTicketsByPrioridad(String prioridad) {
        return ticketSoporteRepository.findByPrioridad(prioridad);
    }
}
