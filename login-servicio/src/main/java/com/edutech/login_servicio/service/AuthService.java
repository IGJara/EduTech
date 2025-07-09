package com.edutech.login_servicio.service; // PAQUETE CORRECTO: login_servicio

import com.edutech.login_servicio.model.User; // IMPORT CORRECTO: login_servicio.model.User
import com.edutech.login_servicio.repository.UserRepository; // IMPORT CORRECTO: login_servicio.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired; // Añadido @Autowired para constructor, aunque no es estrictamente necesario en Spring Boot 2.x+
import org.springframework.stereotype.Service;

import java.util.List; // Importar List para getAllUsers
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository; // TIPO CORRECTO: UserRepository

    @Autowired // Se recomienda para la inyección por constructor
    public AuthService(UserRepository userRepository) { // TIPO CORRECTO: UserRepository
        this.userRepository = userRepository;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param user El objeto User a registrar.
     * @return El usuario registrado.
     * @throws RuntimeException Si el nombre de usuario ya está en uso.
     */
    public User registerUser(User user) { // TIPO CORRECTO: User
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        // En una aplicación real: ¡¡¡ENCRIPTAR LA CONTRASEÑA ANTES DE GUARDARLA!!!
        return userRepository.save(user);
    }

    /**
     * Autentica un usuario con su nombre de usuario y contraseña.
     * @param username El nombre de usuario.
     * @param password La contraseña.
     * @return Un Optional que contiene el usuario autenticado si las credenciales son válidas, o vacío si no.
     */
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

    /**
     * Obtiene todos los usuarios registrados.
     * @return Una lista de todos los usuarios.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Obtiene un usuario por su ID.
     * @param id El ID del usuario.
     * @return Un Optional que contiene el usuario si se encuentra, o vacío si no.
     */
    public Optional<User> getUserById(Long id) { // TIPO CORRECTO: Optional<User>
        return userRepository.findById(id);
    }

    /**
     * Actualiza la información de un usuario existente.
     * @param id El ID del usuario a actualizar.
     * @param userDetails Los detalles actualizados del usuario.
     * @return El usuario actualizado.
     * @throws RuntimeException Si el usuario no es encontrado.
     */
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    // Actualizar solo los campos permitidos o necesarios
                    existingUser.setUsername(userDetails.getUsername());
                    existingUser.setPassword(userDetails.getPassword()); // Encriptar en un caso real
                    existingUser.setRole(userDetails.getRole());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("Usuario de login no encontrado con ID: " + id));
    }

    /**
     * Elimina un usuario por su ID.
     * @param id El ID del usuario a eliminar.
     * @throws RuntimeException Si el usuario no es encontrado.
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario de login no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
