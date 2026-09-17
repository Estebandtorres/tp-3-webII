package com.example.demo.servicios.dto;

import java.time.LocalDateTime;

public record FavoritoResponseDto(
        Long id,
        Long productoId,
        String notaPersonal,
        LocalDateTime fechaAgregado
) {}