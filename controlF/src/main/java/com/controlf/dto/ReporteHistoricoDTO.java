package com.controlf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteHistoricoDTO {
    private long totalLeyes;
    private long totalVotos;
    private long votosFavor;
    private long votosContra;
    private long votosAbstencion;
    private long leyesAprobadas;
    private long leyesEnDebate;
}
