package com.examen.ordenes.controller;

import com.examen.ordenes.dto.OrdenRequest;
import com.examen.ordenes.dto.OrdenResponse;
import com.examen.ordenes.security.AuthClient;
import com.examen.ordenes.security.dto.ValidateTokenResponse;
import com.examen.ordenes.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;
    private final AuthClient authClient;

    @PostMapping
    @PreAuthorize("hasAnyRole('USUARIO')")
    public ResponseEntity<OrdenResponse> crearOrden(
            @RequestHeader("Authorization") String token,
            @RequestBody OrdenRequest request) {

        ValidateTokenResponse userData = authClient.validateToken(token);
        Long usuarioId = userData.getId();

        return ResponseEntity.ok(ordenService.crearOrden(usuarioId, request, token));
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<OrdenResponse>> listarOrdenes() {
        return ResponseEntity.ok(ordenService.listarOrdenes());
    }
}
