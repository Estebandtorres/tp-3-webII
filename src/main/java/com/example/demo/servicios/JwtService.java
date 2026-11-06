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
        // Convierte la clave secreta de tu properties en una llave criptográfica
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
        Date fechaCreacion = new Date();
        Date fechaExpiracion = new Date(fechaCreacion.getTime() + expiration);

        return Jwts.builder()
                .subject(usuario.getEmail()) // Identificador principal
                .claim("id", usuario.getId()) // Guardamos el ID para usarlo luego en la autorización
                .claim("rol", usuario.getRol().name()) // Guardamos el rol (USER/ADMIN)
                .issuedAt(fechaCreacion)
                .expiration(fechaExpiracion)
                .signWith(key) // Firmamos el token
                .compact();
    }

    public Claims extraerClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key) // Verifica la firma con nuestra clave
                .build()
                .parseSignedClaims(token)
                .getPayload(); // Devuelve el contenido (subject, claims, etc.)
    }

    public boolean validarToken(String token) {
        try {
            extraerClaims(token);
            return true; // Si no lanza excepción, la firma es válida y no expiró
        } catch (Exception e) {
            return false;
        }
    }
}
