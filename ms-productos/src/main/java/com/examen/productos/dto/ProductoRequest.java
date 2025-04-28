package com.examen.productos.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequest {
    private String nombre;
    private Double precio;
    private String categoria;
}
