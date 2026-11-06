package com.example.demo.servicios;

import java.nio.charset.StandardCharsets;
import java.util.Date;


import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {
    
    @Value ("${jwt.secret}")
    private String secret;

    @Value ("${jwt.expiration}")
    private Long expiration;

    public String generarToken(Usuario usuario) {
        
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
        Date fechaCreacion = new Date();
        Date fechaExpiracion = new Date(fechaCreacion.getTime() + expiration);

        return Jwts.builder()
                .subject(usuario.getEmail()) 
                .claim("id", usuario.getId()) 
                .claim("rol", usuario.getRol().name()) 
                .issuedAt(fechaCreacion)
                .expiration(fechaExpiracion)
                .signWith(key) 
                .compact();
    }

    public Claims extraerClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key) 
                .build()
                .parseSignedClaims(token)
                .getPayload(); 
    }

    public boolean validarToken(String token) {
        try {
            extraerClaims(token);
            return true; 
        } catch (Exception e) {
            return false;
        }
    }
}
