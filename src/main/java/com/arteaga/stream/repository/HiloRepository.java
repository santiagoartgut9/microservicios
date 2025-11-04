package com.arteaga.stream.repository;

import com.arteaga.stream.model.Hilo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HiloRepository extends JpaRepository<Hilo, Long> {
    Optional<Hilo> findByTitulo(String titulo);
}
