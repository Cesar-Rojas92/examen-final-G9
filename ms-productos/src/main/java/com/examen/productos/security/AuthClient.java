package com.examen.productos.security;

import com.examen.productos.security.dto.ValidateTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-auth", url = "http://localhost:8081/auth")
public interface AuthClient {

    @GetMapping("/validate")
    ValidateTokenResponse validateToken(@RequestHeader("Authorization") String token);
}
