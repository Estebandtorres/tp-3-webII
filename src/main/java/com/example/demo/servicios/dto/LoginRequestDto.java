package com.example.demo.servicios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
    @NotBlank (message = "El email no puede estar vacío")
    @Email (message = "Deber ser un email válido")
    String email,

    @NotBlank (message = "La contraseña no puede estar vacía")
    String password
) {}
