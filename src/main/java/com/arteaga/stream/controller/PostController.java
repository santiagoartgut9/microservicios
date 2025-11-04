package com.arteaga.stream.controller;

import com.arteaga.stream.dto.CreatePostRequest;
import com.arteaga.stream.dto.PostResponse;
import com.arteaga.stream.model.Hilo;
import com.arteaga.stream.model.Post;
import com.arteaga.stream.model.Usuario;
import com.arteaga.stream.service.HiloService;
import com.arteaga.stream.service.PostService;
import com.arteaga.stream.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final UsuarioService usuarioService;
    private final HiloService hiloService;

    public PostController(PostService postService, UsuarioService usuarioService, HiloService hiloService) {
        this.postService = postService;
        this.usuarioService = usuarioService;
        this.hiloService = hiloService;
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> feed() {
        return ResponseEntity.ok(postService.feed());
    }

    @GetMapping("/hilo/{id}")
    public ResponseEntity<List<PostResponse>> byHilo(@PathVariable Long id) {
        return ResponseEntity.ok(postService.byHilo(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Validated @RequestBody CreatePostRequest req, Principal principal) {
        try {
            if(principal == null) {
                return ResponseEntity.status(401).body(Map.of("error","No autenticado"));
            }

            // Validar longitud del contenido
            if(req.getContenido().length() > 140) {
                return ResponseEntity.badRequest().body(Map.of("error", "El post no puede exceder 140 caracteres"));
            }

            String username = principal.getName();
            Usuario autor = usuarioService.findByUsername(username)
                    .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.NOT_FOUND, "Autor no encontrado"));

            // Assign to global hilo
            Hilo hilo = hiloService.getOrCreateGlobal();

            Post post = new Post(req.getContenido(), autor, hilo);
            Post saved = postService.save(post);

            PostResponse resp = new PostResponse();
            resp.setId(saved.getId());
            resp.setContenido(saved.getContenido());
            resp.setAutorId(autor.getId());
            resp.setAutorUsername(autor.getUsername());
            resp.setHiloId(hilo.getId());
            resp.setCreatedAt(saved.getCreatedAt());

            return ResponseEntity.created(URI.create("/api/posts/" + saved.getId())).body(resp);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al crear post: " + e.getMessage()));
        }
    }
}