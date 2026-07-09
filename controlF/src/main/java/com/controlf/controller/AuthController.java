package com.controlf.controller;

import com.controlf.auth.JwtService;
import com.controlf.db.repository.UsuarioRepository;
import com.controlf.db.schema.Usuario;
import com.controlf.dto.AuthLoginRequestDTO;
import com.controlf.dto.AuthRegisterRequestDTO;
import com.controlf.dto.AuthResponseDTO;
import com.controlf.dto.AuthMeResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody AuthRegisterRequestDTO request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Usuario.Rol.CIUDADANO);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario.getEmail(), usuario.getRol().name());
        return ResponseEntity.ok(new AuthResponseDTO(token, Map.of("id", usuario.getId(), "email", usuario.getEmail(), "nombre", usuario.getNombre(), "rol", usuario.getRol().name())));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthLoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken(request.getEmail(), authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority().replace("ROLE_", ""));
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .body(new AuthResponseDTO(token, Map.of("email", request.getEmail(), "rol", authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority().replace("ROLE_", ""))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AuthMeResponseDTO> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(new AuthMeResponseDTO(usuario.getId(), usuario.getEmail(), usuario.getNombre(), usuario.getRol().name()));
    }
}
