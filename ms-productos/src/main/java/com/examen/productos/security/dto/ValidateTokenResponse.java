package com.examen.productos.security.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateTokenResponse {
    private String nombre;
    private String email;
    private String rol;
    private String token;
}
