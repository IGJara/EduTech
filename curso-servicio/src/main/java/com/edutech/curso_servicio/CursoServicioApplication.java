package com.edutech.curso_servicio; // El paquete base para el microservicio de cursos

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Anotación que marca esta clase como una aplicación Spring Boot
public class CursoServicioApplication { // El nombre de la clase principal de la aplicación

    public static void main(String[] args) {
        // Método principal que arranca la aplicación Spring Boot
        SpringApplication.run(CursoServicioApplication.class, args);
    }

}
