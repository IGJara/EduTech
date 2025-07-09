package com.edutech.curso_servicio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Marks this class as a JPA entity
@Table(name = "cursos") // Maps the entity to the 'cursos' table
@Data // Generates getters, setters, equals, hashCode, toString (Lombok)
@NoArgsConstructor // No-argument constructor (Lombok)
@AllArgsConstructor // Constructor with all arguments (Lombok)
public class Curso {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated ID
    private Long id;

    @Column(nullable = false, unique = true) // Column cannot be null and must be unique
    private String nombre;

    @Column(nullable = false) // Column cannot be null
    private String descripcion;

    @Column(nullable = false) // Column cannot be null
    private String categoria;

    private Integer duracionHoras; // Duration in hours

    private Double precio; // Course price
}