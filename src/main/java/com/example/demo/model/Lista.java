package com.example.demo.model;

public class Lista {
    
    private Integer id;
    private String nombre;

    // Constructores
    public Lista() {
    }

    public Lista(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() { 
        return id;
    }

    public void setId(Integer id) { 
        this.id = id; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
}
