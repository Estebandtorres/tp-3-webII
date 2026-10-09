package com.example.demo.servicios.dto;

import jakarta.validation.constraints.NotNull;

public record MoverFavoritosRequestDto(
    @NotNull(message = "El ID de la lista destino es obligatorio")
    Integer destinoId
) {}
