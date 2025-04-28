package com.examen.ordenes.security;

import com.examen.ordenes.security.JwtValidationFilter;
import com.examen.ordenes.security.dto.ValidateTokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class JwtValidationFilterTest {

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private JwtValidationFilter jwtValidationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAuthenticateWhenValidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        ValidateTokenResponse validateTokenResponse = new ValidateTokenResponse();
        validateTokenResponse.setId(1L);
        validateTokenResponse.setEmail("user@example.com");
        validateTokenResponse.setRol("ROLE_USUARIO");

        when(authClient.validateToken("Bearer valid-token")).thenReturn(validateTokenResponse);

        jwtValidationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(authClient).validateToken("Bearer valid-token");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenNoToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        jwtValidationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext());
        verify(filterChain).doFilter(request, response);
    }
}
