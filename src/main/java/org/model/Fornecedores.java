package org.model;


import jakarta.persistence.*;
import javafx.fxml.FXML;
import lombok.*;

@Entity
@Table(name = "FORNECEDORES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Fornecedores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FORNECEDOR_ID")
    private Long fornecedor_id;
    @Column(name = "RAZAO_SOCIAL")
    private String nome;
    @Column(name = "CNPJ_FORNECEDOR")
    private String CNPJ;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "CIDADE")
    private String cidade;
    @Column(name = "ATIVO")
    private boolean ativo;
}
