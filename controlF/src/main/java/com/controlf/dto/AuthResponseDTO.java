package com.controlf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private Map<String, Object> user;
}
