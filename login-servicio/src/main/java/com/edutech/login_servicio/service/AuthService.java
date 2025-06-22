package com.edutech.login_servicio.service; // CORREGIDO: Usar 'login_servicio'

import com.edutech.login_servicio.model.User; // CORREGIDO: Usar 'login_servicio'
import com.edutech.login_servicio.repository.UserRepository; // CORREGIDO: Usar 'login_servicio'
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        // En una aplicación real: ¡¡¡ENCRIPTAR LA CONTRASEÑA ANTES DE GUARDARLA!!!
        return userRepository.save(user);
    }

    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // En una aplicación real: COMPARAR CONTRASEÑAS ENCRIPTADAS
            if (user.getPassword().equals(password)) { // Esto es solo para pruebas, no para producción
                return Optional.of(user);
            }
        }
        return Optional.empty(); // Autenticación fallida
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario de login no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
