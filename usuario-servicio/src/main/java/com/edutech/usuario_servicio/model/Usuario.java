package com.edutech.usuario_servicio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "usuarios") // Nombre de la tabla en la base de datos
@Data // Genera getters, setters, toString, equals y hashCode con Lombok
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class Usuario {

    @Id // Marca la propiedad como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estrategia de generación de ID (auto-incremento)
    private Long id;

    @Column(nullable = false, unique = true) // El email debe ser único y no nulo
    private String email;

    @Column(nullable = false)
    private String password; // La contraseña del usuario (debe ser encriptada en una aplicación real)

    @Column(nullable = false)
    private String nombre; // Nombre del usuario

    @Column(nullable = false)
    private String apellido; // Apellido del usuario

    private LocalDate fechaNacimiento; // Fecha de nacimiento del usuario

    @Enumerated(EnumType.STRING) // Mapea el enum a una columna de tipo String en la BD
    @Column(nullable = false)
    private TipoUsuario tipoUsuario; // Tipo de usuario (ej. ESTUDIANTE, PROFESOR, ADMINISTRADOR)

    private String direccion; // Dirección del usuario
    private String telefono; // Número de teléfono del usuario
}
