package com.edutech.inscripciones_servicio.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient indica que esta interfaz es un cliente Feign.
// 'name' debe coincidir con el 'spring.application.name' del servicio al que se conecta (cursos-service).
// 'url' es la URL base del servicio de cursos (útil para desarrollo local o si no usas Service Discovery).
@FeignClient(name = "cursos-service", url = "http://localhost:8083")
public interface CursoServiceClient {

    // Define un método que mapea a un endpoint en el cursos-service.
    // Similar al UsuarioServiceClient, este método simula la verificación de existencia de un curso por su ID.
    // NOTA: Este endpoint 'existe/{id}' debe ser implementado en el CourseController del cursos-service
    // para que esta llamada funcione correctamente. Si no existe, deberás ajustarlo.
    @GetMapping("/api/cursos/existe/{id}")
    boolean existeCurso(@PathVariable("id") Long id);
}
