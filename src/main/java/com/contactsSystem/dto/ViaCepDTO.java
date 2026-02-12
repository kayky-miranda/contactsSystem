package com.contactsSystem.dto;

public record ViaCepDTO(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf
) {}