package com.edutech.login_servicio; // PAQUETE CORRECTO: login_servicio

import com.edutech.login_servicio.model.User; // IMPORT CORRECTO: login_servicio.model.User
import com.edutech.login_servicio.model.Role; // IMPORT CORRECTO: login_servicio.model.Role
import com.edutech.login_servicio.repository.UserRepository; // IMPORT CORRECTO: login_servicio.repository.UserRepository
import com.edutech.login_servicio.service.AuthService; // IMPORT CORRECTO: login_servicio.service.AuthService

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName; // Para nombres de pruebas más descriptivos
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor; // Para capturar argumentos

import java.util.Arrays; // Para Arrays.asList
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita la integración de Mockito con JUnit 5
public class AuthServiceTest {

    @Mock // Crea un mock del UserRepository
    private UserRepository userRepository;

    @InjectMocks // Inyecta los mocks en la instancia de AuthService
    private AuthService authService;

    private User existingUser;
    private User newUser;

    @BeforeEach // Este método se ejecuta antes de cada prueba
    void setUp() {
        // Usuarios de prueba
        existingUser = new User(1L, "existinguser", "password", Role.ALUMNO);
        newUser = new User(null, "newuser", "newpass", Role.PROFESOR);
    }

    // --- Pruebas de REGISTER ---

    @Test
    @DisplayName("Registrar usuario - Éxito")
    void testRegisterUser_Success() {
        // Simular que el nombre de usuario no existe
        when(userRepository.existsByUsername(newUser.getUsername())).thenReturn(false);
        // Simular que save devuelve el usuario guardado (con ID asignado)
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(2L); // Simular ID asignado por la BD
            return userToSave;
        });

        User registeredUser = authService.registerUser(newUser);

        assertNotNull(registeredUser);
        assertNotNull(registeredUser.getId());
        assertEquals(newUser.getUsername(), registeredUser.getUsername());
        assertEquals(newUser.getRole(), registeredUser.getRole());

        verify(userRepository, times(1)).existsByUsername(newUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Registrar usuario - Nombre de usuario ya existe")
    void testRegisterUser_UsernameAlreadyExists() {
        // Simular que el nombre de usuario ya existe
        when(userRepository.existsByUsername(existingUser.getUsername())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(existingUser);
        });

        assertEquals("El nombre de usuario ya está en uso.", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(existingUser.getUsername());
        verify(userRepository, never()).save(any(User.class)); // Verificar que save NO fue llamado
    }

    // --- Pruebas de AUTHENTICATE ---

    @Test
    @DisplayName("Autenticar usuario - Éxito")
    void testAuthenticate_Success() {
        // Simular que el usuario es encontrado por username
        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));

        Optional<User> authenticatedUser = authService.authenticate(existingUser.getUsername(), existingUser.getPassword());

        assertTrue(authenticatedUser.isPresent());
        assertEquals(existingUser.getUsername(), authenticatedUser.get().getUsername());
        verify(userRepository, times(1)).findByUsername(existingUser.getUsername());
    }

    @Test
    @DisplayName("Autenticar usuario - Credenciales inválidas (contraseña incorrecta)")
    void testAuthenticate_InvalidPassword() {
        // Simular que el usuario es encontrado por username
        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));

        Optional<User> authenticatedUser = authService.authenticate(existingUser.getUsername(), "wrongpass");

        assertFalse(authenticatedUser.isPresent());
        verify(userRepository, times(1)).findByUsername(existingUser.getUsername());
    }

    @Test
    @DisplayName("Autenticar usuario - Usuario no encontrado")
    void testAuthenticate_UserNotFound() {
        // Simular que el usuario no es encontrado por username
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Optional<User> authenticatedUser = authService.authenticate("nonexistent", "anypass");

        assertFalse(authenticatedUser.isPresent());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    // --- NUEVAS PRUEBAS CRUD ---

    // --- READ (getAllUsers) ---
    @Test
    @DisplayName("Obtener todos los usuarios - Éxito")
    void testGetAllUsers_Success() {
        // Simular que findAll devuelve una lista de usuarios
        when(userRepository.findAll()).thenReturn(Arrays.asList(existingUser, newUser));

        List<User> users = authService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains(existingUser));
        assertTrue(users.contains(newUser));
        verify(userRepository, times(1)).findAll();
    }

    // --- READ (getUserById) ---
    @Test
    @DisplayName("Obtener usuario por ID - Encontrado")
    void testGetUserById_Found() {
        // Simular que findById devuelve el usuario existente
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        Optional<User> foundUser = authService.getUserById(existingUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(existingUser.getId(), foundUser.get().getId());
        verify(userRepository, times(1)).findById(existingUser.getId());
    }

    @Test
    @DisplayName("Obtener usuario por ID - No encontrado")
    void testGetUserById_NotFound() {
        // Simular que findById devuelve vacío
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> foundUser = authService.getUserById(99L);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(99L);
    }

    // --- UPDATE (updateUser) ---
    @Test
    @DisplayName("Actualizar usuario - Éxito")
    void testUpdateUser_Success() {
        User updatedDetails = new User(existingUser.getId(), "updateduser", "newpassword", Role.ADMIN);

        // Simular que findById encuentra el usuario
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        // Simular que save devuelve el usuario actualizado
        when(userRepository.save(any(User.class))).thenReturn(updatedDetails);

        User result = authService.updateUser(existingUser.getId(), updatedDetails);

        assertNotNull(result);
        assertEquals(updatedDetails.getUsername(), result.getUsername());
        assertEquals(updatedDetails.getPassword(), result.getPassword());
        assertEquals(updatedDetails.getRole(), result.getRole());

        verify(userRepository, times(1)).findById(existingUser.getId());
        verify(userRepository, times(1)).save(existingUser); // Verify that the existingUser object was saved after modification
    }

    @Test
    @DisplayName("Actualizar usuario - No encontrado")
    void testUpdateUser_NotFound() {
        User updatedDetails = new User(99L, "nonexistent", "pass", Role.ALUMNO);

        // Simular que findById no encuentra el usuario
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.updateUser(99L, updatedDetails);
        });

        assertEquals("Usuario de login no encontrado con ID: 99", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(userRepository, never()).save(any(User.class));
    }

    // --- DELETE (deleteUser) ---
    @Test
    @DisplayName("Eliminar usuario - Éxito")
    void testDeleteUser_Success() {
        // Simular que el usuario existe
        when(userRepository.existsById(existingUser.getId())).thenReturn(true);
        // No necesitamos mockear deleteById porque es void y lo verificamos con verify

        authService.deleteUser(existingUser.getId());

        verify(userRepository, times(1)).existsById(existingUser.getId());
        verify(userRepository, times(1)).deleteById(existingUser.getId());
    }

    @Test
    @DisplayName("Eliminar usuario - No encontrado")
    void testDeleteUser_NotFound() {
        // Simular que el usuario no existe
        when(userRepository.existsById(anyLong())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.deleteUser(99L);
        });

        assertEquals("Usuario de login no encontrado con ID: 99", exception.getMessage());
        verify(userRepository, times(1)).existsById(99L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}
