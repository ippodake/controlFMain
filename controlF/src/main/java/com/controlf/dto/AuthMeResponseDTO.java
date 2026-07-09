package com.controlf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthMeResponseDTO {
    private Integer id;
    private String email;
    private String nombre;
    private String rol;
}
