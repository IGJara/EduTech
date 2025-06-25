package com.edutech.login_servicio.controller; // PAQUETE CORRECTO: login_servicio

import com.edutech.login_servicio.model.User; // IMPORT CORRECTO: login_servicio.model.User
import com.edutech.login_servicio.service.AuthService; // IMPORT CORRECTO: login_servicio.service.AuthService
import org.springframework.beans.factory.annotation.Autowired; // Añadido @Autowired para constructor
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

    @Autowired // Se recomienda para la inyección por constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) { // TIPO CORRECTO: User
        try {
            User registeredUser = authService.registerUser(user); // TIPO CORRECTO: User
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginUser) { // TIPO CORRECTO: User
        Optional<User> authenticatedUser = authService.authenticate(loginUser.getUsername(), loginUser.getPassword()); // TIPO CORRECTO: Optional<User>

        if (authenticatedUser.isPresent()) {
            // En una aplicación real, aquí se emitiría un JWT (JSON Web Token)
            Map<String, String> response = new HashMap<>();
            response.put("message", "Autenticación exitosa");
            response.put("username", authenticatedUser.get().getUsername());
            response.put("role", authenticatedUser.get().getRole().name());
            response.put("userId", authenticatedUser.get().getId().toString());
            // response.put("token", "YOUR_JWT_TOKEN_HERE"); // Aquí iría el JWT real
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
