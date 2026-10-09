package com.example.demo.repository;

import com.example.demo.model.Lista;
import com.example.demo.repository.entity.ListaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ListaRepositoryAdapter implements ListaRepository {

    private final ListaJpaRepository jpaRepository;

    public ListaRepositoryAdapter(ListaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Lista> buscarTodas() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Lista> buscarPorId(Integer id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Lista guardar(Lista lista) {
        ListaEntity entity = toEntity(lista);
        ListaEntity guardado = jpaRepository.save(entity);
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

    // --- Métodos privados para mapear entre Entidad y Dominio ---
    private Lista toDomain(ListaEntity entity) {
        return new Lista(
                entity.getId(),
                entity.getNombre()
        );
    }

    private ListaEntity toEntity(Lista lista) {
        ListaEntity entity = new ListaEntity();
        entity.setId(lista.getId());
        entity.setNombre(lista.getNombre());
        return entity;
    }
}
