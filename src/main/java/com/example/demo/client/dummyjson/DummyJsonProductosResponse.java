package com.example.demo.client.dummyjson;

import java.util.List;


public record DummyJsonProductosResponse(
        List<DummyJsonProducto> products,
        int total,
        int skip,
        int limit
) {
}
