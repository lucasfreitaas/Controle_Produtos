package org.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PRODUTOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Produtos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUTO_ID")
    private Long produto_id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "UNIDADE")
    private String unidade;
    @ManyToOne
    @JoinColumn(name = "FORNECEDOR_ID")
    private Fornecedores fornecedores;
    @Column(name = "ATIVO")
    private boolean ativo;
}
