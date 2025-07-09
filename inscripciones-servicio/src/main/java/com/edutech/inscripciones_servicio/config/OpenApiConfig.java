        package com.edutech.inscripciones_servicio.config;

        import io.swagger.v3.oas.models.OpenAPI;
        import io.swagger.v3.oas.models.info.Info;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;

        @Configuration // Indicates that this class provides configurations for the Spring context
        public class OpenApiConfig {

            @Bean // Defines a Spring bean for OpenAPI configuration
            public OpenAPI customOpenAPI() {
                return new OpenAPI()
                        .info(new Info()
                                .title("API de Inscripciones - EduTech") // API documentation title
                                .version("1.0") // API version
                                .description("Documentación de la API del microservicio de gestión de inscripciones para la plataforma EduTech.")); // API description
            }
        }
        