package com.example.demo.servicios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FavoritoRequestDto(
        
        @NotNull(message = "El ID del producto es obligatorio")
        Long productoId,
        
        @NotBlank(message = "La nota personal no puede estar vacia ni tener solo espacios")
        @Size(max = 255, message = "La nota personal no puede exceder los 255 caracteres")
        String notaPersonal,

        @NotNull(message = "El ID de la lista es obligatorio")
        Integer listaId

        
) {
}

