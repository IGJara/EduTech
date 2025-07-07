package com.edutech.soporte_servicio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; // Importar para habilitar Feign Clients

@SpringBootApplication // Anotación principal para una aplicación Spring Boot
@EnableFeignClients // HABILITA LOS CLIENTES FEIGN DEFINIDOS EN EL PAQUETE 'client'
public class SoporteServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoporteServicioApplication.class, args);
    }

}
