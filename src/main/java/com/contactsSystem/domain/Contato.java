package com.contactsSystem.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    @Column(nullable = false, unique = true, length = 150)
    private String nome;

    @Email(message = "Email inválido")
    @Column(unique = true, nullable = false, length = 150) // definindo coluna email para receber valores unicos, evitando valores ja existentes
    private String email;

    @Column(unique = true, length = 20) // definindo coluna telefone para receber valores unicos, evitando valores ja existentes
    private String telefone;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Endereco> enderecos;
}
