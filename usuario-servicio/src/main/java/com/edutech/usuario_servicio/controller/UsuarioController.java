package com.edutech.usuario_servicio.controller;

import com.edutech.usuario_servicio.model.Usuario;
import com.edutech.usuario_servicio.model.TipoUsuario;
import com.edutech.usuario_servicio.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/users") // Define la URL base para todos los endpoints
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Inyección de dependencia del UsuarioService
    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping // Maneja solicitudes GET a /api/users
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}") // Maneja solicitudes GET a /api/users/{id}
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // Maneja solicitudes POST a /api/users
    public ResponseEntity<?> createUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario createdUsuario = usuarioService.createUsuario(usuario);
            return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}") // Maneja solicitudes PUT a /api/users/{id}
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        try {
            Usuario updatedUsuario = usuarioService.updateUsuario(id, usuarioDetails);
            return ResponseEntity.ok(updatedUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}") // Maneja solicitudes DELETE a /api/users/{id}
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}") // Obtener usuario por email
    public ResponseEntity<Usuario> getUsuarioByEmail(@PathVariable String email) {
        return usuarioService.getUsuarioByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipoUsuario}") // Obtener usuarios por tipo
    public List<Usuario> getUsuariosByTipoUsuario(@PathVariable String tipoUsuario) {
        try {
            TipoUsuario tipoEnum = TipoUsuario.valueOf(tipoUsuario.toUpperCase());
            return usuarioService.getUsuariosByTipoUsuario(tipoEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de usuario no válido: " + tipoUsuario);
        }
    }

    @GetMapping("/search") // Buscar usuarios por nombre y apellido
    public List<Usuario> searchUsuarios(@RequestParam String nombre, @RequestParam String apellido) {
        return usuarioService.searchUsuariosByNombreAndApellido(nombre, apellido);
    }

    // Endpoint para que otros microservicios verifiquen la existencia de un usuario por ID
    @GetMapping("/existe/{id}")
    public boolean existeUsuario(@PathVariable Long id) {
        return usuarioService.existeUsuario(id);
    }
}
