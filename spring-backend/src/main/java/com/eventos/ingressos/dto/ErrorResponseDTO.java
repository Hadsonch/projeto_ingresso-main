package com.eventos.ingressos.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<FieldErrorDTO> errors
) {
    public record FieldErrorDTO(String field, String message) {}
}
