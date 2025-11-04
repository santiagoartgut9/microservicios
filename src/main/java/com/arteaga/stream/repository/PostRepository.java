package com.arteaga.stream.repository;

import com.arteaga.stream.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByHiloIdOrderByCreatedAtDesc(Long hiloId);
}
