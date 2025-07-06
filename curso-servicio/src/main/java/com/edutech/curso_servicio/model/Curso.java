package com.edutech.curso_servicio.model; // Paquete actualizado para coincidir con la estructura

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity // Indica que esta clase es una entidad JPA y se mapea a una tabla de BD
@Table(name = "cursos") // Especifica el nombre de la tabla en la base de datos
@Data // Genera getters, setters, toString, equals y hashCode con Lombok
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class Curso { // Nombre de la clase: Curso

    @Id // Marca la propiedad como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estrategia de generación de ID (auto-incremento para MySQL)
    private Long id;

    @Column(nullable = false, unique = true) // Nombre del curso, no nulo y único
    private String nombre; // Antes 'name', ahora en español

    @Column(nullable = false, length = 1000) // Descripción del curso, con longitud máxima de 1000
    private String descripcion; // Antes 'description', ahora en español

    @Column(nullable = false)
    private String categoria; // Antes 'category', ahora en español

    @Column(nullable = false)
    private Double precio; // Antes 'price', ahora en español

    private LocalDate fechaInicio; // Antes 'startDate', ahora en español
    private LocalDate fechaFin;   // Antes 'endDate', ahora en español

    // En una arquitectura de microservicios, el ID del profesor generalmente se manejaría aquí
    // y se validaría haciendo una llamada al usuario-service.
    @Column(nullable = false)
    private String nombreUsuarioProfesor; // Antes 'teacherUsername', ahora en español
}
