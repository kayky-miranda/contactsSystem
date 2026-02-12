package com.contactsSystem.repository;

import com.contactsSystem.domain.Contato;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatosRepository extends JpaRepository<Contato, Long> {
    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);
}
