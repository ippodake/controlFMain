package com.controlf.controller;

import com.controlf.dto.*;
import com.controlf.service.LeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/leyes")
@RequiredArgsConstructor
public class LeyController {

    private final LeyService leyService;

    @GetMapping("/{id}/perfil")
    public PerfilLeyDTO getPerfil(@PathVariable Integer id) {
        return leyService.getFullPerfilLey(id);
    }

    @GetMapping("/filtros")
    public FiltrosLeyDTO getFiltros() {
        return leyService.getFiltros();
    }

    @GetMapping
    public GrillaLeyesDTO getLeyes(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String termino,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String estado) {
        return leyService.getLeyesFiltradas(pagina, size, termino, categoria, estado);
    }

    @PatchMapping("/{id}/categoria")
    public void actualizarCategoria(@PathVariable Integer id, @Valid @RequestBody CategoriaLeyRequestDTO request) {
        leyService.actualizarCategoriaLey(id, request);
    }

    @PatchMapping("/{id}/estado")
    public void actualizarEstado(@PathVariable Integer id, @Valid @RequestBody EstadoLeyRequestDTO request) {
        leyService.actualizarEstadoLey(id, request);
    }

    @PatchMapping("/{id}/votos/{votoId}/asistencia")
    public void actualizarAsistencia(@PathVariable Integer id, @PathVariable Integer votoId, @Valid @RequestBody AsistenciaVotoRequestDTO request) {
        leyService.actualizarAsistenciaVoto(id, votoId, request);
    }

    @PostMapping("/{id}/comentarios")
    public void postComentario(@PathVariable Integer id, @Valid @RequestBody ComentarioRequestDTO request) {
        leyService.addComentario(id, request);
    }

    @PostMapping("/{id}/calificaciones")
    public void postCalificacion(@PathVariable Integer id, @Valid @RequestBody CalificacionRequestDTO request) {
        leyService.addCalificacion(id, request);
    }
}
