package com.eventos.ingressos.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventoFormDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "Descrição é obrigatória") String descricao,
        @NotNull(message = "Data/hora é obrigatória") @Future(message = "Data/hora deve ser futura") LocalDateTime dataHora,
        @NotNull @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero") BigDecimal precoIngresso,
        @NotNull(message = "Local é obrigatório") Long localId
) {}
