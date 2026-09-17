package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Favorito;

public interface FavoritoRepository {
    List<Favorito> buscarTodos();
    Optional<Favorito> buscarPorId(Long id);
    Favorito guardar(Favorito favorito);
    void borrarPorId(Long id);
    boolean existeId(Long id);
}
