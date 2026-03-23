package com.contactsSystem.repository;

import com.contactsSystem.domain.Contato;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // classe de test para o JPA
@ActiveProfiles("test") // utilizar o arquivo de teste
class ContatosRepositoryTest {

    @Autowired
    ContatosRepository contatosRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Teste unitário para verificar se o contato existe pelo e-mail")
    void existsByEmailCase1() {
        String email = "test@gmail.com";
        Contato contato = new Contato(
                null,
                "username",
                email,
                "11999999999",
                LocalDate.of(2000,1,10),
                null
        );
        this.createContato(contato);

        boolean result = this.contatosRepository.existsByEmail(email);

        assertTrue(result);
    }

    @Test
    @DisplayName("Teste unitário para verificar se o contato não existe pelo e-mail")
    void existsByEmailCase2() {
        String email = "test@gmail.com";

        boolean result = this.contatosRepository.existsByEmail(email);

        assertFalse(result);
    }

    @Test
    @DisplayName("Teste unitário para verificar se o contato existe pelo e-mail")
    void existByTelefoneCase1(){

        String telefone = "11999999999";
        Contato contato = new Contato(
                null,
                "username",
                "test@gmail.com",
                telefone,
                LocalDate.of(2000,1,10),
                null
        );
        this.createContato(contato);

        boolean result = this.contatosRepository.existsByTelefone(telefone);
        assertTrue(result);
    }

    @Test
    @DisplayName("Teste unitário para verificar se o contato não existe pelo e-mail")
    void existsByTelefoneCase2() {
        String telefone = "11999999999";

        boolean result = this.contatosRepository.existsByTelefone(telefone);

        assertFalse(result);
    }

    private Contato createContato(Contato contato){
        this.entityManager.persist(contato);
        return contato;
    }
}