package com.examen.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private Long id;
    private String token;
    private String nombre;
    private String email;
    private String rol;
}
