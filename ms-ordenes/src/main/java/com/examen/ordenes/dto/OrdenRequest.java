package com.examen.ordenes.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenRequest {
    private List<Long> productosIds;
}
