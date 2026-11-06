package com.example.demo.repository.entity;

import jakarta.persistence.*;

@Entity 
@Table (name = "usuarios")
public class UsuarioEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String rol;

    public UsuarioEntity() {}

    public Integer getId() { 
        return id; 
    }

    public void setId(Integer id) { 
        this.id = id; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }  

    public String getPasswordHash() { 
        return passwordHash; 
    }

    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }

    public String getRol() { 
        return rol; 
    }

    public void setRol(String rol) { 
        this.rol = rol; 
    }
}
