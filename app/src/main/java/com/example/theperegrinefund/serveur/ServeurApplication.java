package com.example.serveur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Annotation qui configure automatiquement Spring Boot
public class ServeurApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServeurApplication.class, args);  // Démarre le serveur
    }
}
