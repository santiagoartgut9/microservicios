package com.arteaga.stream.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePostRequest {
    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 140, message = "El post no puede exceder 140 caracteres")
    private String contenido;

    public CreatePostRequest() {}

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}