package com.arteaga.stream.controller;

import com.arteaga.stream.dto.CreateHiloRequest;
import com.arteaga.stream.model.Hilo;
import com.arteaga.stream.model.Usuario;
import com.arteaga.stream.service.HiloService;
import com.arteaga.stream.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/hilos")
public class HiloController {
    private final HiloService hiloService;
    private final UsuarioService usuarioService;
    public HiloController(HiloService hiloService, UsuarioService usuarioService) {
        this.hiloService = hiloService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Validated @RequestBody CreateHiloRequest req) {
        Usuario creador = usuarioService.findById(req.getCreadorId())
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Hilo h = new Hilo(req.getTitulo(), creador);
        Hilo saved = hiloService.create(h);
        return ResponseEntity.created(URI.create("/api/hilos/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Hilo>> list() { return ResponseEntity.ok(hiloService.list()); }
}
