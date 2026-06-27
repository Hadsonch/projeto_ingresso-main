package com.eventos.ingressos.dto;

public record TokenResponseDTO(String token, String tipo, Long usuarioId, String nome) {}
