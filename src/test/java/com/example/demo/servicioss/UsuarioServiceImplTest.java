package com.example.demo.servicioss;


import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.servicios.dto.RegistroRequestDto;
import com.example.demo.servicios.AuthService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void registrar_credencialesValidas_nuncaGuardaPasswordEnTextoPlano() {

        String passPlano = "secreto123";
        String passHash = "$2a$10$hashSimulado...";
        
    RegistroRequestDto request = new RegistroRequestDto("test@ejemplo.com", passPlano); 

    when(passwordEncoder.encode(passPlano)).thenReturn(passHash);

    authService.registrar(request);

        
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).guardar(captor.capture());

        Usuario guardado = captor.getValue();
        assertNotEquals(passPlano, guardado.getPasswordHash());
        assertEquals(passHash, guardado.getPasswordHash());
    }
}