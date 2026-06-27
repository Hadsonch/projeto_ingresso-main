package com.eventos.ingressos.dto;

import jakarta.validation.constraints.*;

public record LocalFormDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "Cidade é obrigatória") String cidade,
        @NotBlank @Size(min = 2, max = 2) String uf,
        @NotNull @Min(value = 1, message = "Capacidade deve ser maior que 0") Integer capacidadeMaxima
) {}
