package com.arteaga.stream.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 140, nullable = false)
    private String contenido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Hilo hilo;

    private Instant createdAt = Instant.now();

    // Constructors
    public Post() {}
    public Post(String contenido, Usuario autor, Hilo hilo) {
        this.contenido = contenido;
        this.autor = autor;
        this.hilo = hilo;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }
    public Hilo getHilo() { return hilo; }
    public void setHilo(Hilo hilo) { this.hilo = hilo; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
