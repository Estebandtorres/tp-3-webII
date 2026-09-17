package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.servicios.ProductService;
import com.example.demo.servicios.dto.ProductDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Catalogo de Productos", description = "Endpoints de solo lectura que consumen la API externa DummyJSON")
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Obtiene el catalogo completo desde DummyJSON y lo devuelve con nuestra propia estructura DTO.")
    public List<ProductDto> getAll(){
        return productService.obtenerTodo();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID", description = "Busca un producto especifico en la API externa por su id.")
    public ProductDto getById(@PathVariable Long id) {
        return productService.obtenerPorId(id);
    }
}
