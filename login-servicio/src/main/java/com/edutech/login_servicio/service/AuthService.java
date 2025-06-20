package com.edutech.login_servicio.service;

import com.edutech.login_servicio.model.UsuarioLogin; // Se actualizó el tipo de entidad
import com.edutech.login_servicio.repository.UsuarioLoginRepository; // Se actualizó el nombre del repositorio
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio de Spring
public class AuthService {

    private final UsuarioLoginRepository usuarioLoginRepository; // Se actualizó el nombre del repositorio

    // Inyección de dependencia del UsuarioLoginRepository a través del constructor
    public AuthService(UsuarioLoginRepository usuarioLoginRepository) {
        this.usuarioLoginRepository = usuarioLoginRepository;
    }

    // Registra un nuevo usuario de login
    public UsuarioLogin registrarUsuario(UsuarioLogin usuario) { // Antes 'registerUser', se actualizó el tipo de entidad
        // Validación: verificar si el nombre de usuario ya está en uso
        if (usuarioLoginRepository.existsByNombreUsuario(usuario.getNombreUsuario())) { // Antes 'existsByUsername'
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        // En una aplicación real: ¡¡¡ENCRIPTAR LA CONTRASEÑA ANTES DE GUARDARLA!!!
        // Ejemplo (pseudo-código): usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        // Guarda el nuevo usuario en la base de datos
        return usuarioLoginRepository.save(usuario);
    }

    // Autentica a un usuario
    public Optional<UsuarioLogin> autenticar(String nombreUsuario, String contrasena) { // Antes 'authenticate', se actualizaron los nombres de parámetros
        Optional<UsuarioLogin> usuarioOpcional = usuarioLoginRepository.findByNombreUsuario(nombreUsuario); // Antes 'findByUsername'
        if (usuarioOpcional.isPresent()) {
            UsuarioLogin usuario = usuarioOpcional.get();
            // En una aplicación real: COMPARAR CONTRASEÑAS ENCRIPTADAS (ej. con BCryptPasswordEncoder)
            // Ejemplo (pseudo-código): if (passwordEncoder.matches(contrasena, usuario.getContrasena()))
            if (usuario.getContrasena().equals(contrasena)) { // Esto es solo para pruebas simples, NO para producción
                return Optional.of(usuario);
            }
        }
        return Optional.empty(); // Autenticación fallida
    }
    
    // Obtiene un usuario de login por su ID
    public Optional<UsuarioLogin> obtenerUsuarioPorId(Long id) { // Antes 'getUserById'
        return usuarioLoginRepository.findById(id);
    }

    // Elimina un usuario de login por su ID
    public void eliminarUsuario(Long id) { // Antes 'deleteUser'
        if (!usuarioLoginRepository.existsById(id)) {
            throw new RuntimeException("Usuario de login no encontrado con ID: " + id);
        }
        usuarioLoginRepository.deleteById(id);
    }
}
