package com.edutech.login_servicio.controller;

import com.edutech.login_servicio.model.UsuarioLogin; // Se actualizó el tipo de entidad
import com.edutech.login_servicio.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/auth") // Define la URL base para todos los endpoints de este controlador
public class AuthController {

    private final AuthService authService;

    // Inyección de dependencia del AuthService a través del constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro") // Maneja solicitudes POST a /api/auth/registro para registrar un nuevo usuario
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioLogin usuario) { // Antes '/register', se actualizó el tipo de entidad y nombre de método
        try {
            UsuarioLogin usuarioRegistrado = authService.registrarUsuario(usuario);
            return new ResponseEntity<>(usuarioRegistrado, HttpStatus.CREATED); // Devuelve 201 Created con el usuario registrado
        } catch (RuntimeException e) {
            Map<String, String> respuestaError = new HashMap<>();
            respuestaError.put("error", e.getMessage());
            return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST); // Devuelve 400 Bad Request en caso de error
        }
    }

    @PostMapping("/iniciar-sesion") // Maneja solicitudes POST a /api/auth/iniciar-sesion para autenticar un usuario
    public ResponseEntity<?> iniciarSesion(@RequestBody UsuarioLogin usuarioLogin) { // Antes '/login', se actualizó el tipo de entidad y nombre de método
        Optional<UsuarioLogin> usuarioAutenticado = authService.autenticar(usuarioLogin.getNombreUsuario(), usuarioLogin.getContrasena()); // Antes 'getUsername' y 'getPassword'

        if (usuarioAutenticado.isPresent()) {
            // En una aplicación real, aquí se emitiría un JWT (JSON Web Token)
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Autenticación exitosa"); // Antes 'message'
            respuesta.put("nombreUsuario", usuarioAutenticado.get().getNombreUsuario()); // Antes 'username'
            respuesta.put("rol", usuarioAutenticado.get().getRol().name()); // Antes 'role'
            respuesta.put("idUsuario", usuarioAutenticado.get().getId().toString()); // Antes 'userId'
            // respuesta.put("token", "TU_TOKEN_JWT_AQUÍ"); // Aquí iría el JWT real
            return ResponseEntity.ok(respuesta); // Devuelve 200 OK con la información del usuario autenticado
        } else {
            Map<String, String> respuestaError = new HashMap<>();
            respuestaError.put("error", "Credenciales inválidas");
            return new ResponseEntity<>(respuestaError, HttpStatus.UNAUTHORIZED); // Devuelve 401 Unauthorized
        }
    }
    
    @DeleteMapping("/usuario/{id}") // Maneja solicitudes DELETE a /api/auth/usuario/{id} para eliminar un usuario de login
    public ResponseEntity<Void> eliminarUsuarioLogin(@PathVariable Long id) { // Antes '/user', se cambió nombre de método
        try {
            authService.eliminarUsuario(id);
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Devuelve 404 Not Found si no se encuentra el usuario
        }
    }
}
