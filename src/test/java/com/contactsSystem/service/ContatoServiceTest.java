package com.contactsSystem.service;

import com.contactsSystem.domain.Contato;
import com.contactsSystem.domain.Endereco;
import com.contactsSystem.dto.ViaCepDTO;
import com.contactsSystem.repository.ContatosRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContatoServiceTest {


    @Mock
    private ContatosRepository contatosRepository;

    @Mock
    private ViaCepService viaCepService;

    @InjectMocks
    private ContatoService contatoService;

    @Test
    @DisplayName("realizar o salvamento do contato no banco de dados")
    void salvarCase1() {
        Contato contato = new Contato(
                null,
                "Joao",
                "test@gmail.com",
                "11999999999",
                LocalDate.of(2000,1,10),
                List.of(new Endereco(null, "12345678", null, null, null, null, null, null))
        );

        when(viaCepService.buscarPorCep("12345678"))
                .thenReturn(new ViaCepDTO(
                        "12345678",   // cep
                        "Rua X",
                        "Bairro Y",
                        "Cidade Z",
                        "SP"
                )
        );

        when(contatosRepository.existsByEmail(contato.getEmail())).thenReturn(false);
        when(contatosRepository.existsByTelefone(contato.getTelefone())).thenReturn(false);

        contatoService.salvar(contato);

        verify(contatosRepository).saveAndFlush(contato);
    }
}