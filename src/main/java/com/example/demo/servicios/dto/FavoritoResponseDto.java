package com.example.demo.servicios.dto;

import java.time.LocalDateTime;

public record FavoritoResponseDto(
        Integer id,
        Long productoId,
        String notaPersonal,
        LocalDateTime fechaAgregado,
        Integer listaId
) {}