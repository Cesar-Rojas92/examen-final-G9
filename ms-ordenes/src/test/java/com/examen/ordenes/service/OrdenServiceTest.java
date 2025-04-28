package com.examen.ordenes.service;

import com.examen.ordenes.dto.OrdenRequest;
import com.examen.ordenes.entity.Orden;
import com.examen.ordenes.repository.OrdenRepository;
import com.examen.ordenes.security.AuthClient;
import com.examen.ordenes.security.ProductoClient;
import com.examen.ordenes.security.dto.ValidateTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private AuthClient authClient;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private OrdenService ordenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearOrden() {
        // Arrange
        Long usuarioId = 1L;
        String token = "Bearer valid-token";

        OrdenRequest request = new OrdenRequest(List.of(1L, 2L));

        // Mock del AuthClient (validateToken)
        ValidateTokenResponse validateResponse = ValidateTokenResponse.builder()
                .id(usuarioId)
                .nombre("César")
                .email("cesar@example.com")
                .rol("USUARIO")
                .token(token)
                .build();

        when(authClient.validateToken(token)).thenReturn(validateResponse);

        // Mock del ProductoClient (listarProductos)
        when(productoClient.listarProductos(token)).thenReturn(List.of(
                Map.of("id", 1L),
                Map.of("id", 2L)
        ));

        // Mock del OrdenRepository (save)
        Orden orden = Orden.builder()
                .id(1L)
                .usuarioId(usuarioId)
                .productosIds(request.getProductosIds())
                .fecha(LocalDateTime.now())
                .build();

        when(ordenRepository.save(any())).thenReturn(orden);

        // Act
        var response = ordenService.crearOrden(usuarioId, request, token);

        // Assert
        assertEquals(usuarioId, response.getUsuarioId());
        assertEquals(2, response.getProductosIds().size());
        verify(authClient).validateToken(token);
        verify(productoClient).listarProductos(token);
        verify(ordenRepository).save(any(Orden.class));
    }

    @Test
    void testListarOrdenes() {
        // Arrange
        when(ordenRepository.findAll()).thenReturn(List.of(
                new Orden(1L, 100L, List.of(1L, 2L), LocalDateTime.now())
        ));

        // Act
        var ordenes = ordenService.listarOrdenes();

        // Assert
        assertEquals(1, ordenes.size());
    }
}
