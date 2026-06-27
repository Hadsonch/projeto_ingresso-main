package com.eventos.ingressos.repository;

import com.eventos.ingressos.model.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
    long countByEventoId(Long eventoId);
    long countByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);
    List<Ingresso> findByUsuarioId(Long usuarioId);
}
