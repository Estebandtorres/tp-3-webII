package com.example.demo.servicios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ListaRequestDto(
    @NotBlank(message = "El nombre de la lista no puede estar vacio")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    String nombre
) {}
