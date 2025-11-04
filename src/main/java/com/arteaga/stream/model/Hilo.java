package com.arteaga.stream.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "hilos")
public class Hilo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Usuario creador;
    
    

    private Instant createdAt = Instant.now();

    // Constructors
    public Hilo() {}
    public Hilo(String titulo, Usuario creador) {
        this.titulo = titulo;
        this.creador = creador;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Usuario getCreador() { return creador; }
    public void setCreador(Usuario creador) { this.creador = creador; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
