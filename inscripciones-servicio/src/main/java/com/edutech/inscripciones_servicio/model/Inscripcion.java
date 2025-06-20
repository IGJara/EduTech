package com.edutech.inscripcion_servicio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity // Indica que esta clase es una entidad JPA, mapeada a una tabla de base de datos
@Table(name = "inscripciones") // Define el nombre de la tabla en la base de datos
@Data // Anotación de Lombok para generar getters, setters, toString, equals y hashCode automáticamente
@NoArgsConstructor // Anotación de Lombok para generar un constructor sin argumentos
@AllArgsConstructor // Anotación de Lombok para generar un constructor con todos los argumentos
public class Inscripcion {

    @Id // Marca el campo 'id' como la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID (autoincremental)
    private Long id;

    @Column(nullable = false) // Indica que la columna no puede ser nula en la base de datos
    private Long idUsuario; // ID del usuario que se inscribe (referencia lógica a usuario-servicio)

    @Column(nullable = false)
    private Long idCurso; // ID del curso al que se inscribe (referencia lógica a curso-servicio)

    @Column(nullable = false)
    private LocalDate fechaInscripcion; // Fecha en que se realizó la inscripción

    @Enumerated(EnumType.STRING) // Mapea el enum 'EstadoInscripcion' a una columna de tipo String en la BD
    @Column(nullable = false)
    private EstadoInscripcion estado; // Estado de la inscripción (PENDIENTE, ACTIVA, COMPLETADA, CANCELADA)
}
