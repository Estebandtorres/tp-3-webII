package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Favorito;

public interface FavoritoRepository {
    List<Favorito> buscarTodos();
    Optional<Favorito> buscarPorId(Integer id);
    Favorito guardar(Favorito favorito);
    void borrarPorId(Integer id);
    boolean existeId(Integer id);
    List<Favorito> buscarPorListaId(Integer listaId);
}
