package com.arteaga.stream.service;

import com.arteaga.stream.model.Hilo;
import com.arteaga.stream.repository.HiloRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HiloService {
    private final HiloRepository repo;
    
    public HiloService(HiloRepository repo) { 
        this.repo = repo; 
    }

    public Hilo create(Hilo h) { 
        return repo.save(h); 
    }
    
    public List<Hilo> list() { 
        return repo.findAll(); 
    }
    
    public Optional<Hilo> findById(Long id) {
        return repo.findById(id);
    }

    // get or create a Global stream/hilo
    public Hilo getOrCreateGlobal() {
        return repo.findByTitulo("Global")
                .orElseGet(() -> {
                    Hilo global = new Hilo();
                    global.setTitulo("Global");
                    global.setCreador(null); // Hilo global no tiene creador específico
                    return repo.save(global);
                });
    }
}