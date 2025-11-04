package com.arteaga.stream.controller;

import com.arteaga.stream.dto.CreateUserRequest;
import com.arteaga.stream.model.Usuario;
import com.arteaga.stream.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {
    private final UsuarioService service;
    
    public UsuarioController(UsuarioService service) { 
        this.service = service; 
    }

    // Register user (username + password)
    @PostMapping
    public ResponseEntity<?> create(@Validated @RequestBody CreateUserRequest req) {
        try {
            // Verificar si el usuario ya existe
            if (service.findByUsername(req.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El usuario ya existe"));
            }
            
            // register with password hashing
            Usuario saved = service.registerUser(req.getUsername(), req.getDisplayName(), req.getEmail(), req.getPassword());
            return ResponseEntity.created(URI.create("/api/users/" + saved.getId()))
                    .body(Map.of(
                        "id", saved.getId(), 
                        "username", saved.getUsername(), 
                        "displayName", saved.getDisplayName()
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al crear usuario: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> list() { 
        return ResponseEntity.ok(service.list()); 
    }
}