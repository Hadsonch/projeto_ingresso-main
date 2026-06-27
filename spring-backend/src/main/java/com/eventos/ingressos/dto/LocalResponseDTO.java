package com.eventos.ingressos.dto;

public record LocalResponseDTO(
        Long id,
        String nome,
        String cidade,
        String uf,
        Integer capacidadeMaxima
) {}
