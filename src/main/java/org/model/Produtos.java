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
    @Column(name = "FORNECEDOR")
    private String fornecedor;
    @Column(name = "ATIVO")
    private boolean ativo;
}
