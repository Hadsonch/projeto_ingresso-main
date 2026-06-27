package com.eventos.ingressos.dto;

import com.eventos.ingressos.model.TipoUsuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        TipoUsuario tipo,
        EnderecoResponseDTO endereco
) {}
