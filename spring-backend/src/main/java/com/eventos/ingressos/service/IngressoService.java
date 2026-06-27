package com.eventos.ingressos.service;

import com.eventos.ingressos.dto.IngressoFormDTO;
import com.eventos.ingressos.dto.IngressoResponseDTO;
import com.eventos.ingressos.dto.ResumoIngressosDTO;
import com.eventos.ingressos.exception.RecursoNaoEncontradoException;
import com.eventos.ingressos.exception.RegraNegocioException;
import com.eventos.ingressos.model.Evento;
import com.eventos.ingressos.model.Ingresso;
import com.eventos.ingressos.model.Usuario;
import com.eventos.ingressos.repository.EventoRepository;
import com.eventos.ingressos.repository.IngressoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IngressoService {

    private static final int LIMITE_INGRESSOS_POR_USUARIO_EVENTO = 5;

    private final IngressoRepository repository;
    private final EventoRepository eventoRepository;

    public IngressoService(IngressoRepository repository, EventoRepository eventoRepository) {
        this.repository = repository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public IngressoResponseDTO comprar(IngressoFormDTO dto, Usuario usuario) {
        Evento evento = eventoRepository.findById(dto.eventoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento não encontrado com id: " + dto.eventoId()));

        validarRegras(evento, usuario);

        Ingresso ingresso = Ingresso.builder()
                .usuario(usuario)
                .evento(evento)
                .dataCompra(LocalDateTime.now())
                .valorPago(evento.getPrecoIngresso())
                .build();

        return toResponse(repository.save(ingresso));
    }

    private void validarRegras(Evento evento, Usuario usuario) {
        long vendidos = repository.countByEventoId(evento.getId());
        int capacidade = evento.getLocal().getCapacidadeMaxima();
        if (vendidos >= capacidade) {
            throw new RegraNegocioException("Capacidade máxima do local atingida (" + capacidade + " ingressos)");
        }

        long jaComprados = repository.countByUsuarioIdAndEventoId(usuario.getId(), evento.getId());
        if (jaComprados >= LIMITE_INGRESSOS_POR_USUARIO_EVENTO) {
            throw new RegraNegocioException("Limite de " + LIMITE_INGRESSOS_POR_USUARIO_EVENTO +
                    " ingressos por usuário para o mesmo evento atingido");
        }
    }

    @Transactional(readOnly = true)
    public List<IngressoResponseDTO> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<IngressoResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Métricas in-memory: calcula estatísticas usando streams.
     */
    @Transactional(readOnly = true)
    public ResumoIngressosDTO obterResumo() {
        List<Ingresso> todos = repository.findAll();

        long total = todos.size();

        BigDecimal receitaTotal = todos.stream()
                .map(Ingresso::getValorPago)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Evento, Long> porEvento = todos.stream()
                .collect(Collectors.groupingBy(Ingresso::getEvento, Collectors.counting()));

        double taxaOcupacaoMedia = 0.0;
        if (!porEvento.isEmpty()) {
            double soma = porEvento.entrySet().stream()
                    .mapToDouble(en -> {
                        int cap = en.getKey().getLocal().getCapacidadeMaxima();
                        return cap > 0 ? ((double) en.getValue() / cap) : 0.0;
                    })
                    .sum();
            taxaOcupacaoMedia = soma / porEvento.size();
        }

        var topEntry = porEvento.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .orElse(null);

        String topNome = topEntry != null ? topEntry.getKey().getNome() : null;
        Long topId = topEntry != null ? topEntry.getKey().getId() : null;
        long topQtd = topEntry != null ? topEntry.getValue() : 0L;

        return new ResumoIngressosDTO(
                total,
                receitaTotal.setScale(2, RoundingMode.HALF_UP),
                Math.round(taxaOcupacaoMedia * 10000.0) / 10000.0,
                topNome,
                topId,
                topQtd
        );
    }

    private IngressoResponseDTO toResponse(Ingresso i) {
        return new IngressoResponseDTO(
                i.getId(),
                i.getUsuario().getId(),
                i.getUsuario().getNome(),
                i.getEvento().getId(),
                i.getEvento().getNome(),
                i.getDataCompra(),
                i.getValorPago()
        );
    }
}
