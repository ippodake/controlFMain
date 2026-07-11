package com.controlf.service;

import com.controlf.db.repository.*;
import com.controlf.db.schema.Calificacion;
import com.controlf.db.schema.Comentario;
import com.controlf.db.schema.Politico;
import com.controlf.db.schema.Promesa;
import com.controlf.db.schema.Usuario;
import com.controlf.dto.ActualizarCampoPoliticoRequestDTO;
import com.controlf.dto.CalificacionRequestDTO;
import com.controlf.dto.CartaPoliticoDTO;
import com.controlf.dto.ComentarioRequestDTO;
import com.controlf.dto.PromesaDTO;
import com.controlf.dto.PromesaRequestDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PoliticoService {

    private final PoliticoRepository politicoRepository;
    private final VinculoPromesaLeyRepository vinculoRepository;
    private final ComentarioRepository comentarioRepository;
    private final CalificacionRepository calificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PromesaRepository promesaRepository;
    private final ConfiguracionRepository configuracionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public com.controlf.dto.PerfilPoliticoDTO getPoliticoProfile(Integer id) {
        Politico p = politicoRepository.findById(id).orElseThrow();
        Double coherencia = vinculoRepository.findAverageCoherenciaByPoliticoId(p.getId());

        List<com.controlf.dto.HistorialCoherenciaDTO> historial = p.getPromesas().stream()
                .flatMap(promesa -> promesa.getVinculos().stream())
                .map(v -> com.controlf.dto.HistorialCoherenciaDTO.builder()
                        .leyTitulo(v.getLey().getTitulo())
                        .votoReal(findVotoForPolitico(v.getLey(), p))
                        .resultado(v.getNivelCoherencia().name())
                        .analisis(v.getAnalisisCoherencia())
                        .build())
                .collect(Collectors.toList());

        return com.controlf.dto.PerfilPoliticoDTO.builder()
                .id(p.getId().toString())
                .nombre(p.getNombreCompleto())
                .organizacion(p.getPartidoPolitico())
                .cargo(p.getCargoActual())
                .patrimonio(p.getPatrimonioDeclarado() != null ? "$" + p.getPatrimonioDeclarado().toString() : "No declarado")
                .fotoUrl(p.getFotoUrl())
                .antecedentes(p.getAntecedentes())
                .estaActivo(p.getEstaActivo())
                .porcentajeCoherencia(coherencia != null ? coherencia : 0.0)
                .estadoEtiqueta(determineEstadoEtiqueta(coherencia))
                .historial(historial)
                .comentarios(p.getComentarios().stream().map(c -> com.controlf.dto.ComentarioDebateDTO.builder()
                        .id(c.getId().toString())
                        .usuario(c.getUsuario().getNombre())
                        .mensaje(c.getTexto())
                        .fecha(c.getFecha().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .avatarUrl(c.getUsuario().getAvatarUrl())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    private String findVotoForPolitico(com.controlf.db.schema.Ley ley, Politico p) {
        return ley.getVotos().stream()
                .filter(v -> v.getPolitico().getId().equals(p.getId()))
                .map(v -> v.getTipoVoto().name())
                .findFirst()
                .orElse("N/A");
    }

    public com.controlf.dto.FiltrosPoliticoDTO getFiltros() {
        try {
            return com.controlf.dto.FiltrosPoliticoDTO.builder()
                    .partidos(politicoRepository.findDistinctPartidos())
                    .regiones(politicoRepository.findDistinctRegiones())
                    .comisiones(politicoRepository.findDistinctComisiones())
                    .build();
        } catch (Exception e) {
            return com.controlf.dto.FiltrosPoliticoDTO.builder()
                    .partidos(java.util.List.of())
                    .regiones(java.util.List.of())
                    .comisiones(java.util.List.of())
                    .build();
        }
    }

    public List<com.controlf.dto.SimpleItemDTO> getPoliticosImportables() {
        return politicoRepository.findAll().stream()
                .map(PoliticoService::mapToSimpleItemDTO)
                .collect(Collectors.toList());
    }

public com.controlf.dto.GrillaPoliticosDTO getPoliticosFiltrados(int pagina, int size, String nombre, String partido, String region, String comision) {
    try {
        org.springframework.data.jpa.domain.Specification<Politico> spec = (root, query, cb) -> cb.conjunction();

        if (nombre != null && !nombre.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nombreCompleto")), "%" + nombre.toLowerCase() + "%"));
        }

        if (partido != null && !partido.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("partidoPolitico"), partido));
        }

        if (region != null && !region.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("region"), region));
        }

        if (comision != null && !comision.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("comision"), comision));
        }

        org.springframework.data.domain.Page<Politico> page = politicoRepository.findAll(
                spec,
                org.springframework.data.domain.PageRequest.of(
                        Math.max(0, pagina - 1),
                        size,
                        org.springframework.data.domain.Sort.by(
                                org.springframework.data.domain.Sort.Direction.DESC,
                                "id"
                        )
                )
        );

        List<CartaPoliticoDTO> cartas = page.getContent().stream()
                .map(PoliticoService::mapToCartaDTO)
                .collect(Collectors.toList());

        return com.controlf.dto.GrillaPoliticosDTO.builder()
                .id("grilla-politicos")
                .cartas(cartas)
                .paginaActual(pagina)
                .totalPaginas(Math.max(1, page.getTotalPages()))
                .build();

    } catch (Exception e) {
        e.printStackTrace();
        return com.controlf.dto.GrillaPoliticosDTO.builder()
                .id("grilla-politicos")
                .cartas(java.util.List.of())
                .paginaActual(1)
                .totalPaginas(1)
                .build();
    }
}
  public List<CartaPoliticoDTO> getAllPoliticosAsCartas() {
    return politicoRepository.findAll(
            org.springframework.data.domain.Sort.by(
                    org.springframework.data.domain.Sort.Direction.DESC,
                    "id"
            )
    ).stream()
     .map(PoliticoService::mapToCartaDTO)
     .collect(Collectors.toList());
}

    @Transactional
    public void actualizarCampoPolitico(Integer politicoId, ActualizarCampoPoliticoRequestDTO request) {
        Politico p = politicoRepository.findById(politicoId).orElseThrow();

        String campo = request.getCampo() == null ? "" : request.getCampo().trim().toLowerCase();
        String valor = request.getValor();

        String valorAnterior = null;
        switch (campo) {
            case "patrimonio" -> {
                valorAnterior = p.getPatrimonioDeclarado() == null ? null : p.getPatrimonioDeclarado().toPlainString();
                p.setPatrimonioDeclarado(new BigDecimal(valor));
            }
            case "antecedentes" -> {
                valorAnterior = p.getAntecedentes();
                p.setAntecedentes(valor);
            }
            default -> throw new IllegalArgumentException("Campo no soportado: " + campo);
        }

        p.setHistorialActualizaciones(appendHistorialEntry(p.getHistorialActualizaciones(), campo, valorAnterior, valor));
        politicoRepository.save(p);
    }

    public void addComentario(Integer politicoId, ComentarioRequestDTO request, Integer currentUserId) {
        Politico p = politicoRepository.findById(politicoId).orElseThrow();
        Usuario u = usuarioRepository.findById(currentUserId).orElseThrow();

        Comentario c = new Comentario();
        c.setTexto(request.getTexto());
        c.setUsuario(u);
        c.setFecha(LocalDateTime.now());
        c.setEsBasadoEnHechos(false);
        
        comentarioRepository.save(c);
        p.getComentarios().add(c);
        politicoRepository.save(p);
    }

    public void addCalificacion(Integer politicoId, CalificacionRequestDTO request, Integer currentUserId) {
        Politico p = politicoRepository.findById(politicoId).orElseThrow();
        Usuario u = usuarioRepository.findById(currentUserId).orElseThrow();

        Calificacion cal = new Calificacion();
        cal.setPuntaje(request.getPuntaje());
        cal.setUsuario(u);
        cal.setFecha(LocalDateTime.now());

        calificacionRepository.save(cal);
        p.getCalificaciones().add(cal);
        politicoRepository.save(p);
    }

    @Transactional
    public PromesaDTO crearPromesa(Integer politicoId, PromesaRequestDTO request) {
        Politico politico = politicoRepository.findById(politicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Político no encontrado"));

        Promesa promesa = new Promesa();
        promesa.setDescripcion(request.getDescripcion());
        promesa.setCategoria(request.getCategoria());
        promesa.setFechaCreacion(LocalDate.now());
        promesa.setPolitico(politico);

        Promesa saved = promesaRepository.save(promesa);
        if (politico.getPromesas() == null) {
            politico.setPromesas(new ArrayList<>());
        }
        politico.getPromesas().add(saved);
        politicoRepository.save(politico);

        return mapToPromesaDTO(saved);
    }

    public List<PromesaDTO> listarPromesasPorPolitico(Integer politicoId) {
        if (!politicoRepository.existsById(politicoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Político no encontrado");
        }
        return promesaRepository.findByPoliticoId(politicoId).stream()
                .map(this::mapToPromesaDTO)
                .collect(Collectors.toList());
    }

    private PromesaDTO mapToPromesaDTO(Promesa promesa) {
        return PromesaDTO.builder()
                .id(promesa.getId())
                .descripcion(promesa.getDescripcion())
                .categoria(promesa.getCategoria())
                .fechaCreacion(promesa.getFechaCreacion())
                .politicoId(promesa.getPolitico() != null ? promesa.getPolitico().getId() : null)
                .build();
    }

    private static CartaPoliticoDTO mapToCartaDTO(Politico p) {
        Double coherencia = null;
        long proyectos = 0L;
        return CartaPoliticoDTO.builder()
                .id(p.getId().toString())
                .nombre(p.getNombreCompleto())
                .organizacion(p.getPartidoPolitico())
                .fotoUrl(p.getFotoUrl())
                .estaActivo(p.getEstaActivo())
                .porcentajeCoherencia(coherencia != null ? coherencia : 0.0)
                .cantidadProyectos(proyectos)
                .estadoEtiqueta("SIN DATOS")
                .build();
    }

    private static com.controlf.dto.SimpleItemDTO mapToSimpleItemDTO(Politico p) {
        return com.controlf.dto.SimpleItemDTO.builder()
                .id(p.getId().toString())
                .label(p.getNombreCompleto())
                .build();
    }

    private String appendHistorialEntry(String currentJson, String campo, String valorAnterior, String valorNuevo) {
        List<Map<String, Object>> entries = new ArrayList<>();
        if (currentJson != null && !currentJson.isBlank()) {
            try {
                entries = objectMapper.readValue(currentJson, new TypeReference<>() {});
            } catch (Exception ignored) {
                entries = new ArrayList<>();
            }
        }

        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("fecha", LocalDateTime.now().toString());
        entry.put("campo", campo);
        entry.put("valorAnterior", valorAnterior);
        entry.put("valorNuevo", valorNuevo);
        entries.add(entry);

        try {
            return objectMapper.writeValueAsString(entries);
        } catch (Exception e) {
            return currentJson;
        }
    }

    private String determineEstadoEtiqueta(Double coherencia) {
        if (coherencia == null) return "SIN DATOS";
        
        double alta = getThreshold("UMBRAL_COHERENCIA_ALTA", 70.0);
        double media = getThreshold("UMBRAL_COHERENCIA_MEDIA", 40.0);

        if (coherencia >= alta) return "COHERENTE";
        if (coherencia >= media) return "AMBIGUO";
        return "INCOHERENTE";
    }

    private double getThreshold(String clave, double defaultValue) {
        return configuracionRepository.findById(clave)
                .map(c -> Double.parseDouble(c.getValor()))
                .orElse(defaultValue);
    }
}
