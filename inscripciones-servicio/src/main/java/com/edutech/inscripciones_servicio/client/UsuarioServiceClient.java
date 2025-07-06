package com.edutech.inscripciones_servicio.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient indica que esta interfaz es un cliente Feign.
// 'name' debe coincidir con el 'spring.application.name' del servicio al que se conecta (usuario-service).
// 'url' es la URL base del servicio de usuarios (útil para desarrollo local o si no usas Service Discovery).
@FeignClient(name = "usuario-service", url = "http://localhost:8082")
public interface UsuarioServiceClient {

    // Define un método que mapea a un endpoint en el usuario-service.
    // Este método simula la verificación de existencia de un usuario por su ID.
    // En un servicio real, podrías tener un endpoint más específico para esto,
    // o simplemente llamar a `GET /api/users/{id}` y verificar si la respuesta es 200 OK o 404 Not Found.
    // Para simplificar, asumimos un endpoint que devuelve un booleano para la existencia.
    // NOTA: Este endpoint 'existe/{id}' debe ser implementado en el UserController del usuario-service
    // para que esta llamada funcione correctamente. Si no existe, deberás ajustarlo.
    @GetMapping("/api/users/existe/{id}")
    boolean existeUsuario(@PathVariable("id") Long id);
}
