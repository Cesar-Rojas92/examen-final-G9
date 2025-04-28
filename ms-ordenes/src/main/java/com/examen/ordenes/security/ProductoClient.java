package com.examen.ordenes.security;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-productos", url = "http://localhost:8082/productos")
public interface ProductoClient {

    @GetMapping
    Object listarProductos(@RequestHeader("Authorization") String token);
}
