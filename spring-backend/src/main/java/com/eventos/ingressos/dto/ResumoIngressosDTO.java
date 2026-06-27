package com.eventos.ingressos.dto;

import java.math.BigDecimal;

public record ResumoIngressosDTO(
        long totalIngressosVendidos,
        BigDecimal receitaTotal,
        double taxaOcupacaoMedia,
        String eventoMaisVendidoNome,
        Long eventoMaisVendidoId,
        long eventoMaisVendidoQtd
) {}
