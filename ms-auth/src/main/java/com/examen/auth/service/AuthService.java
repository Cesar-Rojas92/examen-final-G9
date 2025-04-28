package com.examen.auth.service;

import com.examen.auth.dto.AuthResponse;
import com.examen.auth.dto.LoginRequest;
import com.examen.auth.dto.RegisterRequest;
import com.examen.auth.entity.Rol;
import com.examen.auth.entity.Usuario;
import com.examen.auth.repository.RolRepository;
import com.examen.auth.repository.UsuarioRepository;
import com.examen.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        Rol rol = rolRepository.findByNombre(request.getRol().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario user = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(rol)
                .build();

        usuarioRepository.save(user);

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .nombre(user.getNombre())
                .email(user.getEmail())
                .rol(user.getRol().getNombre())
                .build();

    }

    public AuthResponse login(LoginRequest request) {
        Usuario user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas, verifique su usuario y/o contraseña"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas, verifique su usuario y/o contraseña");
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .nombre(user.getNombre())
                .email(user.getEmail())
                .rol(user.getRol().getNombre())
                .build();

    }
}
