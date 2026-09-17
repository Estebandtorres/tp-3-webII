package com.example.demo.servicios;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.demo.client.dummyjson.DummyJsonProducto;
import com.example.demo.client.dummyjson.DummyJsonProductosResponse;
import com.example.demo.servicios.dto.ProductDto;

@Service //indica que esta clase es un servicio de Spring y puede ser inyectada en otros componentes
public class ProductService {
    
    private final RestClient restClient;

    public ProductService(RestClient dummyJsonRestClient) {
        this.restClient = dummyJsonRestClient;
    }

    public List<ProductDto> obtenerTodo(){
        DummyJsonProductosResponse respuesta = restClient.get()
                .uri("/products")
                .retrieve()
                .body(DummyJsonProductosResponse.class);

                return respuesta.products().stream()
                .map(ext -> new ProductDto(ext.id(), ext.title(), ext.description(), ext.price()))
                .collect(Collectors.toList());
    }

    public ProductDto obtenerPorId(Long id) {

        DummyJsonProducto ext = restClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(DummyJsonProducto.class);

        return new ProductDto(ext.id(), ext.title(), ext.description(), ext.price());
    }
}
