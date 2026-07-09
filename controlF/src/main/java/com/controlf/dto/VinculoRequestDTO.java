package com.controlf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VinculoRequestDTO {
    @NotNull(message = "La promesa es obligatoria")
    private Integer promesaId;

    @NotNull(message = "La ley es obligatoria")
    private Integer leyId;

    @NotBlank(message = "El impacto esperado es obligatorio")
    private String impactoEsperado; // POSITIVO / NEGATIVO

    @NotBlank(message = "El nivel de coherencia es obligatorio")
    private String nivelCoherencia; // CUMPLE / INCUMPLE / AMBIGUO

    @NotBlank(message = "El análisis es obligatorio")
    private String analisis;
}
