package com.example.serveur.model;

// TypeAlerte.java
import jakarta.persistence.*;

@Entity
@Table(name = "TypeAlerte")
public class TypeAlerte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_TypeAlerte;

    @Column(nullable = false, unique = true)
    private String Zone;

    // getters & setters
}

