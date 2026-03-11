package com.contactsSystem.repository;

import com.contactsSystem.domain.Contato;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@DataJpaTest // classe de test para o JPA
@ActiveProfiles("test") // utilizar o arquivo de teste
class ContatosRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Teste unitário para verificar se o contato existe pelo e-mail")
    void existsByEmailCase1() {

        Contato contato = new Contato(
                null,
                "Joao",
                "test@gmail.com",
                "11999999999",
                LocalDate.of(2000,1,10),
                null
        );
        this.createContato(contato);
    }

    private Contato createContato(Contato contato){
        this.entityManager.persist(contato);
        return contato;
    }
}