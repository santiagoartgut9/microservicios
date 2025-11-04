package com.arteaga.stream.dto;

import java.time.Instant;

public class PostResponse {
    private Long id;
    private String contenido;
    private Long autorId;
    private String autorUsername;
    private Long hiloId;
    private Instant createdAt;

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }
    public String getAutorUsername() { return autorUsername; }
    public void setAutorUsername(String autorUsername) { this.autorUsername = autorUsername; }
    public Long getHiloId() { return hiloId; }
    public void setHiloId(Long hiloId) { this.hiloId = hiloId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
