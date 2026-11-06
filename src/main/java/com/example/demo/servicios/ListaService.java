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
    // 1. Buscamos al usuario real usando el email extraído del token JWT
    Usuario usuario = usuarioRepository.buscarPorEmail(emailUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    // 2. Convertimos el request a dominio usando tu Mapper
    Lista nuevaLista = ListaMapper.toDomain(request);

    // 3. Forzamos que el dueño de la lista sea el usuario autenticado
    nuevaLista.setUsuarioId(usuario.getId());

    // 4. Guardamos la lista de forma segura y devolvemos el DTO
    Lista guardada = listaRepository.guardar(nuevaLista);
    return ListaMapper.toResponseDto(guardada);
}

    public List<ListaResponseDto> listarListas(String emailUsuario) {
        // 1. Buscamos al usuario autenticado para saber su ID y su Rol
        Usuario usuario = usuarioRepository.buscarPorEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<Lista> listasResultantes;

        // 2. Aplicamos la regla de negocio
        if (usuario.getRol() == Rol.ADMIN) {
            // Si es ADMIN, trae todas sin importar de quién sean
            listasResultantes = listaRepository.buscarTodas(); 
        } else {
            // Si es USER, trae únicamente las que coincidan con su ID
            listasResultantes = listaRepository.buscarPorUsuarioId(usuario.getId());
        }

        // 3. Mapeamos a DTO y devolvemos
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

    // 1. Método centralizado de validación
    public Lista obtenerYValidarAcceso(Integer listaId, String emailUsuario) {
        // A. Buscamos la lista. Si no existe, tiramos error para 404.
        Lista lista = listaRepository.buscarPorId(listaId)
                .orElseThrow(() -> new IllegalArgumentException("La lista no existe"));

        // B. Buscamos al usuario logueado
        Usuario usuario = usuarioRepository.buscarPorEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // C. Lógica de autorización: ¿Es el dueño? ¿O es ADMIN?
        boolean esDueno = lista.getUsuarioId().equals(usuario.getId());
        boolean esAdmin = usuario.getRol() == Rol.ADMIN;

        if (!esDueno && !esAdmin) {
            // Si no cumple ninguna, disparamos la excepción de seguridad (403)
            throw new AccessDeniedException("No tenés permiso para operar sobre esta lista");
        }

        return lista; // Si pasa las validaciones, devolvemos la lista
    }

    // 2. Adaptamos los métodos públicos para que usen la validación
    public Lista obtenerListaPorId(Integer id, String emailUsuario) {
        return obtenerYValidarAcceso(id, emailUsuario);
    }

    public void eliminarLista(Integer id, String emailUsuario) {
        Lista lista = obtenerYValidarAcceso(id, emailUsuario);
        listaRepository.borrarPorId(lista.getId()); // Asumiendo que tenés un método eliminar
    }
}