package com.examen.ordenes.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenResponse {
    private Long id;
    private Long usuarioId;
    private List<Long> productosIds;
    private LocalDateTime fecha;
}
