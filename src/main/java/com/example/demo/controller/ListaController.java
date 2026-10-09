package com.example.demo.controller;

import com.example.demo.servicios.ListaService;
import com.example.demo.servicios.dto.ListaRequestDto;
import com.example.demo.servicios.dto.ListaResponseDto;
import com.example.demo.servicios.dto.MoverFavoritosRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.demo.model.Favorito;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "Listas", description = "Endpoints para la administración de las listas de favoritos")
public class ListaController {

    private final ListaService listaService;

    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    // 1. Crear lista (POST /api/listas)
    @PostMapping
    @Operation(summary = "Crear una nueva lista", description = "Guarda una nueva lista con el nombre proporcionado.")
    public ResponseEntity<ListaResponseDto> crearLista(@Valid @RequestBody ListaRequestDto request) {
        ListaResponseDto nuevaLista = listaService.crearLista(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaLista);
    }

    // 2. Listar listas (GET /api/listas)
    @GetMapping
    @Operation(summary = "Listar todas las listas", description = "Devuelve todas las listas de favoritos existentes.")
    public ResponseEntity<List<ListaResponseDto>> listarListas() {
        return ResponseEntity.ok(listaService.listarTodas());
    }

    // 3. Obtener una lista (GET /api/listas/{id})
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una lista por ID", description = "Devuelve los detalles de una lista especifica por su ID.")
    public ResponseEntity<ListaResponseDto> obtenerLista(@PathVariable Integer id) {
        return listaService.obtenerLista(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Favoritos de una lista (GET /api/listas/{id}/favoritos)
    @GetMapping("/{id}/favoritos")
    @Operation(summary = "Obtener favoritos de una lista", description = "Devuelve todos los favoritos asociados a una lista especifica.")
    public ResponseEntity<List<Favorito>> obtenerFavoritosDeLista(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(listaService.obtenerFavoritosDeLista(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Eliminar una lista vacía (DELETE /api/listas/{id})
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una lista vacia", description = "Elimina una lista especifica si no tiene favoritos asociados.")
    public ResponseEntity<Void> eliminarLista(@PathVariable Integer id) {
        listaService.eliminarLista(id);
        return ResponseEntity.noContent().build();
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
            // Si el servicio lanza el error de que no existe alguna lista, devolvemos 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
