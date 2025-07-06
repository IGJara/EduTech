package com.edutech.login_servicio; // Asegúrate de que este paquete coincida con la ruta de tu archivo

// Importaciones de JUnit y Mockito
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Importaciones de las clases de tu proyecto (¡CRUCIALES!)
import com.edutech.login_servicio.model.Role; // Importa tu enum Role
import com.edutech.login_servicio.model.User; // Importa tu clase User
import com.edutech.login_servicio.repository.UserRepository; // Importa tu interfaz UserRepository
import com.edutech.login_servicio.service.AuthService; // ¡IMPORTACIÓN AÑADIDA/DESCOMENTADA PARA AuthService!

// Importaciones estáticas para aserciones y métodos de Mockito
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita la integración de Mockito con JUnit 5
public class AuthServiceTest { // El nombre de la clase DEBE coincidir con el nombre del archivo (AuthServiceTest.java)

    @Mock // Crea un objeto "mock" (simulado) de UserRepository
    private UserRepository userRepository;

    @InjectMocks // Inyecta los mocks creados (userRepository) en una instancia de AuthService
    private AuthService authService; // Asegúrate de que AuthService sea el nombre correcto de tu clase de servicio

    // Este método se ejecuta antes de cada prueba
    @BeforeEach
    void setUp() {
        // Aquí podrías resetear mocks o configurar un estado inicial común si fuera necesario
    }

    @Test // Marca este método como una prueba unitaria
    void testRegisterUser_Success() {
        // 1. Configurar el escenario (Given)
        // Usamos un constructor que no requiere 'id' para nuevos usuarios
        User newUser = new User(null, "testuser", "password123", Role.ALUMNO);

        // Simular el comportamiento del userRepository:
        // Cuando se llame a existsByUsername con "testuser", que devuelva false (no existe)
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        // Cuando se llame a save con cualquier User, que devuelva el mismo newUser (simulando que se guarda)
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // 2. Ejecutar la acción a probar (When)
        User registeredUser = authService.registerUser(newUser);

        // 3. Verificar los resultados (Then)
        assertNotNull(registeredUser, "El usuario registrado no debería ser nulo");
        assertEquals("testuser", registeredUser.getUsername(), "El username debería coincidir");
        assertEquals(Role.ALUMNO, registeredUser.getRole(), "El rol debería coincidir");

        // Verificar que los métodos del mock fueron llamados como esperamos
        verify(userRepository, times(1)).existsByUsername("testuser"); // existsByUsername fue llamado una vez
        verify(userRepository, times(1)).save(any(User.class)); // save fue llamado una vez
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        // 1. Configurar el escenario (Given)
        User existingUser = new User(null, "existinguser", "password", Role.ALUMNO);

        // Simular que el username ya existe en la base de datos
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // 2. Ejecutar la acción a probar y verificar que lanza una excepción (When & Then)
        // Usamos assertThrows para verificar que se lanza una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(existingUser);
        });

        // Verificar el mensaje de la excepción
        assertEquals("El nombre de usuario ya está en uso.", exception.getMessage(), "El mensaje de error debería coincidir");

        // Verificar que save NO fue llamado (porque el usuario ya existía)
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, times(1)).existsByUsername("existinguser");
    }
}
