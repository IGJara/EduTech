package com.edutech.usuario_servicio.controller;

import com.edutech.usuario_servicio.model.RolUsuario; // Se actualizó el tipo de enum
import com.edutech.usuario_servicio.model.Usuario; // Se actualizó el tipo de entidad
import com.edutech.usuario_servicio.service.UserService; // Se mantuvo el nombre del servicio por consistencia
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/usuarios") // Define la URL base para todos los endpoints de este controlador (se cambió de 'users')
public class UserController { // Se mantuvo el nombre 'UserController' por convención, aunque se puede cambiar a 'UsuarioController'

    private final UserService userService; // Se mantuvo el nombre del servicio

    // Inyección de dependencia del UserService a través del constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping // Maneja solicitudes GET a /api/usuarios para obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() { // Antes 'getAllUsers'
        return userService.obtenerTodosLosUsuarios();
    }

    @GetMapping("/{id}") // Maneja solicitudes GET a /api/usuarios/{id} para obtener un usuario por su ID
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) { // Antes 'getUserById'
        return userService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok) // Si el usuario se encuentra, devuelve 200 OK con el usuario
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, devuelve 404 Not Found
    }

    @PostMapping // Maneja solicitudes POST a /api/usuarios para crear un nuevo usuario
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) { // Antes 'createUser'
        try {
            Usuario usuarioCreado = userService.crearUsuario(usuario);
            return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED); // Devuelve 201 Created con el usuario creado
        } catch (RuntimeException e) {
            Map<String, String> respuestaError = new HashMap<>();
            respuestaError.put("error", e.getMessage());
            return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST); // Devuelve 400 Bad Request en caso de error
        }
    }

    @PutMapping("/{id}") // Maneja solicitudes PUT a /api/usuarios/{id} para actualizar un usuario existente
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario detallesUsuario) { // Antes 'updateUser'
        try {
            Usuario usuarioActualizado = userService.actualizarUsuario(id, detallesUsuario);
            return ResponseEntity.ok(usuarioActualizado); // Devuelve 200 OK con el usuario actualizado
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si no se encuentra, devuelve 404 Not Found
        }
    }

    @DeleteMapping("/{id}") // Maneja solicitudes DELETE a /api/usuarios/{id} para eliminar un usuario
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) { // Antes 'deleteUser'
        try {
            userService.eliminarUsuario(id);
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Si no se encuentra, devuelve 404 Not Found
        }
    }

    @GetMapping("/rol/{rol}") // Maneja solicitudes GET a /api/usuarios/rol/{rol}
    public List<Usuario> obtenerUsuariosPorRol(@PathVariable String rol) { // Antes '/role', se cambió nombre de método
        try {
            RolUsuario rolUsuario = RolUsuario.valueOf(rol.toUpperCase()); // Convierte el String a RolUsuario enum
            return userService.obtenerUsuariosPorRol(rolUsuario);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol no válido: " + rol); // Lanza una excepción si el rol no es válido
        }
    }
}
