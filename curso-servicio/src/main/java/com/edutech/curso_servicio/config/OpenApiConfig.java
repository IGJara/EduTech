package com.edutech.curso_servicio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Indica que esta clase proporciona configuraciones para el contexto de Spring
public class OpenApiConfig {

    @Bean // Define un bean de Spring para la configuración de OpenAPI
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Cursos - EduTech") // Título de la documentación de la API
                        .version("1.0") // Versión de la API
                        .description("Documentación de la API del microservicio de gestión de cursos para la plataforma EduTech.")); // Descripción de la API
    }
}
