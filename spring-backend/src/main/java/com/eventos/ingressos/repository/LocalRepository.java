package com.eventos.ingressos.repository;

import com.eventos.ingressos.model.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, Long> {
}
