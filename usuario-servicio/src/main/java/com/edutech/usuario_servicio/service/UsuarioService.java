package com.edutech.usuario_servicio.service; // Paquete correcto para coincidir con la carpeta: usuario_servicio

import com.edutech.usuario_servicio.model.RolUsuario; // Importa la clase RolUsuario (coincide con RolUsuario.java)
import com.edutech.usuario_servicio.model.Usuario; // Importa la clase Usuario (coincide con Usuario.java)
import com.edutech.usuario_servicio.repository.UsuarioRepository; // Importa la interfaz UsuarioRepository (coincide con UsuarioRepository.java)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio
public class UsuarioService { // CLASE: UsuarioService (coincide con UsuarioService.java)

    private final UsuarioRepository usuarioRepository; // Usa la interfaz UsuarioRepository

    @Autowired // Inyección de dependencia del UsuarioRepository
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getAllUsuarios() { // Retorna una lista de objetos Usuario
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) { // Retorna un Optional de Usuario
        return usuarioRepository.findById(id);
    }

    public Usuario createUsuario(Usuario usuario) { // Crea un nuevo Usuario
        // Validaciones de negocio:
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }
        // En una aplicación real: encriptar la contraseña antes de guardar
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuarioDetails) { // Actualiza un Usuario existente
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setUsername(usuarioDetails.getUsername());
                    usuario.setEmail(usuarioDetails.getEmail());
                    usuario.setRole(usuarioDetails.getRole());
                    // Puedes añadir más campos a actualizar aquí si es necesario
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public void deleteUsuario(Long id) { // Elimina un Usuario por ID
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> getUsuariosByRole(RolUsuario role) { // Busca Usuarios por RolUsuario
        // Es más eficiente si defines un método findByRole(RolUsuario role) en UsuarioRepository,
        // pero esta implementación filtra después de obtener todos los usuarios.
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getRole().equals(role))
                .toList();
    }
}
