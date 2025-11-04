package com.arteaga.stream.controller;

import com.arteaga.stream.dto.CreateUserRequest;
import com.arteaga.stream.model.Usuario;
import com.arteaga.stream.service.UsuarioService;
import com.arteaga.stream.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest req) {
        try {
            // Verificar si el usuario ya existe
            if (usuarioService.findByUsername(req.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El usuario ya existe"));
            }
            
            Usuario saved = usuarioService.registerUser(
                req.getUsername(), 
                req.getDisplayName(), 
                req.getEmail(), 
                req.getPassword()
            );
            
            // Generar token automáticamente después del registro
            String token = jwtUtil.generateToken(saved.getUsername());
            
            return ResponseEntity.ok(Map.of(
                "token", token,
                "user", Map.of(
                    "id", saved.getId(),
                    "username", saved.getUsername(),
                    "displayName", saved.getDisplayName()
                )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al registrar usuario: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        if(principal == null) {
            return ResponseEntity.status(401).body(Map.of("error","No autenticado"));
        }
        Usuario u = usuarioService.findByUsername(principal.getName()).orElse(null);
        if(u == null) {
            return ResponseEntity.status(404).body(Map.of("error","Usuario no encontrado"));
        }
        return ResponseEntity.ok(Map.of(
            "id", u.getId(), 
            "username", u.getUsername(), 
            "displayName", u.getDisplayName(), 
            "email", u.getEmail()
        ));
    }
}