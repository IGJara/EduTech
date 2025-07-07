package com.edutech.usuario_servicio.service;

import com.edutech.usuario_servicio.model.Usuario;
import com.edutech.usuario_servicio.model.TipoUsuario;
import com.edutech.usuario_servicio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio de Spring
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Constructor para inyección de dependencias
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Obtiene todos los usuarios
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtiene un usuario por su ID
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Crea un nuevo usuario
    public Usuario createUsuario(Usuario usuario) {
        // Validar que el email no esté ya registrado
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email " + usuario.getEmail() + " ya está registrado.");
        }

        // Asignar fecha de nacimiento si no está definida (ejemplo, puede ser nulo si no se requiere)
        if (usuario.getFechaNacimiento() == null) {
            // Opcional: asignar una fecha por defecto o validar que no sea nula si es un campo requerido
            // usuario.setFechaNacimiento(LocalDate.now());
        }
        // Asignar tipo de usuario por defecto si no está asignado
        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(TipoUsuario.ESTUDIANTE); // Por defecto, un nuevo usuario es estudiante
        }

        // En una aplicación real, la contraseña debería ser encriptada aquí
        // usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Guarda el nuevo usuario en la base de datos
        return usuarioRepository.save(usuario);
    }

    // Actualiza un usuario existente
    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    // Actualiza los campos relevantes
                    usuarioExistente.setEmail(usuarioDetails.getEmail());
                    usuarioExistente.setPassword(usuarioDetails.getPassword()); // Encriptar en un caso real
                    usuarioExistente.setNombre(usuarioDetails.getNombre());
                    usuarioExistente.setApellido(usuarioDetails.getApellido());
                    usuarioExistente.setFechaNacimiento(usuarioDetails.getFechaNacimiento());
                    usuarioExistente.setTipoUsuario(usuarioDetails.getTipoUsuario());
                    usuarioExistente.setDireccion(usuarioDetails.getDireccion());
                    usuarioExistente.setTelefono(usuarioDetails.getTelefono());
                    return usuarioRepository.save(usuarioExistente);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    // Elimina un usuario por su ID
    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // Obtiene un usuario por su email
    public Optional<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Obtiene usuarios por tipo
    public List<Usuario> getUsuariosByTipoUsuario(TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuario(tipoUsuario);
    }

    // Obtiene usuarios por nombre y apellido (búsqueda parcial)
    public List<Usuario> searchUsuariosByNombreAndApellido(String nombre, String apellido) {
        return usuarioRepository.findByNombreContainingIgnoreCaseAndApellidoContainingIgnoreCase(nombre, apellido);
    }

    // Método para verificar la existencia de un usuario por ID (para clientes Feign)
    public boolean existeUsuario(Long id) {
        return usuarioRepository.existsById(id);
    }
}
