package com.eventos.ingressos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locais")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String uf;

    @Column(nullable = false)
    private Integer capacidadeMaxima;
}
