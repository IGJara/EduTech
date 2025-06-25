package com.edutech.usuario_servicio.controller; // Paquete correcto para coincidir con la carpeta: usuario_servicio

import com.edutech.usuario_servicio.model.RolUsuario; // Importa la clase RolUsuario (coincide con RolUsuario.java)
import com.edutech.usuario_servicio.model.Usuario; // Importa la clase Usuario (coincide con Usuario.java)
import com.edutech.usuario_servicio.service.UsuarioService; // Importa la clase UsuarioService (coincide con UsuarioService.java)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/users") // Define la URL base para todos los endpoints de este controlador
public class UsuarioController { // CLASE: UsuarioController (coincide con UsuarioController.java)

    private final UsuarioService usuarioService; // Usa la clase UsuarioService

    @Autowired // Inyección de dependencia del UsuarioService
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping // GET /api/users
    public List<Usuario> getAllUsuarios() { // Obtiene todos los usuarios
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}") // GET /api/users/{id}
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) { // Obtiene un usuario por ID
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok) // Si se encuentra el usuario, devuelve 200 OK
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, devuelve 404 Not Found
    }

    @PostMapping // POST /api/users
    public ResponseEntity<?> createUsuario(@RequestBody Usuario usuario) { // Crea un nuevo usuario
        // Es común usar DTOs (Data Transfer Objects) para la entrada/salida de datos
        // en lugar de la entidad directamente, para controlar qué datos se exponen o requieren.
        try {
            Usuario createdUsuario = usuarioService.createUsuario(usuario);
            return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED); // Devuelve 201 Created si es exitoso
        } catch (RuntimeException e) {
            // Devuelve 400 Bad Request con el mensaje de error si hay un problema (ej. usuario/email duplicado)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}") // PUT /api/users/{id}
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) { // Actualiza un usuario
        try {
            Usuario updatedUsuario = usuarioService.updateUsuario(id, usuarioDetails);
            return ResponseEntity.ok(updatedUsuario); // Devuelve 200 OK si es exitoso
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found si el usuario no existe
        }
    }

    @DeleteMapping("/{id}") // DELETE /api/users/{id}
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) { // Elimina un usuario
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content para eliminación exitosa
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found si el usuario no existe
        }
    }

    // Ejemplo de endpoint para buscar usuarios por rol
    @GetMapping("/role/{role}") // GET /api/users/role/{nombreRol}
    public List<Usuario> getUsuariosByRole(@PathVariable String role) { // Obtiene usuarios por rol
        try {
            RolUsuario userRole = RolUsuario.valueOf(role.toUpperCase()); // Convierte el String de rol a un valor del Enum RolUsuario
            return usuarioService.getUsuariosByRole(userRole);
        } catch (IllegalArgumentException e) {
            // Manejo de error si el rol proporcionado no es válido (no existe en el Enum)
            throw new RuntimeException("Rol no válido: " + role);
        }
    }
}
