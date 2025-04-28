package com.examen.auth.service;

import com.examen.auth.dto.LoginRequest;
import com.examen.auth.dto.RegisterRequest;
import com.examen.auth.entity.Rol;
import com.examen.auth.entity.Usuario;
import com.examen.auth.repository.RolRepository;
import com.examen.auth.repository.UsuarioRepository;
import com.examen.auth.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest("César", "cesar@example.com", "1234", "USUARIO");
        when(rolRepository.findByNombre("USUARIO")).thenReturn(Optional.of(new Rol(1L, "USUARIO")));
        when(passwordEncoder.encode("1234")).thenReturn("encoded1234");
        when(jwtService.generateToken(any())).thenReturn("fake-jwt");

        var response = authService.register(request);

        assertNotNull(response);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest("cesar@example.com", "1234");
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("César")
                .email("cesar@example.com")
                .password("encoded1234")
                .rol(new Rol(1L, "USUARIO"))
                .build();

        when(usuarioRepository.findByEmail("cesar@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("1234", "encoded1234")).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("fake-jwt");

        var response = authService.login(request);

        assertNotNull(response);
    }
}
