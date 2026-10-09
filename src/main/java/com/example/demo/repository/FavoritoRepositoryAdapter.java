package com.example.demo.repository;

import java.util.stream.Collectors;
import com.example.demo.model.Favorito;
import com.example.demo.repository.entity.FavoritoEntity;
import com.example.demo.repository.entity.ListaEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class FavoritoRepositoryAdapter implements FavoritoRepository{
    
    private final FavoritoJpaRepository jpaRepository;
    
    public FavoritoRepositoryAdapter(FavoritoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Favorito> buscarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Favorito> buscarPorId(Integer id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Favorito guardar(Favorito favorito) {
        FavoritoEntity entity = toEntity(favorito);
        FavoritoEntity guardado = jpaRepository.save(entity);
        return toDomain(guardado);
    }

    @Override
    public void borrarPorId(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existeId(Integer id) {
        return jpaRepository.existsById(id);
    }


    private Favorito toDomain(FavoritoEntity entity) {
        Integer listaId = (entity.getLista() != null) ? entity.getLista().getId() : null;
        return new Favorito(
                entity.getId(),
                entity.getProductoId(),
                entity.getNotaPersonal(),
                entity.getFechaAgregado(),
                listaId
        );
    }

    private FavoritoEntity toEntity(Favorito favorito) {
        FavoritoEntity entity = new FavoritoEntity();
        entity.setId(favorito.getId());
        entity.setProductoId(favorito.getProductoId());
        entity.setNotaPersonal(favorito.getNotaPersonal());
        entity.setFechaAgregado(favorito.getFechaAgregado());
        if (favorito.getListaId() != null) {
            ListaEntity listaReference = new ListaEntity();
            listaReference.setId(favorito.getListaId());
            entity.setLista(listaReference);
        }
        return entity;
    }

    @Override
    public List<Favorito> buscarPorListaId(Integer listaId) {
        return jpaRepository.findByListaId(listaId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}
