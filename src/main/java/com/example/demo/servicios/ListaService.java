package com.example.demo.servicios;

import com.example.demo.model.Lista;
import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.model.Favorito;
import com.example.demo.repository.ListaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.FavoritoRepository;
import com.example.demo.servicios.dto.ListaRequestDto;
import com.example.demo.servicios.dto.ListaResponseDto;
import org.springframework.security.access.AccessDeniedException;

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
    private final UsuarioRepository usuarioRepository;

    public ListaService(ListaRepository listaRepository, FavoritoRepository favoritoRepository, UsuarioRepository usuarioRepository) {
        this.listaRepository = listaRepository;
        this.favoritoRepository = favoritoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ListaResponseDto crearLista(ListaRequestDto request, String emailUsuario) {
    Usuario usuario = usuarioRepository.buscarPorEmail(emailUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    Lista nuevaLista = ListaMapper.toDomain(request);
    nuevaLista.setUsuarioId(usuario.getId());

    Lista guardada = listaRepository.guardar(nuevaLista);
    return ListaMapper.toResponseDto(guardada);
}

    public List<ListaResponseDto> listarListas(String emailUsuario) {
        Usuario usuario = usuarioRepository.buscarPorEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<Lista> listasResultantes;

        if (usuario.getRol() == Rol.ADMIN) {
            listasResultantes = listaRepository.buscarTodas(); 
        } else {
            listasResultantes = listaRepository.buscarPorUsuarioId(usuario.getId());
        }

        return listasResultantes.stream()
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

    public Lista obtenerYValidarAcceso(Integer listaId, String emailUsuario) {
        
        Lista lista = listaRepository.buscarPorId(listaId)
                .orElseThrow(() -> new IllegalArgumentException("La lista no existe"));

        
        Usuario usuario = usuarioRepository.buscarPorEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        
        boolean esDueno = lista.getUsuarioId().equals(usuario.getId());
        boolean esAdmin = usuario.getRol() == Rol.ADMIN;

        if (!esDueno && !esAdmin) {
            
            throw new AccessDeniedException("No tenés permiso para operar sobre esta lista");
        }

        return lista; 
    }

    
    public Lista obtenerListaPorId(Integer id, String emailUsuario) {
        return obtenerYValidarAcceso(id, emailUsuario);
    }

    public void eliminarLista(Integer id, String emailUsuario) {
        Lista lista = obtenerYValidarAcceso(id, emailUsuario);
        listaRepository.borrarPorId(lista.getId()); 
    }
}