package com.example.demo.model;

public class Lista {
    
    private Integer id;
    private String nombre;
    private Integer usuarioId;

    // Constructores
    public Lista() {
    }

    public Lista(Integer id, String nombre, Integer usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.usuarioId = usuarioId;
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

    public Integer getUsuarioId() { 
        return usuarioId; 
    }

    public void setUsuarioId(Integer usuarioId) { 
        this.usuarioId = usuarioId; 
    }
}
