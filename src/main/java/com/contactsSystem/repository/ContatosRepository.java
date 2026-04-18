package com.contactsSystem.repository;

import com.contactsSystem.domain.Contato;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContatosRepository extends JpaRepository<Contato, Long> {
    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);

    List<Contato> findAll();

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByTelefoneAndIdNot(String telefone, Long id);
}
 