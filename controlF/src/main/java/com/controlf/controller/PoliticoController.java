package com.controlf.controller;

import com.controlf.dto.CalificacionRequestDTO;
import com.controlf.dto.CartaPoliticoDTO;
import com.controlf.dto.ComentarioRequestDTO;
import com.controlf.dto.SimpleItemDTO;
import com.controlf.service.PoliticoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/politicos")
@RequiredArgsConstructor
public class PoliticoController {

    private final PoliticoService politicoService;

    @GetMapping("/{id}")
    public com.controlf.dto.PerfilPoliticoDTO getPolitico(@PathVariable Integer id) {
        return politicoService.getPoliticoProfile(id);
    }

    @GetMapping("/filtros")
    public com.controlf.dto.FiltrosPoliticoDTO getFiltros() {
        return politicoService.getFiltros();
    }

    @GetMapping("/importables")
    public List<SimpleItemDTO> getImportables() {
        return politicoService.getPoliticosImportables();
    }

    @GetMapping
    public com.controlf.dto.GrillaPoliticosDTO getPoliticos(
            @RequestParam(defaultValue = "1") int pagina,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String partido,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String comision) {
        return politicoService.getPoliticosFiltrados(pagina, size, nombre, partido, region, comision);
    }

    @PostMapping("/{id}/comentarios")
    public void postComentario(@PathVariable Integer id, @Valid @RequestBody ComentarioRequestDTO request) {
        politicoService.addComentario(id, request);
    }

    @PostMapping("/{id}/calificaciones")
    public void postCalificacion(@PathVariable Integer id, @Valid @RequestBody CalificacionRequestDTO request) {
        politicoService.addCalificacion(id, request);
    }
}
