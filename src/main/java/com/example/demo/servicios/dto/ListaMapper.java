package com.example.demo.servicios.dto;

import com.example.demo.model.Lista;

public class ListaMapper {
    
    public static Lista toDomain(ListaRequestDto request) {
        Lista lista = new Lista();
        lista.setNombre(request.nombre());
        return lista;
    }

    public static ListaResponseDto toResponseDto(Lista lista) {
        return new ListaResponseDto(lista.getId(), lista.getNombre());
    }
}
