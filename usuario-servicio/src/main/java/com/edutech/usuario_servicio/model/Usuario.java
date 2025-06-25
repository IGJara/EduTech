package com.edutech.usuario_servicio.model; // Paquete correcto para coincidir con la carpeta: usuario_servicio

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Indica que esta clase es una entidad JPA y se mapea a una tabla de BD
@Table(name = "users") // Especifica el nombre de la tabla en la base de datos
@Data // Genera getters, setters, toString, equals y hashCode con Lombok
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class Usuario { // CLASE: Usuario (coincide con Usuario.java)

    @Id // Marca la propiedad como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estrategia de generación de ID (auto-incremento para MySQL)
    private Long id;

    @Column(nullable = false, unique = true) // Columna no nula y única
    private String username;

    @Column(nullable = false)
    private String password; // ATENCIÓN: En una aplicación real, NUNCA almacenes contraseñas en texto plano.
                             // Usa Spring Security para encriptarlas (ej. BCryptPasswordEncoder).

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING) // Almacena el nombre del Enum (ROLE_STUDENT, ROLE_TEACHER, etc.)
    @Column(nullable = false)
    private RolUsuario role; // Enum para los roles de usuario (Tipo corregido)
}
