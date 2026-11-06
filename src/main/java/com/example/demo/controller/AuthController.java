package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.servicios.dto.RegistroRequestDto;
import com.example.demo.servicios.AuthService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.demo.servicios.dto.LoginRequestDto;
import com.example.demo.servicios.dto.LoginResponseDto;

@RestController 
@RequestMapping ("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@Valid @RequestBody RegistroRequestDto request) {
        try {
            authService.registrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
        } catch (IllegalArgumentException e) {
            
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        try {
            String token = authService.login(request);
            return ResponseEntity.ok(new LoginResponseDto(token));
        } catch (IllegalArgumentException e) {
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    
}
