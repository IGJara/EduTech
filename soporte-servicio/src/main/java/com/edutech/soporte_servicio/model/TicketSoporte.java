package com.edutech.soporte_servicio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "tickets_soporte") // Nombre de la tabla en la base de datos
@Data // Genera getters, setters, toString, equals y hashCode con Lombok
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class TicketSoporte {

    @Id // Marca la propiedad como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estrategia de generación de ID (auto-incremento)
    private Long id;

    @Column(nullable = false)
    private Long userId; // ID del usuario que crea el ticket (se validará con usuario-servicio)

    @Column(nullable = false)
    private String asunto; // Asunto o título del ticket

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion; // Descripción detallada del problema

    @Column(nullable = false)
    private LocalDateTime fechaCreacion; // Fecha y hora de creación del ticket

    @Enumerated(EnumType.STRING) // Mapea el enum a una columna de tipo String en la BD
    @Column(nullable = false)
    private EstadoTicket estado; // Estado del ticket (ej. ABIERTO, EN_PROGRESO, CERRADO)

    private String prioridad; // Prioridad del ticket (ej. "Baja", "Media", "Alta")
    private LocalDateTime fechaActualizacion; // Fecha y hora de la última actualización
    private String agenteAsignado; // Nombre del agente de soporte asignado
}
