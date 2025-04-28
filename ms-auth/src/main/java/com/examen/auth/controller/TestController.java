package com.examen.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/superadmin")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String accesoSuperadmin() {
        return "✅ Bienvenido SUPERADMIN";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accesoAdmin() {
        return "✅ Bienvenido ADMIN";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USUARIO')")
    public String accesoUser() {
        return "✅ Bienvenido USUARIO";
    }
}
