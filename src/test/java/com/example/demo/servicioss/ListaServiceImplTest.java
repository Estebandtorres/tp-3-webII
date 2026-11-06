package com.example.demo.servicioss;

import com.example.demo.model.Lista;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ListaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.servicios.ListaServiceImpl;
import com.example.demo.servicios.dto.ListaRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListaServiceImplTest {

    @Mock
    private ListaRepository listaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ListaServiceImpl listaService;

    @Test
    void crearLista_usuarioAutenticado_asociaListaAlUsuario() {
        // GIVEN
        String email = "test@ejemplo.com";
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(5);
        
        ListaRequestDto request = new ListaRequestDto("Nueva Lista");

        when(usuarioRepository.buscarPorEmail(email)).thenReturn(Optional.of(usuarioMock));
        when(listaRepository.guardar(any(Lista.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN
        listaService.crearLista(request, email);

        // THEN
        ArgumentCaptor<Lista> captor = ArgumentCaptor.forClass(Lista.class);
        verify(listaRepository).guardar(captor.capture());
        
        assertEquals(5, captor.getValue().getUsuarioId(), "La lista debe quedar asociada al ID del usuario autenticado");
    }
}
