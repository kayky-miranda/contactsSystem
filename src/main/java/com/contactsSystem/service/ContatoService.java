package com.contactsSystem.service;

import com.contactsSystem.domain.Contato;
import com.contactsSystem.repository.ContatosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContatoService { // RESTful

    private final ContatosRepository repository;

    public ContatoService(ContatosRepository repository) {
        this.repository = repository;
    }

    public List<Contato> listar(){ // metodo listar contatos salvos
        return repository.findAll();
    }

    public Contato buscarPorId(Long id){ // busca um contato salvo pelo id
        return repository.findById(id).orElseThrow();
    }

    public Contato salvar(Contato contato){ // realiza o salvamento do contato
        return repository.save(contato);
    }

    public Contato atualizar(Long id, Contato contato){ // utiliza o id como referencia para buscar e atualiza os dados
        Contato real = buscarPorId(id);

        contato.setNome(contato.getNome());
        contato.setEmail(contato.getEmail());
        contato.setTelefone(contato.getTelefone());
        contato.setDataNascimento(contato.getDataNascimento());
        contato.setEnderecos(contato.getEnderecos());

        return repository.save(real);
    }

    public void deletar(Long id) { // deleta o id mencionado
        repository.deleteById(id);
    }

}
