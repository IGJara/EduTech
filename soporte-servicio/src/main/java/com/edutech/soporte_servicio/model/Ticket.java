package com.edutech.soporte_servicio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "tickets") // Define el nombre de la tabla en la base de datos
@Data // Anotación de Lombok para generar getters, setters, toString, equals y hashCode
@NoArgsConstructor // Anotación de Lombok para generar un constructor sin argumentos
@AllArgsConstructor // Anotación de Lombok para generar un constructor con todos los argumentos
public class Ticket {

    @Id // Marca el campo 'id' como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID
    private Long id;

    @Column(nullable = false)
    private Long idUsuario; // Antes 'userId' - ID del usuario que crea el ticket (referencia lógica a usuario-servicio)

    @Column(nullable = false)
    private String asunto; // Asunto del ticket

    @Column(nullable = false, length = 2000)
    private String descripcion; // Descripción detallada del problema

    @Enumerated(EnumType.STRING) // Mapea el enum 'EstadoTicket' a una columna de tipo String en la BD
    @Column(nullable = false)
    private EstadoTicket estado; // Antes 'status' - Estado del ticket (ABIERTO, EN_PROGRESO, CERRADO, RESUELTO)

    @Column(nullable = false)
    private LocalDateTime fechaCreacion; // Fecha y hora de creación del ticket

    private LocalDateTime fechaActualizacion; // Fecha y hora de última actualización

    private Long idAdministradorAsignado; // Antes 'asignadoAAdminId' - ID del administrador asignado (referencia lógica a usuario-servicio)
}
