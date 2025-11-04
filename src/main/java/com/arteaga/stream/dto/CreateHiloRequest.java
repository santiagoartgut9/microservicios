package com.arteaga.stream.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateHiloRequest {
    @NotBlank(message = "El título es requerido")
    private String titulo;
    
    @NotNull(message = "El ID del creador es requerido")
    private Long creadorId;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public Long getCreadorId() { return creadorId; }
    public void setCreadorId(Long creadorId) { this.creadorId = creadorId; }
}