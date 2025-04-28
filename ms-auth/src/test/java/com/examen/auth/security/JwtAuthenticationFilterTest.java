package com.examen.auth.security;

import com.examen.auth.entity.Rol;
import com.examen.auth.entity.Usuario;
import com.examen.auth.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldDoNothingWhenNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldDoNothingWhenInvalidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer fake.token.here");
        when(jwtService.validateToken("fake.token.here")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateWhenValidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token.here");
        when(jwtService.validateToken("valid.token.here")).thenReturn(true);
        when(jwtService.extractUsername("valid.token.here")).thenReturn("c.rojas113092@gmail.com");

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("César")
                .email("c.rojas113092@gmail.com")
                .password("password")
                .rol(new Rol(1L, "USUARIO"))
                .build();

        when(usuarioRepository.findByEmail("c.rojas113092@gmail.com")).thenReturn(Optional.of(usuario));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).validateToken("valid.token.here");
        verify(jwtService).extractUsername("valid.token.here");
        verify(usuarioRepository).findByEmail("c.rojas113092@gmail.com");
        verify(filterChain).doFilter(request, response);
    }
}
