package com.eventos.ingressos.service;

import com.eventos.ingressos.dto.EventoFormDTO;
import com.eventos.ingressos.dto.EventoResponseDTO;
import com.eventos.ingressos.dto.LocalResponseDTO;
import com.eventos.ingressos.exception.RecursoNaoEncontradoException;
import com.eventos.ingressos.model.Evento;
import com.eventos.ingressos.model.Local;
import com.eventos.ingressos.repository.EventoRepository;
import com.eventos.ingressos.repository.LocalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository repository;
    private final LocalRepository localRepository;

    public EventoService(EventoRepository repository, LocalRepository localRepository) {
        this.repository = repository;
        this.localRepository = localRepository;
    }

    @Transactional
    public EventoResponseDTO criar(EventoFormDTO dto) {
        Local local = localRepository.findById(dto.localId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Local não encontrado com id: " + dto.localId()));

        Evento e = Evento.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .dataHora(dto.dataHora())
                .precoIngresso(dto.precoIngresso())
                .local(local)
                .build();
        return toResponse(repository.save(e));
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EventoResponseDTO buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public Evento buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento não encontrado com id: " + id));
    }

    public EventoResponseDTO toResponse(Evento e) {
        Local l = e.getLocal();
        return new EventoResponseDTO(
                e.getId(),
                e.getNome(),
                e.getDescricao(),
                e.getDataHora(),
                e.getPrecoIngresso(),
                new LocalResponseDTO(l.getId(), l.getNome(), l.getCidade(), l.getUf(), l.getCapacidadeMaxima())
        );
    }
}
