package com.edutech.soporte_servicio.controller;

import com.edutech.soporte_servicio.model.TicketSoporte;
import com.edutech.soporte_servicio.model.EstadoTicket;
import com.edutech.soporte_servicio.service.SoporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/soporte") // Define la URL base para todos los endpoints
public class SoporteController {

    private final SoporteService soporteService;

    // Inyección de dependencia del SoporteService
    @Autowired
    public SoporteController(SoporteService soporteService) {
        this.soporteService = soporteService;
    }

    @GetMapping // Maneja solicitudes GET a /api/soporte
    public List<TicketSoporte> getAllTickets() {
        return soporteService.getAllTickets();
    }

    @GetMapping("/{id}") // Maneja solicitudes GET a /api/soporte/{id}
    public ResponseEntity<TicketSoporte> getTicketById(@PathVariable Long id) {
        return soporteService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // Maneja solicitudes POST a /api/soporte
    public ResponseEntity<?> createTicket(@RequestBody TicketSoporte ticket) {
        try {
            TicketSoporte createdTicket = soporteService.createTicket(ticket);
            return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}") // Maneja solicitudes PUT a /api/soporte/{id}
    public ResponseEntity<TicketSoporte> updateTicket(@PathVariable Long id, @RequestBody TicketSoporte ticketDetails) {
        try {
            TicketSoporte updatedTicket = soporteService.updateTicket(id, ticketDetails);
            return ResponseEntity.ok(updatedTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}") // Maneja solicitudes DELETE a /api/soporte/{id}
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        try {
            soporteService.deleteTicket(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}") // Obtener tickets por ID de usuario
    public List<TicketSoporte> getTicketsByUserId(@PathVariable Long userId) {
        return soporteService.getTicketsByUserId(userId);
    }

    @GetMapping("/estado/{estado}") // Obtener tickets por estado
    public List<TicketSoporte> getTicketsByEstado(@PathVariable String estado) {
        try {
            EstadoTicket estadoEnum = EstadoTicket.valueOf(estado.toUpperCase());
            return soporteService.getTicketsByEstado(estadoEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de ticket no válido: " + estado);
        }
    }

    @GetMapping("/prioridad/{prioridad}") // Obtener tickets por prioridad
    public List<TicketSoporte> getTicketsByPrioridad(@PathVariable String prioridad) {
        return soporteService.getTicketsByPrioridad(prioridad);
    }
}
