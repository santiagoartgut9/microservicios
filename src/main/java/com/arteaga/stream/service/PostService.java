package com.arteaga.stream.service;

import com.arteaga.stream.dto.PostResponse;
import com.arteaga.stream.model.Post;
import com.arteaga.stream.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository repo;
    public PostService(PostRepository repo) { this.repo = repo; }

    public Post save(Post p) { return repo.save(p); }

    public List<PostResponse> feed() {
        return repo.findAllByOrderByCreatedAtDesc().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<PostResponse> byHilo(Long hiloId) {
        return repo.findByHiloIdOrderByCreatedAtDesc(hiloId).stream().map(this::toDto).collect(Collectors.toList());
    }

    private PostResponse toDto(Post p) {
        PostResponse r = new PostResponse();
        r.setId(p.getId());
        r.setContenido(p.getContenido());
        r.setAutorId(p.getAutor().getId());
        r.setAutorUsername(p.getAutor().getUsername());
        r.setHiloId(p.getHilo().getId());
        r.setCreatedAt(p.getCreatedAt());
        return r;
    }
}
