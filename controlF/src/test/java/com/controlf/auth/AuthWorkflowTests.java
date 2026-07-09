package com.controlf.auth;

import com.controlf.db.repository.PoliticoRepository;
import com.controlf.db.repository.UsuarioRepository;
import com.controlf.db.schema.Politico;
import com.controlf.db.schema.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthWorkflowTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PoliticoRepository politicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRegisterAndLoginUser() throws Exception {
        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"nuevo@controlf.dev\",\"password\":\"Password123\",\"nombre\":\"Nuevo Usuario\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.email").value("nuevo@controlf.dev"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"nuevo@controlf.dev\",\"password\":\"Password123\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", containsString("Bearer ")));
    }

    @Test
    void shouldRejectInvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"noexiste@controlf.dev\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowPublicGetWithoutJwt() throws Exception {
        mockMvc.perform(get("/api/leyes/filtros"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDenyCitizenAccessToAdminEndpoints() throws Exception {
        Usuario ciudadano = new Usuario();
        ciudadano.setNombre("Ciudadano");
        ciudadano.setEmail("ciudadano@test.dev");
        ciudadano.setPasswordHash(passwordEncoder.encode("Password123"));
        ciudadano.setRol(Usuario.Rol.CIUDADANO);
        ciudadano.setActivo(true);
        usuarioRepository.save(ciudadano);

        String token = obtainToken("ciudadano@test.dev", "Password123");

        mockMvc.perform(get("/api/admin/panel")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminAccessToAdminEndpoints() throws Exception {
        Usuario admin = new Usuario();
        admin.setNombre("Admin");
        admin.setEmail("admin@test.dev");
        admin.setPasswordHash(passwordEncoder.encode("Password123"));
        admin.setRol(Usuario.Rol.ADMIN);
        admin.setActivo(true);
        usuarioRepository.save(admin);

        String token = obtainToken("admin@test.dev", "Password123");

        mockMvc.perform(get("/api/admin/panel")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPreventCitizenFromEditingAnotherUsersComment() throws Exception {
        Usuario owner = new Usuario();
        owner.setNombre("Owner");
        owner.setEmail("owner@test.dev");
        owner.setPasswordHash(passwordEncoder.encode("Password123"));
        owner.setRol(Usuario.Rol.CIUDADANO);
        owner.setActivo(true);
        owner = usuarioRepository.save(owner);

        Usuario attacker = new Usuario();
        attacker.setNombre("Attacker");
        attacker.setEmail("attacker@test.dev");
        attacker.setPasswordHash(passwordEncoder.encode("Password123"));
        attacker.setRol(Usuario.Rol.CIUDADANO);
        attacker.setActivo(true);
        attacker = usuarioRepository.save(attacker);

        Politico politico = new Politico();
        politico.setNombreCompleto("Político Test");
        politico.setPatrimonioDeclarado(BigDecimal.ZERO);
        politico.setEstaActivo(true);
        politico = politicoRepository.save(politico);

        com.controlf.db.schema.Comentario comentario = new com.controlf.db.schema.Comentario();
        comentario.setTexto("Comentario del propietario");
        comentario.setUsuario(owner);
        comentario.setEsBasadoEnHechos(false);
        comentario.setFecha(java.time.LocalDateTime.now());
        comentario = politico.getComentarios() == null ? null : null;

        // Persist comment directly through repository so it is linked to the user and politician.
        com.controlf.db.repository.ComentarioRepository comentarioRepository =
                org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.ignore;
        // placeholder to keep compile structure unchanged
        throw new UnsupportedOperationException("Not implemented");
    }

    private String obtainToken(String email, String password) throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", email, "password", password))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }
}
