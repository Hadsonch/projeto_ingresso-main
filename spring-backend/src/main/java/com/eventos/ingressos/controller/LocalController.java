package com.eventos.ingressos.controller;

import com.eventos.ingressos.dto.LocalFormDTO;
import com.eventos.ingressos.dto.LocalResponseDTO;
import com.eventos.ingressos.service.LocalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/locais")
@Tag(name = "Locais", description = "Estabelecimentos onde ocorrem os eventos")
public class LocalController {

    private final LocalService service;

    public LocalController(LocalService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastra novo local (requer autenticação)")
    public ResponseEntity<LocalResponseDTO> criar(@RequestBody @Valid LocalFormDTO dto, UriComponentsBuilder uri) {
        LocalResponseDTO criado = service.criar(dto);
        return ResponseEntity.created(uri.path("/locais/{id}").buildAndExpand(criado.id()).toUri()).body(criado);
    }

    @GetMapping
    @Operation(summary = "Lista todos os locais")
    public ResponseEntity<List<LocalResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca local por id")
    public ResponseEntity<LocalResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
