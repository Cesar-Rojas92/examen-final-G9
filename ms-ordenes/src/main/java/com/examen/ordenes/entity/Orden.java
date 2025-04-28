package com.examen.ordenes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ord_ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @ElementCollection
    @CollectionTable(name = "ord_ordenes_productos", joinColumns = @JoinColumn(name = "orden_id"))
    @Column(name = "producto_id")
    private List<Long> productosIds;

    private LocalDateTime fecha;
}
