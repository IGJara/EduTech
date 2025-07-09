package com.edutech.curso_servicio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.openfeign.EnableFeignClients; // Uncomment if this service uses Feign Clients

@SpringBootApplication // Main Spring Boot annotation
// @EnableFeignClients // Uncomment if this service uses Feign Clients to call other services
public class CursoServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(CursoServicioApplication.class, args);
    }
}