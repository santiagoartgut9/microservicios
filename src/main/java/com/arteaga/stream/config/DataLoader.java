package com.arteaga.stream.config;

import com.arteaga.stream.model.Hilo;
import com.arteaga.stream.model.Post;
import com.arteaga.stream.model.Usuario;
import com.arteaga.stream.repository.PostRepository;
import com.arteaga.stream.service.HiloService;
import com.arteaga.stream.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(UsuarioService usuarioService,
                               HiloService hiloService,
                               PostRepository postRepository) {
        return args -> {
            try {
                // Registrar usuarios con password (en dev: 'password')
                Usuario u1 = usuarioService.registerUser("santiago", "Santiago A.", "santiago@example.com", "password");
                Usuario u2 = usuarioService.registerUser("ana", "Ana P.", "ana@example.com", "password");

                // Obtener o crear hilo Global
                Hilo global = hiloService.getOrCreateGlobal();

                // Crear algunos posts iniciales
                postRepository.save(new Post("¡Hola mundo desde el monolito Spring! Este es mi primer post.", u1, global));
                postRepository.save(new Post("Probando la aplicación de streams. ¡Funciona muy bien!", u2, global));
                postRepository.save(new Post("Los posts están limitados a 140 caracteres, similar a Twitter.", u1, global));
                
                System.out.println("Datos de prueba cargados correctamente");
            } catch (Exception e) {
                System.err.println("Error cargando datos de prueba: " + e.getMessage());
            }
        };
    }
}