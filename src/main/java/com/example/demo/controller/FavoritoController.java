package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.servicios.FavoritoService;
import com.example.demo.servicios.dto.FavoritoRequestDto;
import com.example.demo.servicios.dto.FavoritoResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/favoritos")
@Tag(name = "Favoritos", description = "CRUD para gestionar los productos favoritos del usuario")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un nuevo favorito", description = "Guarda un producto como favorito")
    public FavoritoResponseDto crear(@Valid @RequestBody FavoritoRequestDto requestDTO) {
        return favoritoService.crear(requestDTO);
    }

    @GetMapping
    @Operation(summary = "Listar todos los favoritos", description = "Devuelve la lista completa de favoritos guardados en memoria.")
    public List<FavoritoResponseDto> listar() {
        return favoritoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un favorito por ID", description = "Devuelve los detalles de un favorito especifico.")
    public FavoritoResponseDto obtenerUno(@PathVariable Long id) {
        return favoritoService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un favorito", description = "Modifica el producto referenciado o la nota personal de un favorito existente.")
    public FavoritoResponseDto actualizar(@PathVariable Long id, @Valid @RequestBody FavoritoRequestDto requestDTO) {
        return favoritoService.actualizar(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un favorito", description = "Borra un favorito de la memoria a partir de su ID.")
    public void eliminar(@PathVariable Long id) {
        favoritoService.eliminar(id);
    }
}
