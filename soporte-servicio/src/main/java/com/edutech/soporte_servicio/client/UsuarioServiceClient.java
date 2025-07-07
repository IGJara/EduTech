package com.edutech.soporte_servicio.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Interfaz Feign Client para comunicarse con el microservicio 'usuario-servicio'.
 * Permite realizar llamadas HTTP de forma declarativa.
 *
 * 'name' debe coincidir con el 'spring.application.name' del servicio al que se conecta.
 * 'url' es la URL base del servicio de usuarios (útil para desarrollo local o si no usas Service Discovery).
 * En un entorno de producción con Service Discovery (ej. Eureka), solo necesitarías el 'name'.
 */
@FeignClient(name = "usuario-servicio", url = "http://localhost:8082")
public interface UsuarioServiceClient {

    /**
     * Define un método que mapea a un endpoint en el usuario-servicio.
     * Este método simula la verificación de existencia de un usuario por su ID.
     *
     * NOTA: Este endpoint "/api/users/existe/{id}" debe ser implementado en el UserController
     * del usuario-servicio y devolver un booleano.
     *
     * Ejemplo de implementación en UserController (usuario-servicio):
     * @GetMapping("/existe/{id}")
     * public boolean existeUsuario(@PathVariable Long id) {
     * return userService.getUserById(id).isPresent();
     * }
     */
    @GetMapping("/api/users/existe/{id}")
    boolean existeUsuario(@PathVariable("id") Long id);
}
