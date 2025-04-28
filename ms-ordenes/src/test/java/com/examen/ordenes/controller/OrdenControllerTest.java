package com.examen.ordenes.controller;

import com.examen.ordenes.dto.OrdenRequest;
import com.examen.ordenes.dto.OrdenResponse;
import com.examen.ordenes.security.AuthClient;
import com.examen.ordenes.security.dto.ValidateTokenResponse;
import com.examen.ordenes.service.OrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrdenControllerTest {

    @Mock
    private OrdenService ordenService;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private OrdenController ordenController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearOrden() {
        String token = "Bearer fake-token";
        OrdenRequest request = new OrdenRequest();
        OrdenResponse expectedResponse = new OrdenResponse();

        ValidateTokenResponse validateTokenResponse = new ValidateTokenResponse();
        validateTokenResponse.setId(1L);

        when(authClient.validateToken(token)).thenReturn(validateTokenResponse);
        when(ordenService.crearOrden(1L, request, token)).thenReturn(expectedResponse);

        ResponseEntity<OrdenResponse> response = ordenController.crearOrden(token, request);

        assertEquals(expectedResponse, response.getBody());
        verify(authClient).validateToken(token);
        verify(ordenService).crearOrden(1L, request, token);
    }

    @Test
    void testListarOrdenes() {
        OrdenResponse ordenResponse = new OrdenResponse();
        when(ordenService.listarOrdenes()).thenReturn(Collections.singletonList(ordenResponse));

        ResponseEntity<?> response = ordenController.listarOrdenes();

        assertEquals(1, ((java.util.List<?>) response.getBody()).size());
        verify(ordenService).listarOrdenes();
    }
}
