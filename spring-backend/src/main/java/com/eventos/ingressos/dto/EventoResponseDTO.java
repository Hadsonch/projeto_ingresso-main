package com.eventos.ingressos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventoResponseDTO(
        Long id,
        String nome,
        String descricao,
        LocalDateTime dataHora,
        BigDecimal precoIngresso,
        LocalResponseDTO local
) {}
