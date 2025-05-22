package org.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ESTOQUE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Estoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long produto_id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "produto_id", referencedColumnName = "PRODUTO_ID")
    private Produtos produto;
    @Column(name = "ESTOQUE_ATUAL")
    private int estoque_atual;
    @Column(name = "TOTAL_ENTRADAS")
    private int entradas;
    @Column(name = "TOTAL_SAIDAS")
    private int saidas;
}
