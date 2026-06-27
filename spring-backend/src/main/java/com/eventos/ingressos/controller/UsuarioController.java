package com.eventos.ingressos.controller;

import com.eventos.ingressos.dto.UsuarioFormDTO;
import com.eventos.ingressos.dto.UsuarioResponseDTO;
import com.eventos.ingressos.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Cadastro e consulta de usuários")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastra novo usuário (rota pública)", security = {})
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioFormDTO dto,
                                                       UriComponentsBuilder uri) {
        UsuarioResponseDTO criado = service.cadastrar(dto);
        var uriBuilt = uri.path("/usuarios/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(uriBuilt).body(criado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca usuário por id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
