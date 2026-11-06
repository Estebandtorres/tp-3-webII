package com.example.demo.config;

import com.example.demo.servicios.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component 
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
            
        // 1. Extraemos el header "Authorization"
        String authHeader = request.getHeader("Authorization");

        // 2. Verificamos que exista y empiece con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Sigue su camino sin autenticar
            return;
        }

        // 3. Extraemos el token quitando los primeros 7 caracteres ("Bearer ")
        String token = authHeader.substring(7);

        // 4. Si el token es válido y aún no hay nadie autenticado en el contexto actual
        if (jwtService.validarToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            Claims claims = jwtService.extraerClaims(token);
            String email = claims.getSubject();
            String rol = claims.get("rol", String.class);

            // Convertimos el rol en una Autoridad de Spring Security (le agregamos el prefijo ROLE_)
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));

            // Creamos el token interno de Spring Security que representa la identidad del usuario
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email, 
                    null, // No necesitamos la contraseña acá
                    authorities
            );

            // Guardamos la identidad en el contexto de seguridad para el resto del request
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 5. Continuamos con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
