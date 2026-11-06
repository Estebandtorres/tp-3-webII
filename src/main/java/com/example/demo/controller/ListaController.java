package com.example.demo.controller;

import com.example.demo.servicios.ListaService;
import com.example.demo.servicios.dto.ListaMapper;
import com.example.demo.servicios.dto.ListaRequestDto;
import com.example.demo.servicios.dto.ListaResponseDto;
import com.example.demo.servicios.dto.MoverFavoritosRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.demo.model.Favorito;
import com.example.demo.model.Lista;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "Listas", description = "Endpoints para la administración de las listas de favoritos")
public class ListaController {

    private final ListaService listaService;

    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    
    @PostMapping
    @Operation(summary = "Crear una nueva lista", description = "Guarda una nueva lista con el nombre proporcionado.")
    public ResponseEntity<ListaResponseDto> crearLista(@Valid @RequestBody ListaRequestDto request, Authentication authentication) {
    
    String emailUsuario = authentication.getName();
    
    
    ListaResponseDto nuevaLista = listaService.crearLista(request, emailUsuario); 
    
    return ResponseEntity.status(HttpStatus.CREATED).body(nuevaLista);
}

    
    @GetMapping
    @Operation(summary = "Traer todas las listas", description = "Devuelve todas las listas de favoritos existentes.")
    public ResponseEntity<List<ListaResponseDto>> listarListas(Authentication authentication) {
        
        List<ListaResponseDto> listas = listaService.listarListas(authentication.getName());
        return ResponseEntity.ok(listas);
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una lista por ID", description = "Devuelve los detalles de una lista especifica por su ID.")
    public ResponseEntity<?> obtenerLista(@PathVariable Integer id, Authentication authentication) {
        try {
            Lista lista = listaService.obtenerListaPorId(id, authentication.getName());
            return ResponseEntity.ok(ListaMapper.toResponseDto(lista));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    
    @GetMapping("/{id}/favoritos")
    @Operation(summary = "Obtener favoritos de una lista", description = "Devuelve todos los favoritos asociados a una lista especifica.")
    public ResponseEntity<List<Favorito>> obtenerFavoritosDeLista(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(listaService.obtenerFavoritosDeLista(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una lista vacia", description = "Elimina una lista especifica si no tiene favoritos asociados.")
    public ResponseEntity<?> eliminarLista(@PathVariable Integer id, Authentication authentication) {
        try {
            listaService.eliminarLista(id, authentication.getName());
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{origenId}/mover-favoritos")
    @Operation(summary = "Mover favoritos de una lista a otra", description = "Mueve todos los favoritos de la lista origen a la lista destino y elimina la lista origen.")
    public ResponseEntity<String> moverFavoritos(
            @PathVariable Integer origenId,
            @Valid @RequestBody MoverFavoritosRequestDto request) {
        
        try {
            listaService.moverFavoritosYBorrarOrigen(origenId, request.destinoId());
            return ResponseEntity.ok("Favoritos movidos exitosamente y lista origen eliminada.");
        } catch (IllegalArgumentException e) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
