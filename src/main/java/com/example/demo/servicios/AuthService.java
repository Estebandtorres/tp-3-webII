package com.example.demo.servicios;

import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.servicios.dto.RegistroRequestDto;
import com.example.demo.servicios.dto.LoginRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service 
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void registrar(RegistroRequestDto request) {
        // 1. Verificamos que el email no exista
        if (usuarioRepository.existePorEmail(request.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // 2. Hasheamos la contraseña
        String passwordHasheada = passwordEncoder.encode(request.password());

        // 3. Creamos el usuario con rol USER por defecto
        Usuario nuevoUsuario = new Usuario(null, request.email(), passwordHasheada, Rol.USER);

        // 4. Guardamos
        usuarioRepository.guardar(nuevoUsuario);
    }

        public String login(LoginRequestDto request) {
        // 1. Buscamos al usuario por email
        Usuario usuario = usuarioRepository.buscarPorEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        // 2. Verificamos que la contraseña plana coincida con el hash guardado
        if (!passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // 3. Generamos y devolvemos el JWT
        return jwtService.generarToken(usuario);
    }
}
