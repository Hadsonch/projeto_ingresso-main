package com.eventos.ingressos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IngressoResponseDTO(
        Long id,
        Long usuarioId,
        String usuarioNome,
        Long eventoId,
        String eventoNome,
        LocalDateTime dataCompra,
        BigDecimal valorPago
) {}
