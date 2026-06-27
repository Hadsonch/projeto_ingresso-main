package com.eventos.ingressos.controller;

import com.eventos.ingressos.dto.LoginDTO;
import com.eventos.ingressos.dto.TokenResponseDTO;
import com.eventos.ingressos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Login e geração de token JWT")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica usuário e devolve token JWT (rota pública)", security = {})
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }
}
