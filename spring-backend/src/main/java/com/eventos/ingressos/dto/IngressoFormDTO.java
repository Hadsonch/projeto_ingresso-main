package com.eventos.ingressos.dto;

import jakarta.validation.constraints.NotNull;

public record IngressoFormDTO(
        @NotNull(message = "Evento é obrigatório") Long eventoId
) {}
