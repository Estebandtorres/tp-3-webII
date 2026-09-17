package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Favorito;

@Repository // Le avisa a Spring que esto es un componente de acceso a datos
public class FavoritoRepositoryImpl implements FavoritoRepository {

    private final Map<Long, Favorito> tablaFavoritos = new ConcurrentHashMap<>();
    
    private final AtomicLong generadorId = new AtomicLong(1);

    @Override
    public List<Favorito> buscarTodos() {
        return new ArrayList<>(tablaFavoritos.values());
    }

    @Override
    public Optional<Favorito> buscarPorId(Long id) {
        return Optional.ofNullable(tablaFavoritos.get(id));
    }

    @Override
    public Favorito guardar(Favorito favorito) {
        if (favorito.getId() == null) {
            
            favorito.setId(generadorId.getAndIncrement());
        }
        
        tablaFavoritos.put(favorito.getId(), favorito);
        return favorito;
    }

    @Override
    public void borrarPorId(Long id) {
        tablaFavoritos.remove(id);
    }

    @Override
    public boolean existeId(Long id) {
        return tablaFavoritos.containsKey(id);
    }
}