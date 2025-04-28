package com.examen.ordenes.service;

import com.examen.ordenes.dto.OrdenRequest;
import com.examen.ordenes.dto.OrdenResponse;
import com.examen.ordenes.entity.Orden;
import com.examen.ordenes.repository.OrdenRepository;
import com.examen.ordenes.security.AuthClient;
import com.examen.ordenes.security.ProductoClient;
import com.examen.ordenes.security.dto.ValidateTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final ProductoClient productoClient;
    ;
    private final AuthClient authClient;

    public OrdenResponse crearOrden(Long usuarioId, OrdenRequest request, String token) {
        ValidateTokenResponse userData = authClient.validateToken(token);
        if (userData == null || userData.getId() == null || !userData.getId().equals(usuarioId)) {
            throw new RuntimeException("Usuario inválido o no autorizado.");
        }

        validarProductosExistentes(request.getProductosIds(),token);

        Orden orden = Orden.builder()
                .usuarioId(usuarioId)
                .productosIds(request.getProductosIds())
                .fecha(LocalDateTime.now())
                .build();

        Orden guardada = ordenRepository.save(orden);

        return OrdenResponse.builder()
                .id(guardada.getId())
                .usuarioId(guardada.getUsuarioId())
                .productosIds(guardada.getProductosIds())
                .fecha(guardada.getFecha())
                .build();
    }

    public List<OrdenResponse> listarOrdenes() {
        return ordenRepository.findAll().stream()
                .map(orden -> OrdenResponse.builder()
                        .id(orden.getId())
                        .usuarioId(orden.getUsuarioId())
                        .productosIds(orden.getProductosIds())
                        .fecha(orden.getFecha())
                        .build())
                .collect(Collectors.toList());
    }

    private void validarProductosExistentes(List<Long> productosIds, String token) {
        List<?> productos = (List<?>) productoClient.listarProductos(token);


        List<Long> idsExistentes = productos.stream()
                .map(prod -> Long.valueOf(((Map<?, ?>) prod).get("id").toString()))
                .collect(Collectors.toList());


        boolean todosExisten = productosIds.stream().allMatch(idsExistentes::contains);

        if (!todosExisten) {
            throw new RuntimeException("Uno o más productos no existen.");
        }
    }
}
