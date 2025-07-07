package com.edutech.soporte_servicio.repository;

import com.edutech.soporte_servicio.model.TicketSoporte;
import com.edutech.soporte_servicio.model.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Indica que esta interfaz es un componente de repositorio de Spring Data JPA
public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Long> {
    // JpaRepository proporciona métodos CRUD básicos.
    // Puedes añadir métodos de consulta personalizados aquí si son necesarios.

    // Busca tickets por el ID del usuario que los creó
    List<TicketSoporte> findByUserId(Long userId);

    // Busca tickets por su estado
    List<TicketSoporte> findByEstado(EstadoTicket estado);

    // Busca tickets por su prioridad
    List<TicketSoporte> findByPrioridad(String prioridad);
}
