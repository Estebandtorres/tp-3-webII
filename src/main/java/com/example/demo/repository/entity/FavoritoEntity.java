package com.example.demo.repository.entity;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "favoritos")
public class FavoritoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name= "producto_id", nullable= false)
    private Long productoId;

    @Column(name = "nota")
    private String notaPersonal;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAgregado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lista_id")
    private ListaEntity lista;

    public FavoritoEntity() {}

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

    public ListaEntity getLista() { 
        return lista;
    }

    public void setLista(ListaEntity lista) { 
        this.lista = lista; 
    }
}
