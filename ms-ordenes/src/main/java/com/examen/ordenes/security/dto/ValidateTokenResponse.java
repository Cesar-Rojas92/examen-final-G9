package com.examen.ordenes.security.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateTokenResponse {
    private Long id;
    private String token;
    private String nombre;
    private String email;
    private String rol;
}
