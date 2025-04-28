package com.examen.auth.security;

import com.examen.auth.entity.Rol;
import com.examen.auth.entity.Usuario;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(jwtService, "secret", "12345678901234567890123456789012");
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);

        usuario = Usuario.builder()
                .id(1L)
                .nombre("César")
                .email("cesar@example.com")
                .password("1234")
                .rol(Rol.builder().id(1L).nombre("USUARIO").build())
                .build();
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(usuario);
        assertNotNull(token, "El token generado no debe ser nulo");
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtService.generateToken(usuario);
        boolean isValid = jwtService.validateToken(token);
        assertTrue(isValid, "El token generado debe ser válido");
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalid.token.here";
        boolean isValid = jwtService.validateToken(invalidToken);
        assertFalse(isValid, "Un token inválido debe ser rechazado");
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(usuario);
        String username = jwtService.extractUsername(token);
        assertEquals(usuario.getEmail(), username, "El email extraído debe coincidir con el del usuario");
    }

    @Test
    void testValidateToken_ExpiredToken() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1000L);
        String expiredToken = jwtService.generateToken(usuario);

        boolean isValid = jwtService.validateToken(expiredToken);
        assertFalse(isValid, "Un token expirado debe ser inválido");
    }

    @Test
    void testExtractUsername_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(JwtException.class, () -> {
            jwtService.extractUsername(invalidToken);
        }, "Debe lanzar excepción al intentar extraer de un token inválido");
    }

}
