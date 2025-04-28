package com.examen.auth.controller;

import com.examen.auth.dto.AuthResponse;
import com.examen.auth.dto.LoginRequest;
import com.examen.auth.dto.RegisterRequest;
import com.examen.auth.entity.Usuario;
import com.examen.auth.repository.UsuarioRepository;
import com.examen.auth.security.JwtService;
import com.examen.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validate(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.badRequest().build();
        }

        String email = jwtService.extractUsername(token);
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .nombre(user.getNombre())
                .email(user.getEmail())
                .rol(user.getRol().getNombre())
                .build());
    }
}
