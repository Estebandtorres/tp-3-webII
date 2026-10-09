package com.example.demo.servicios.dto;

import java.time.LocalDateTime;

import com.example.demo.model.Favorito;

public class FavoritoMapper {

    
    public static Favorito toEntity(FavoritoRequestDto requestDTO) {
        Favorito favorito = new Favorito();
        favorito.setProductoId(requestDTO.productoId());
        favorito.setNotaPersonal(requestDTO.notaPersonal());
        favorito.setListaId(requestDTO.listaId());
        favorito.setFechaAgregado(LocalDateTime.now()); 
        return favorito;
    }

    public static FavoritoResponseDto toResponseDTO(Favorito favorito) {
        return new FavoritoResponseDto(
                favorito.getId(),
                favorito.getProductoId(),
                favorito.getNotaPersonal(),
                favorito.getFechaAgregado(),
                favorito.getListaId()
        );
    }
}