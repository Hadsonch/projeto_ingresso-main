package com.eventos.ingressos.service;

import com.eventos.ingressos.dto.LoginDTO;
import com.eventos.ingressos.dto.TokenResponseDTO;
import com.eventos.ingressos.model.Usuario;
import com.eventos.ingressos.repository.UsuarioRepository;
import com.eventos.ingressos.security.TokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UsuarioRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public TokenResponseDTO login(LoginDTO dto) {
        Usuario usuario = repository.findByEmail(dto.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = tokenService.gerarToken(usuario);
        return new TokenResponseDTO(token, "Bearer", usuario.getId(), usuario.getNome());
    }
}
