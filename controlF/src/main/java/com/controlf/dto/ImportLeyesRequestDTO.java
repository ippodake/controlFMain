package com.controlf.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ImportLeyesRequestDTO {
    @NotEmpty(message = "Debe seleccionar al menos un político")
    private List<Integer> politicoIds;
}
