package com.eventos.ingressos.controller;

import com.eventos.ingressos.dto.EventoFormDTO;
import com.eventos.ingressos.dto.EventoResponseDTO;
import com.eventos.ingressos.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/eventos")
@Tag(name = "Eventos", description = "Eventos culturais (shows, peças, etc.)")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastra novo evento (requer autenticação)")
    public ResponseEntity<EventoResponseDTO> criar(@RequestBody @Valid EventoFormDTO dto, UriComponentsBuilder uri) {
        EventoResponseDTO criado = service.criar(dto);
        return ResponseEntity.created(uri.path("/eventos/{id}").buildAndExpand(criado.id()).toUri()).body(criado);
    }

    @GetMapping
    @Operation(summary = "Lista todos os eventos")
    public ResponseEntity<List<EventoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca evento por id")
    public ResponseEntity<EventoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}
