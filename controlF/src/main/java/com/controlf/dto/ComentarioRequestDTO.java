package com.controlf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComentarioRequestDTO {
    @NotBlank(message = "El texto del comentario es obligatorio")
    private String texto;

    @NotNull(message = "El identificador de usuario es obligatorio")
    private Integer usuarioId;
}
