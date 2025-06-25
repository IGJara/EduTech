package com.edutech.login_servicio.service; // PAQUETE CORRECTO: login_servicio

import com.edutech.login_servicio.model.User; // IMPORT CORRECTO: login_servicio.model.User
import com.edutech.login_servicio.repository.UserRepository; // IMPORT CORRECTO: login_servicio.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired; // Añadido @Autowired para constructor, aunque no es estrictamente necesario en Spring Boot 2.x+
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository; // TIPO CORRECTO: UserRepository

    @Autowired // Se recomienda para la inyección por constructor
    public AuthService(UserRepository userRepository) { // TIPO CORRECTO: UserRepository
        this.userRepository = userRepository;
    }

    public User registerUser(User user) { // TIPO CORRECTO: User
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        // En una aplicación real: ¡¡¡ENCRIPTAR LA CONTRASEÑA ANTES DE GUARDARLA!!!
        return userRepository.save(user);
    }

    public Optional<User> authenticate(String username, String password) { // TIPO CORRECTO: Optional<User>
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get(); // TIPO CORRECTO: User
            // En una aplicación real: COMPARAR CONTRASEÑAS ENCRIPTADAS (ej. con BCryptPasswordEncoder)
            if (user.getPassword().equals(password)) { // Esto es solo para pruebas simples, NO para producción
                return Optional.of(user);
            }
        }
        return Optional.empty(); // Autenticación fallida
    }
    
    public Optional<User> getUserById(Long id) { // TIPO CORRECTO: Optional<User>
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario de login no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
