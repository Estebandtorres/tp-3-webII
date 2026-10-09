package com.example.demo.model;

import java.time.LocalDateTime;
public class Favorito {
    private Integer id;
    private Long productoId;
    private String notaPersonal;
    private LocalDateTime fechaAgregado;
    private Integer listaId;
public Favorito() {
}

public Favorito(Integer id, Long productoId, String notaPersonal, LocalDateTime fechaAgregado, Integer listaId) {
        this.id = id;
        this.productoId = productoId;
        this.notaPersonal = notaPersonal;
        this.fechaAgregado = fechaAgregado;
        this.listaId = listaId;
    }

    public Integer getId() { 
        return id;
    }
    public void setId(Integer id) {
        this.id = id; 
    }

    public Long getProductoId() { 
        return productoId; 
    }

    public void setProductoId(Long productoId) { 
        this.productoId = productoId; 
    }

    public String getNotaPersonal() { 
        return notaPersonal; 
    }

    public void setNotaPersonal(String notaPersonal) { 
        this.notaPersonal = notaPersonal; 
    }

    public LocalDateTime getFechaAgregado() { 
        return fechaAgregado; 
    }

    public void setFechaAgregado(LocalDateTime fechaAgregado) { 
        this.fechaAgregado = fechaAgregado; 
    }
    public Integer getListaId() { return listaId; }
    public void setListaId(Integer listaId) { this.listaId = listaId; }
}

