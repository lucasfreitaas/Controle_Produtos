package org.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FUNCIONARIOS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Funcionarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FUNCIONARIO_ID")
    private Long funcionario_id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "senha")
    private String senha;
    @Column(name = "BAIRRO")
    private String bairro;
    @Column(name = "ENDERECO")
    private String endereco;
    @Column(name = "COMPLEMENTO")
    private String complemento;
}
