package com.example.demo.servicios;

import com.example.demo.model.Lista;
import com.example.demo.model.Favorito;
import com.example.demo.repository.ListaRepository;
import com.example.demo.repository.FavoritoRepository;
import com.example.demo.servicios.dto.ListaRequestDto;
import com.example.demo.servicios.dto.ListaResponseDto;

import jakarta.transaction.Transactional;

import com.example.demo.servicios.dto.ListaMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListaService {

    private final ListaRepository listaRepository;
    private final FavoritoRepository favoritoRepository;

    public ListaService(ListaRepository listaRepository, FavoritoRepository favoritoRepository) {
        this.listaRepository = listaRepository;
        this.favoritoRepository = favoritoRepository;
    }

    public ListaResponseDto crearLista(ListaRequestDto request) {
        Lista nuevaLista = ListaMapper.toDomain(request);
        Lista guardada = listaRepository.guardar(nuevaLista);
        return ListaMapper.toResponseDto(guardada);
    }

    public List<ListaResponseDto> listarTodas() {
        return listaRepository.buscarTodas().stream()
                .map(ListaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<ListaResponseDto> obtenerLista(Integer id) {
        return listaRepository.buscarPorId(id)
                .map(ListaMapper::toResponseDto);
    }

    public void eliminarLista(Integer id) {

        listaRepository.borrarPorId(id);
    }

    
    public List<Favorito> obtenerFavoritosDeLista(Integer idLista) {
        if (!listaRepository.existeId(idLista)) {
            throw new RuntimeException("La lista no existe"); 
        }
        return favoritoRepository.buscarPorListaId(idLista); 
    }

    @Transactional 
    public void moverFavoritosYBorrarOrigen(Integer origenId, Integer destinoId) {

        if (!listaRepository.existeId(origenId)) {
            throw new IllegalArgumentException("La lista de origen no existe");
        }
        if (!listaRepository.existeId(destinoId)) {
            throw new IllegalArgumentException("La lista de destino no existe");
        }

        List<Favorito> favoritos = favoritoRepository.buscarPorListaId(origenId);

        for (Favorito fav : favoritos) {
            fav.setListaId(destinoId);
            favoritoRepository.guardar(fav); 
        }

        listaRepository.borrarPorId(origenId);
    }
}