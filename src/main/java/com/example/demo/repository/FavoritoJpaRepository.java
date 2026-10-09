package com.example.demo.repository;

import com.example.demo.repository.entity.FavoritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoritoJpaRepository extends JpaRepository<FavoritoEntity, Integer> {

    List<FavoritoEntity> findByListaId(Integer listaId);
}
