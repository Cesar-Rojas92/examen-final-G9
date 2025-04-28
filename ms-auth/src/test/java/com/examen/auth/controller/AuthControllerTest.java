package com.examen.auth.controller;

import com.examen.auth.dto.AuthResponse;
import com.examen.auth.dto.LoginRequest;
import com.examen.auth.dto.RegisterRequest;
import com.examen.auth.entity.Rol;
import com.examen.auth.entity.Usuario;
import com.examen.auth.repository.UsuarioRepository;
import com.examen.auth.security.JwtService;
import com.examen.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest("César", "cesar@example.com", "1234", "USUARIO");
        AuthResponse expectedResponse = AuthResponse.builder()
                .id(1L)
                .token("fake-jwt")
                .nombre("César")
                .email("cesar@example.com")
                .rol("USUARIO")
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<AuthResponse> response = authController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("fake-jwt", response.getBody().getToken());
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest("cesar@example.com", "1234");
        AuthResponse expectedResponse = AuthResponse.builder()
                .id(1L)
                .token("fake-login-jwt")
                .nombre("César")
                .email("cesar@example.com")
                .rol("USUARIO")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("fake-login-jwt", response.getBody().getToken());
    }

    @Test
    void testValidateSuccess() {
        String bearerToken = "Bearer validtoken";
        String token = "validtoken";

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("César")
                .email("cesar@example.com")
                .rol(new Rol(1L, "USUARIO"))
                .build();

        when(jwtService.validateToken(token)).thenReturn(true);
        when(jwtService.extractUsername(token)).thenReturn("cesar@example.com");
        when(usuarioRepository.findByEmail("cesar@example.com")).thenReturn(Optional.of(usuario));

        ResponseEntity<AuthResponse> response = authController.validate(bearerToken);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("César", response.getBody().getNombre());
    }

    @Test
    void testValidateInvalidToken() {
        String bearerToken = "Bearer invalidtoken";
        String token = "invalidtoken";

        when(jwtService.validateToken(token)).thenReturn(false);

        ResponseEntity<AuthResponse> response = authController.validate(bearerToken);

        assertEquals(400, response.getStatusCodeValue());
    }
}
