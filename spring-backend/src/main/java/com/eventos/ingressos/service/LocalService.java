package com.eventos.ingressos.service;

import com.eventos.ingressos.dto.LocalFormDTO;
import com.eventos.ingressos.dto.LocalResponseDTO;
import com.eventos.ingressos.exception.RecursoNaoEncontradoException;
import com.eventos.ingressos.model.Local;
import com.eventos.ingressos.repository.LocalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocalService {

    private final LocalRepository repository;

    public LocalService(LocalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public LocalResponseDTO criar(LocalFormDTO dto) {
        Local l = Local.builder()
                .nome(dto.nome())
                .cidade(dto.cidade())
                .uf(dto.uf().toUpperCase())
                .capacidadeMaxima(dto.capacidadeMaxima())
                .build();
        return toResponse(repository.save(l));
    }

    public List<LocalResponseDTO> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public LocalResponseDTO buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Local não encontrado com id: " + id)));
    }

    public LocalResponseDTO toResponse(Local l) {
        return new LocalResponseDTO(l.getId(), l.getNome(), l.getCidade(), l.getUf(), l.getCapacidadeMaxima());
    }
}
