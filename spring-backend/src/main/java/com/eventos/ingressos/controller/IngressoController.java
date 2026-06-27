package com.eventos.ingressos.controller;

import com.eventos.ingressos.dto.IngressoFormDTO;
import com.eventos.ingressos.dto.IngressoResponseDTO;
import com.eventos.ingressos.dto.ResumoIngressosDTO;
import com.eventos.ingressos.model.Usuario;
import com.eventos.ingressos.service.IngressoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/ingressos")
@Tag(name = "Ingressos", description = "Compra, listagem e resumo estatístico de ingressos")
public class IngressoController {

    private final IngressoService service;

    public IngressoController(IngressoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Compra ingresso para um evento (usuário autenticado)")
    public ResponseEntity<IngressoResponseDTO> comprar(@RequestBody @Valid IngressoFormDTO dto,
                                                      @AuthenticationPrincipal Usuario usuario,
                                                      UriComponentsBuilder uri) {
        IngressoResponseDTO criado = service.comprar(dto, usuario);
        return ResponseEntity.created(uri.path("/ingressos/{id}").buildAndExpand(criado.id()).toUri()).body(criado);
    }

    @GetMapping("/meus")
    @Operation(summary = "Lista ingressos comprados pelo usuário autenticado")
    public ResponseEntity<List<IngressoResponseDTO>> meus(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.listarPorUsuario(usuario.getId()));
    }

    @GetMapping
    @Operation(summary = "Lista todos os ingressos")
    public ResponseEntity<List<IngressoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/resumo")
    @Operation(summary = "Resumo estatístico de ingressos (cálculo in-memory)")
    public ResponseEntity<ResumoIngressosDTO> resumo() {
        return ResponseEntity.ok(service.obterResumo());
    }
}
