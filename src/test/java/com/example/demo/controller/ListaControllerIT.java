package com.example.demo.controller;

import com.example.demo.model.Lista;
import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ListaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.servicios.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ListaControllerIT extends AbstractBaseIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ListaRepository listaRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    private String tokenUser1;
    private String tokenUser2;
    private String tokenAdmin;
    private Integer listaUser1Id;

    @BeforeEach
    void setup() {

        Usuario user1 = new Usuario();
        user1.setEmail("user1@test.com");
        user1.setPasswordHash(passwordEncoder.encode("pass123"));
        user1.setRol(Rol.USER);
        user1 = usuarioRepository.guardar(user1);
        tokenUser1 = "Bearer " + jwtService.generarToken(user1);

        Usuario user2 = new Usuario();
        user2.setEmail("user2@test.com");
        user2.setPasswordHash(passwordEncoder.encode("pass123"));
        user2.setRol(Rol.USER);
        user2 = usuarioRepository.guardar(user2);
        tokenUser2 = "Bearer " + jwtService.generarToken(user2);

        Usuario admin = new Usuario();
        admin.setEmail("admin@test.com");
        admin.setPasswordHash(passwordEncoder.encode("pass123"));
        admin.setRol(Rol.ADMIN);
        admin = usuarioRepository.guardar(admin);
        tokenAdmin = "Bearer " + jwtService.generarToken(admin);

        Lista lista = new Lista();
        lista.setNombre("Lista de User 1");
        lista.setUsuarioId(user1.getId());
        Lista guardada = listaRepository.guardar(lista);
        listaUser1Id = guardada.getId();
    }

    @Test
    void login_credencialesValidas_devuelve200YToken() throws Exception {
        String loginJson = "{\"email\":\"user1@test.com\", \"password\":\"pass123\"}";
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_credencialesInvalidas_devuelve401() throws Exception {
        String loginJson = "{\"email\":\"user1@test.com\", \"password\":\"mal_password\"}";
        
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void obtenerLista_listaPropia_devuelve200() throws Exception {
        mockMvc.perform(get("/api/listas/" + listaUser1Id)
                .header("Authorization", tokenUser1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Lista de User 1"));
    }

    @Test
    void obtenerLista_listaAjena_devuelve403() throws Exception {
        mockMvc.perform(get("/api/listas/" + listaUser1Id)
                .header("Authorization", tokenUser2))
                .andExpect(status().isForbidden());
    }

    @Test
    void obtenerListas_sinToken_devuelve401() throws Exception {
        mockMvc.perform(get("/api/listas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void obtenerLista_adminListaAjena_devuelve200() throws Exception {
        mockMvc.perform(get("/api/listas/" + listaUser1Id)
                .header("Authorization", tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Lista de User 1"));
    }
}