package com.edutech.login_servicio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "login_users") // Usamos un nombre de tabla diferente para evitar conflictos con usuario-servicio
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // ¡RECORDATORIO: En una aplicación real, esta contraseña DEBE ser encriptada!

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // Rol básico para el usuario de login
}
