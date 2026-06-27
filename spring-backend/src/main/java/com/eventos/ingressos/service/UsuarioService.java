package com.eventos.ingressos.service;

import com.eventos.ingressos.dto.*;
import com.eventos.ingressos.exception.RegraNegocioException;
import com.eventos.ingressos.exception.RecursoNaoEncontradoException;
import com.eventos.ingressos.model.Endereco;
import com.eventos.ingressos.model.Usuario;
import com.eventos.ingressos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioFormDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new RegraNegocioException("Email já cadastrado");
        }

        Endereco endereco = Endereco.builder()
                .logradouro(dto.endereco().logradouro())
                .numero(dto.endereco().numero())
                .complemento(dto.endereco().complemento())
                .bairro(dto.endereco().bairro())
                .cidade(dto.endereco().cidade())
                .uf(dto.endereco().uf().toUpperCase())
                .cep(dto.endereco().cep())
                .build();

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .tipo(dto.tipo())
                .endereco(endereco)
                .build();

        Usuario salvo = repository.save(usuario);
        return toResponse(salvo);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario u = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id: " + id));
        return toResponse(u);
    }

    private UsuarioResponseDTO toResponse(Usuario u) {
        var endDTO = new EnderecoResponseDTO(
                u.getEndereco().getId(),
                u.getEndereco().getLogradouro(),
                u.getEndereco().getNumero(),
                u.getEndereco().getComplemento(),
                u.getEndereco().getBairro(),
                u.getEndereco().getCidade(),
                u.getEndereco().getUf(),
                u.getEndereco().getCep()
        );
        return new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getTipo(), endDTO);
    }
}
