package com.eventos.ingressos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoFormDTO(
        @NotBlank(message = "Logradouro é obrigatório") String logradouro,
        @NotBlank(message = "Número é obrigatório") String numero,
        String complemento,
        @NotBlank(message = "Bairro é obrigatório") String bairro,
        @NotBlank(message = "Cidade é obrigatória") String cidade,
        @NotBlank @Size(min = 2, max = 2, message = "UF deve ter 2 caracteres") String uf,
        @NotBlank @Size(min = 8, max = 9) String cep
) {}
