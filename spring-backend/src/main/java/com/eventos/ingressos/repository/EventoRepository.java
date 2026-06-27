package com.eventos.ingressos.repository;

import com.eventos.ingressos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}
