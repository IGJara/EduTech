package com.edutech.login_service.controller;

import com.edutech.login_service.model.User; // CORREGIDO: Usar 'User'
import com.edutech.login_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) { // CORREGIDO: Usar 'User'
        try {
            User registeredUser = authService.registerUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginUser) { // CORREGIDO: Usar 'User'
        Optional<User> authenticatedUser = authService.authenticate(loginUser.getUsername(), loginUser.getPassword());

        if (authenticatedUser.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Autenticación exitosa");
            response.put("username", authenticatedUser.get().getUsername());
            response.put("role", authenticatedUser.get().getRole().name());
            response.put("userId", authenticatedUser.get().getId().toString());
            // response.put("token", "YOUR_JWT_TOKEN_HERE"); // Aquí iría el JWT
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Credenciales inválidas");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }
    
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            authService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
