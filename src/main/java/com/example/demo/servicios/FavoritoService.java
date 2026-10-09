package com.example.demo.servicios;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.model.Favorito;
import com.example.demo.repository.FavoritoRepository;
import com.example.demo.servicios.dto.FavoritoMapper;
import com.example.demo.servicios.dto.FavoritoRequestDto;
import com.example.demo.servicios.dto.FavoritoResponseDto;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;

    public FavoritoService(FavoritoRepository favoritoRepository) {
        this.favoritoRepository = favoritoRepository;
    }

    public FavoritoResponseDto crear(FavoritoRequestDto requestDTO) {
        Favorito nuevoFavorito = FavoritoMapper.toEntity(requestDTO);
        Favorito guardado = favoritoRepository.guardar(nuevoFavorito);
        return FavoritoMapper.toResponseDTO(guardado);
    }

    public List<FavoritoResponseDto> obtenerTodos() {
        return favoritoRepository.buscarTodos().stream()
                .map(FavoritoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public FavoritoResponseDto obtenerPorId(Integer id) {
        Favorito favorito = favoritoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el favorito con ID: " + id));
        return FavoritoMapper.toResponseDTO(favorito);
    }

    public FavoritoResponseDto actualizar(Integer id, FavoritoRequestDto requestDTO) {
        Favorito existente = favoritoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el favorito con ID: " + id));
        
        existente.setProductoId(requestDTO.productoId());
        existente.setNotaPersonal(requestDTO.notaPersonal());
        
        Favorito actualizado = favoritoRepository.guardar(existente);
        return FavoritoMapper.toResponseDTO(actualizado);
    }

    public void eliminar(Integer id) {
        if (!favoritoRepository.existeId(id)) {
            throw new RecursoNoEncontradoException("No se encontro el favorito con ID: " + id);
        }
        favoritoRepository.borrarPorId(id);
    }
}
