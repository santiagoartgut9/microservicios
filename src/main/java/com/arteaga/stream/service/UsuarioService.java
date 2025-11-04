package com.arteaga.stream.service;

import com.arteaga.stream.model.Usuario;
import com.arteaga.stream.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario create(Usuario u) { return repo.save(u); }

    // register user hashing password
    public Usuario registerUser(String username, String displayName, String email, String rawPassword) {
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setDisplayName(displayName);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        return repo.save(u);
    }

    public Optional<Usuario> findById(Long id) { return repo.findById(id); }

    public Optional<Usuario> findByUsername(String username) { return repo.findByUsername(username); }

    public List<Usuario> list() { return repo.findAll(); }
}
