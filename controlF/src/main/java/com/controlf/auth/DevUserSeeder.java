package com.controlf.auth;

import com.controlf.db.repository.UsuarioRepository;
import com.controlf.db.schema.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevUserSeeder implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepository.findByEmail("admin@controlf.dev").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin Dev");
            admin.setEmail("admin@controlf.dev");
            admin.setPasswordHash(passwordEncoder.encode("DevAdmin2026!"));
            admin.setRol(Usuario.Rol.ADMIN);
            admin.setFechaRegistro(LocalDateTime.now());
            admin.setActivo(true);
            usuarioRepository.save(admin);
        }
    }
}
