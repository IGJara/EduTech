package com.edutech.usuario_servicio.service;

import com.edutech.usuario_servicio.model.RolUsuario; // Se actualizó el tipo de enum
import com.edutech.usuario_servicio.model.Usuario; // Se actualizó el tipo de entidad
import com.edutech.usuario_servicio.repository.UsuarioRepository; // Se actualizó el nombre del repositorio
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Importar Collectors

@Service // Indica que esta clase es un componente de servicio de Spring
public class UserService { // Se mantuvo el nombre 'UserService' por convención, aunque se puede cambiar a 'UsuarioService' si se prefiere

    private final UsuarioRepository usuarioRepository; // Se actualizó el nombre del repositorio

    // Inyección de dependencia del UsuarioRepository a través del constructor
    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Obtiene una lista de todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() { // Antes 'getAllUsers'
        return usuarioRepository.findAll();
    }

    // Obtiene un usuario específico por su ID
    public Optional<Usuario> obtenerUsuarioPorId(Long id) { // Antes 'getUserById'
        return usuarioRepository.findById(id);
    }

    // Crea un nuevo usuario
    public Usuario crearUsuario(Usuario usuario) { // Antes 'createUser', se actualizó el tipo de entidad
        // Validación: verificar si el nombre de usuario o el correo electrónico ya existen
        if (usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())) { // Antes 'existsByUsername'
            throw new RuntimeException("El nombre de usuario ya existe.");
        }
        if (usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())) { // Antes 'existsByEmail'
            throw new RuntimeException("El email ya está registrado.");
        }
        // En una aplicación real: encriptar la contraseña antes de guardar.
        // Ejemplo (pseudo-código): usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        // Guarda el nuevo usuario en la base de datos
        return usuarioRepository.save(usuario);
    }

    // Actualiza los detalles de un usuario existente por su ID
    public Usuario actualizarUsuario(Long id, Usuario detallesUsuario) { // Antes 'updateUser', se actualizó el tipo de entidad y nombre de parámetro
        // Busca el usuario por su ID. Si no se encuentra, lanza una excepción.
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    // Actualiza los campos del usuario existente con los nuevos detalles
                    usuarioExistente.setNombreUsuario(detallesUsuario.getNombreUsuario()); // Antes 'setUsername'
                    usuarioExistente.setCorreoElectronico(detallesUsuario.getCorreoElectronico()); // Antes 'setEmail'
                    usuarioExistente.setRol(detallesUsuario.getRol()); // Antes 'setRole'
                    // Nota: No se actualiza la contraseña directamente desde este método CRUD básico.
                    // La actualización de contraseña tendría su propio endpoint y lógica específica.
                    // Guarda los cambios del usuario actualizado
                    return usuarioRepository.save(usuarioExistente);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    // Elimina un usuario por su ID
    public void eliminarUsuario(Long id) { // Antes 'deleteUser'
        // Verifica si el usuario existe antes de intentar eliminarlo
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        // Elimina el usuario de la base de datos
        usuarioRepository.deleteById(id);
    }

    // Obtiene una lista de usuarios filtrados por rol
    public List<Usuario> obtenerUsuariosPorRol(RolUsuario rol) { // Antes 'getUsersByRole', se actualizó el tipo de enum y nombre de parámetro
        // Si el repositorio no tiene un método findByRol(), se puede filtrar en memoria.
        // Opción recomendada para el repositorio: List<Usuario> findByRol(RolUsuario rol);
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getRol().equals(rol))
                .collect(Collectors.toList());
    }
}
