package com.edutech.curso_servicio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; // Antes 'name'

    @Column(nullable = false, length = 1000)
    private String descripcion; // Antes 'description'

    @Column(nullable = false)
    private String categoria; // Antes 'category'

    @Column(nullable = false)
    private Double precio; // Antes 'price'

    private LocalDate fechaInicio; // Antes 'startDate'
    private LocalDate fechaFin; // Antes 'endDate'

    @Column(nullable = false)
    private String nombreUsuarioProfesor; // Antes 'teacherUsername'
}
