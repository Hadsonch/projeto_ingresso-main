package com.eventos.ingressos.dto;

import com.eventos.ingressos.model.TipoUsuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record UsuarioFormDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank @Email(message = "Email inválido") String email,
        @NotBlank @Size(min = 6, message = "Senha deve ter ao menos 6 caracteres") String senha,
        @NotNull(message = "Tipo é obrigatório") TipoUsuario tipo,
        @Valid @NotNull(message = "Endereço é obrigatório") EnderecoFormDTO endereco
) {}
