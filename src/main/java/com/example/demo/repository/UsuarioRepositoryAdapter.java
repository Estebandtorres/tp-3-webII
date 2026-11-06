package com.example.demo.repository;

import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.repository.entity.UsuarioEntity;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component 
public class UsuarioRepositoryAdapter implements UsuarioRepository {
    
    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = toEntity(usuario);
        UsuarioEntity entityGuardada = jpaRepository.save(entity);
        return toDomain(entityGuardada);
    }

    @Override
public Optional<Usuario> buscarPorId(Integer id) {
    return jpaRepository.findById(id).map(this::toDomain);
}

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    // --- Métodos de Mapeo Internos ---

    private Usuario toDomain(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                // Convertimos el String de la BD al Enum de tu dominio
                Rol.valueOf(entity.getRol()) 
        );
    }

    private UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setEmail(usuario.getEmail());
        entity.setPasswordHash(usuario.getPasswordHash());
        
        if (usuario.getRol() != null) {
            // Convertimos el Enum de tu dominio a String para la BD
            entity.setRol(usuario.getRol().name());
        }
        
        return entity;
    }
}