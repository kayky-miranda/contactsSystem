package com.contactsSystem.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cep;
    private String rua;
    private String bairro;
    private String cidade;
    private String uf;
    private String numero;


    @ManyToOne
    @JoinColumn(name = "contato_id")
    @JsonBackReference
    private Contato contato;

}
