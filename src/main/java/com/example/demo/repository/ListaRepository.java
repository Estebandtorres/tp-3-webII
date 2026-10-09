package com.example.demo.repository;

import com.example.demo.model.Lista;
import java.util.List;
import java.util.Optional;

public interface ListaRepository {
    
    List<Lista> buscarTodas();
    
    Optional<Lista> buscarPorId(Integer id);
    
    Lista guardar(Lista lista);
    
    void borrarPorId(Integer id);
    
    boolean existeId(Integer id);
}
