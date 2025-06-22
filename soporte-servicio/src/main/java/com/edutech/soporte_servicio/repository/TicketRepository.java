package com.edutech.soporte_servicio.repository;

import com.edutech.soporte_servicio.model.Ticket;
import com.edutech.soporte_servicio.model.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Indica que esta interfaz es un componente de repositorio de Spring Data JPA
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Busca tickets por el ID del usuario que los creó
    List<Ticket> findByIdUsuario(Long idUsuario); // Antes 'findByUserId'
    // Busca tickets por un estado específico
    List<Ticket> findByEstado(EstadoTicket estado); // Antes 'findByStatus'
    // Busca tickets asignados a un ID de administrador específico
    List<Ticket> findByIdAdministradorAsignado(Long idAdministradorAsignado); // Antes 'findByAsignadoAAdminId'
}
