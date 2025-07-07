package com.edutech.usuario_servicio;

// Importaciones de JUnit y Mockito
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor; // Para capturar argumentos pasados a mocks

// Importaciones de las clases de tu proyecto
import com.edutech.usuario_servicio.model.Usuario;
import com.edutech.usuario_servicio.model.TipoUsuario;
import com.edutech.usuario_servicio.repository.UsuarioRepository;
import com.edutech.usuario_servicio.service.UsuarioService;

// Importaciones estáticas para aserciones y métodos de Mockito
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Arrays; // Para Arrays.asList

@ExtendWith(MockitoExtension.class) // Habilita la integración de Mockito con JUnit 5
public class UsuarioServiceTest {

    @Mock // Mock del repositorio de usuarios
    private UsuarioRepository usuarioRepository;

    @InjectMocks // Inyecta los mocks en la instancia de UsuarioService
    private UsuarioService usuarioService;

    // Datos de prueba comunes
    private Usuario usuarioExistente;
    private Usuario nuevoUsuario;

    // Este método se ejecuta antes de cada prueba
    @BeforeEach
    void setUp() {
        // Inicializa un objeto de usuario existente
        usuarioExistente = new Usuario(1L, "existente@example.com", "password123", "Juan", "Perez", LocalDate.of(1990, 5, 10), TipoUsuario.ESTUDIANTE, "Calle Falsa 123", "555-1234");
        // Inicializa un nuevo objeto de usuario para creación
        nuevoUsuario = new Usuario(null, "nuevo@example.com", "newpass", "Maria", "Gomez", LocalDate.of(1995, 8, 20), null, "Avenida Siempre Viva 742", "555-5678");
    }

    @Test // Prueba para obtener todos los usuarios
    void testGetAllUsuarios() {
        // Configurar el mock para que devuelva una lista de usuarios
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioExistente, nuevoUsuario));

        // Llamar al método del servicio
        List<Usuario> usuarios = usuarioService.getAllUsuarios();

        // Verificar los resultados
        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
        verify(usuarioRepository, times(1)).findAll(); // Verificar que findAll fue llamado una vez
    }

    @Test // Prueba para obtener un usuario por ID existente
    void testGetUsuarioById_Found() {
        // Configurar el mock para que devuelva un usuario cuando se busca por ID
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));

        // Llamar al método del servicio
        Optional<Usuario> foundUsuario = usuarioService.getUsuarioById(1L);

        // Verificar los resultados
        assertTrue(foundUsuario.isPresent());
        assertEquals(1L, foundUsuario.get().getId());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test // Prueba para obtener un usuario por ID no existente
    void testGetUsuarioById_NotFound() {
        // Configurar el mock para que devuelva un Optional vacío (no encontrado)
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Llamar al método del servicio
        Optional<Usuario> foundUsuario = usuarioService.getUsuarioById(99L);

        // Verificar los resultados
        assertFalse(foundUsuario.isPresent());
        verify(usuarioRepository, times(1)).findById(99L);
    }

    @Test // Prueba para una creación de usuario exitosa
    void testCreateUsuario_Success() {
        // 1. Configurar el escenario (Given)
        // Simular que el email no está registrado
        when(usuarioRepository.findByEmail(nuevoUsuario.getEmail())).thenReturn(Optional.empty());

        // Usar ArgumentCaptor para capturar el objeto Usuario que se pasa al método save
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

        // Simular el guardado del usuario: captura el argumento y devuelve el mismo objeto con un ID simulado
        when(usuarioRepository.save(usuarioCaptor.capture())).thenAnswer(invocation -> {
            Usuario savedUsuario = invocation.getArgument(0);
            savedUsuario.setId(1L); // Asignar un ID simulado por el repositorio
            return savedUsuario;
        });

        // 2. Ejecutar la acción a probar (When)
        Usuario createdUsuario = usuarioService.createUsuario(nuevoUsuario);

        // 3. Verificar los resultados (Then)
        assertNotNull(createdUsuario, "El usuario creado no debería ser nulo");
        assertNotNull(createdUsuario.getId(), "El ID del usuario debería haberse asignado");
        assertEquals(nuevoUsuario.getEmail(), createdUsuario.getEmail(), "El email debería coincidir");
        assertEquals(TipoUsuario.ESTUDIANTE, createdUsuario.getTipoUsuario(), "El tipo de usuario debería ser ESTUDIANTE por defecto");

        // Verificar que findByEmail y save fueron llamados
        verify(usuarioRepository, times(1)).findByEmail(nuevoUsuario.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));

        // Verificar el objeto capturado para asegurar que el servicio lo modificó correctamente
        Usuario capturedUsuario = usuarioCaptor.getValue();
        assertEquals(TipoUsuario.ESTUDIANTE, capturedUsuario.getTipoUsuario());
    }

    @Test // Prueba para el caso en que el email ya está registrado
    void testCreateUsuario_EmailAlreadyRegistered() {
        // 1. Configurar el escenario (Given)
        // Simular que el email ya está registrado
        when(usuarioRepository.findByEmail(usuarioExistente.getEmail())).thenReturn(Optional.of(usuarioExistente));

        // 2. Ejecutar la acción a probar y verificar que lanza una excepción (When & Then)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.createUsuario(usuarioExistente);
        });

        // Verificar el mensaje de la excepción
        assertEquals("El email " + usuarioExistente.getEmail() + " ya está registrado.", exception.getMessage(), "El mensaje de error debería indicar que el email ya está registrado");

        // Verificar que el método save del repositorio NUNCA fue llamado
        verify(usuarioRepository, never()).save(any(Usuario.class));
        // Verificar que findByEmail SÍ fue llamado
        verify(usuarioRepository, times(1)).findByEmail(usuarioExistente.getEmail());
    }

    @Test // Prueba para actualizar un usuario existente
    void testUpdateUsuario_Success() {
        // Detalles para la actualización
        Usuario updatedDetails = new Usuario(
                usuarioExistente.getId(), "updated@example.com", "newpass123", "Juanito", "Perez",
                LocalDate.of(1990, 5, 10), TipoUsuario.PROFESOR, "Nueva Calle 456", "555-9876"
        );

        // Configurar el mock para encontrar el usuario existente y luego guardarlo
        when(usuarioRepository.findById(usuarioExistente.getId())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(updatedDetails);

        // Ejecutar el método del servicio
        Usuario result = usuarioService.updateUsuario(usuarioExistente.getId(), updatedDetails);

        // Verificar los resultados
        assertNotNull(result);
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("Juanito", result.getNombre());
        assertEquals(TipoUsuario.PROFESOR, result.getTipoUsuario());

        verify(usuarioRepository, times(1)).findById(usuarioExistente.getId());
        verify(usuarioRepository, times(1)).save(usuarioExistente); // Se guarda la instancia existente modificada
    }

    @Test // Prueba para actualizar un usuario no existente
    void testUpdateUsuario_NotFound() {
        // Detalles para la actualización
        Usuario updatedDetails = new Usuario(
                99L, "nonexistent@example.com", "pass", "Fake", "User",
                LocalDate.now(), TipoUsuario.ESTUDIANTE, null, null
        );
        // Configurar el mock para que no se encuentre el usuario
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Verificar que se lanza una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.updateUsuario(99L, updatedDetails);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test // Prueba para eliminar un usuario existente
    void testDeleteUsuario_Success() {
        // Configurar el mock para que el usuario exista
        when(usuarioRepository.existsById(usuarioExistente.getId())).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(usuarioExistente.getId());

        // Ejecutar el método del servicio
        usuarioService.deleteUsuario(usuarioExistente.getId());

        // Verificar
        verify(usuarioRepository, times(1)).existsById(usuarioExistente.getId());
        verify(usuarioRepository, times(1)).deleteById(usuarioExistente.getId());
    }

    @Test // Prueba para eliminar un usuario no existente
    void testDeleteUsuario_NotFound() {
        // Configurar el mock para que el usuario NO exista
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        // Verificar que se lanza una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.deleteUsuario(99L);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Usuario no encontrado con ID: 99", exception.getMessage());
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test // Prueba para obtener un usuario por email
    void testGetUsuarioByEmail_Found() {
        when(usuarioRepository.findByEmail(usuarioExistente.getEmail())).thenReturn(Optional.of(usuarioExistente));

        Optional<Usuario> foundUsuario = usuarioService.getUsuarioByEmail(usuarioExistente.getEmail());

        assertTrue(foundUsuario.isPresent());
        assertEquals(usuarioExistente.getEmail(), foundUsuario.get().getEmail());
        verify(usuarioRepository, times(1)).findByEmail(usuarioExistente.getEmail());
    }

    @Test // Prueba para obtener un usuario por email no existente
    void testGetUsuarioByEmail_NotFound() {
        when(usuarioRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<Usuario> foundUsuario = usuarioService.getUsuarioByEmail("nonexistent@example.com");

        assertFalse(foundUsuario.isPresent());
        verify(usuarioRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test // Prueba para obtener usuarios por tipo
    void testGetUsuariosByTipoUsuario() {
        when(usuarioRepository.findByTipoUsuario(TipoUsuario.ESTUDIANTE)).thenReturn(Arrays.asList(usuarioExistente));

        List<Usuario> usuarios = usuarioService.getUsuariosByTipoUsuario(TipoUsuario.ESTUDIANTE);

        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals(TipoUsuario.ESTUDIANTE, usuarios.get(0).getTipoUsuario());
        verify(usuarioRepository, times(1)).findByTipoUsuario(TipoUsuario.ESTUDIANTE);
    }

    @Test // Prueba para buscar usuarios por nombre y apellido
    void testSearchUsuariosByNombreAndApellido() {
        when(usuarioRepository.findByNombreContainingIgnoreCaseAndApellidoContainingIgnoreCase("juan", "perez")).thenReturn(Arrays.asList(usuarioExistente));

        List<Usuario> usuarios = usuarioService.searchUsuariosByNombreAndApellido("juan", "perez");

        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("Juan", usuarios.get(0).getNombre());
        assertEquals("Perez", usuarios.get(0).getApellido());
        verify(usuarioRepository, times(1)).findByNombreContainingIgnoreCaseAndApellidoContainingIgnoreCase("juan", "perez");
    }

    @Test // Prueba para verificar la existencia de un usuario por ID
    void testExisteUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        boolean existe = usuarioService.existeUsuario(1L);
        assertTrue(existe);
        verify(usuarioRepository, times(1)).existsById(1L);

        when(usuarioRepository.existsById(99L)).thenReturn(false);
        boolean noExiste = usuarioService.existeUsuario(99L);
        assertFalse(noExiste);
        verify(usuarioRepository, times(1)).existsById(99L);
    }
}
