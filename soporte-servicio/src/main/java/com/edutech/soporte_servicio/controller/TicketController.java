package com.edutech.soporte_servicio.controller;

import com.edutech.soporte_servicio.model.Ticket;
import com.edutech.soporte_servicio.model.EstadoTicket;
import com.edutech.soporte_servicio.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/tickets") // Define la URL base para todos los endpoints de este controlador
public class TicketController {

    private final TicketService ticketService;

    // Inyección de dependencia del TicketService a través del constructor
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping // Maneja solicitudes GET a /api/tickets para obtener todos los tickets
    public List<Ticket> obtenerTodosLosTickets() {
        return ticketService.obtenerTodosLosTickets();
    }

    @GetMapping("/{id}") // Maneja solicitudes GET a /api/tickets/{id} para obtener un ticket por su ID
    public ResponseEntity<Ticket> obtenerTicketPorId(@PathVariable Long id) {
        return ticketService.obtenerTicketPorId(id)
                .map(ResponseEntity::ok) // Si el ticket se encuentra, devuelve 200 OK con el ticket
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, devuelve 404 Not Found
    }

    @PostMapping // Maneja solicitudes POST a /api/tickets para crear un nuevo ticket
    public ResponseEntity<?> crearTicket(@RequestBody Ticket ticket) {
        try {
            Ticket ticketCreado = ticketService.crearTicket(ticket);
            return new ResponseEntity<>(ticketCreado, HttpStatus.CREATED); // Devuelve 201 Created con el ticket creado
        } catch (RuntimeException e) {
            Map<String, String> respuestaError = new HashMap<>();
            respuestaError.put("error", e.getMessage());
            return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST); // Devuelve 400 Bad Request en caso de error
        }
    }

    @PutMapping("/{id}") // Maneja solicitudes PUT a /api/tickets/{id} para actualizar un ticket existente
    public ResponseEntity<Ticket> actualizarTicket(@PathVariable Long id, @RequestBody Ticket detallesTicket) {
        try {
            Ticket ticketActualizado = ticketService.actualizarTicket(id, detallesTicket);
            return ResponseEntity.ok(ticketActualizado); // Devuelve 200 OK con el ticket actualizado
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si no se encuentra, devuelve 404 Not Found
        }
    }

    @DeleteMapping("/{id}") // Maneja solicitudes DELETE a /api/tickets/{id} para eliminar un ticket
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        try {
            ticketService.eliminarTicket(id);
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si no se encuentra, devuelve 404 Not Found
        }
    }

    @GetMapping("/usuario/{idUsuario}") // Maneja solicitudes GET a /api/tickets/usuario/{idUsuario}
    public List<Ticket> obtenerTicketsPorIdUsuario(@PathVariable Long idUsuario) {
        return ticketService.obtenerTicketsPorIdUsuario(idUsuario);
    }

    @GetMapping("/estado/{estado}") // Maneja solicitudes GET a /api/tickets/estado/{estado}
    public List<Ticket> obtenerTicketsPorEstado(@PathVariable String estado) {
        try {
            EstadoTicket estadoTicket = EstadoTicket.valueOf(estado.toUpperCase());
            return ticketService.obtenerTicketsPorEstado(estadoTicket);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de ticket no válido: " + estado);
        }
    }

    @GetMapping("/administrador/{idAdministrador}") // Maneja solicitudes GET a /api/tickets/administrador/{idAdministrador}
    public List<Ticket> obtenerTicketsAsignadosAAdministrador(@PathVariable Long idAdministrador) { // Antes '/asignado'
        return ticketService.obtenerTicketsAsignadosAAdministrador(idAdministrador);
    }
}
