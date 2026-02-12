package com.contactsSystem.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity(name="contatos")
@Table(name="contatos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of="id")
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(unique = true) // definindo coluna email para receber valores unicos, evitando valores ja existentes
    private String email;

    @Column(unique = true) // definindo coluna telefone para receber valores unicos, evitando valores ja existentes
    private String telefone;

    private LocalDate dataNascimento;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Endereco>enderecos;


}
