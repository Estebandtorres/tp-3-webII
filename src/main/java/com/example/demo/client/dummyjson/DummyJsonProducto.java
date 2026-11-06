package com.example.demo.client.dummyjson;


public record DummyJsonProducto(
        Long id,
        String title,
        String description,
        String category,
        String brand,
        double price,
        double discountPercentage,
        int stock,
        double rating,
        String thumbnail
) {
}
