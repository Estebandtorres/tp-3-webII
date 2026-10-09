package com.example.demo.repository;

import com.example.demo.repository.entity.ListaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaJpaRepository extends JpaRepository<ListaEntity, Integer> {
}
