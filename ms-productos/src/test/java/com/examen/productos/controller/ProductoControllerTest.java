package com.examen.productos.controller;

import com.examen.productos.dto.ProductoRequest;
import com.examen.productos.dto.ProductoResponse;
import com.examen.productos.security.AuthClient;
import com.examen.productos.security.dto.ValidateTokenResponse;
import com.examen.productos.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearProductoTest() throws Exception {

        String token = "Bearer fake-token";

        ProductoRequest request = new ProductoRequest();
        ProductoResponse expectedResponse = new ProductoResponse();

        ValidateTokenResponse validateTokenResponse = new ValidateTokenResponse();

        when(authClient.validateToken(token)).thenReturn(validateTokenResponse);

        when(productoService.crearProducto(request)).thenReturn(expectedResponse);

        ResponseEntity<ProductoResponse> response = productoController.crear(request);

        assertEquals(expectedResponse, response.getBody());

        verify(productoService).crearProducto(request);
    }


    @Test
    void listarProductosTest() throws Exception {
        ProductoResponse productoResponse = new ProductoResponse();
        when(productoService.listarProductos()).thenReturn(Collections.singletonList(productoResponse));

        ResponseEntity<?> response = productoController.listar();

        assertEquals(1, ((java.util.List<?>) response.getBody()).size());
        verify(productoService).listarProductos();
    }
}
