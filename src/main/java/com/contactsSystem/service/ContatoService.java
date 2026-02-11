package com.contactsSystem.service;

import com.contactsSystem.domain.Contato;
import com.contactsSystem.domain.Endereco;
import com.contactsSystem.repository.ContatosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Contato não encontrado")
        );
    }

    @Transactional
    public void salvar(Contato contato){
        repository.saveAndFlush(contato);
    }

    @Transactional
    public void atualizarPut(Long id, Contato contato){
        Contato real = buscarPorId(id);

        real.setNome(contato.getNome());
        real.setEmail(contato.getEmail());
        real.setTelefone(contato.getTelefone());
        real.setDataNascimento(contato.getDataNascimento());
        real.setEnderecos(contato.getEnderecos());

        repository.save(real);
    }

    @Transactional
    public void atualizar(Long id, Contato contato) { // utiliza o id como referencia para buscar e atualiza os dados
        Contato real = repository.findById(id).orElseThrow(() -> new RuntimeException("Contato não encontrado"));
        Contato contatoAtualizado = Contato.builder()
                .nome(contato.getNome() != null ? contato.getNome() :
                        real.getNome())
                .email(contato.getEmail() != null ? contato.getEmail() :
                        real.getEmail())
                .telefone(contato.getTelefone() != null ? contato.getTelefone() :
                        real.getTelefone())
                .dataNascimento(contato.getDataNascimento() != null ? contato.getDataNascimento() :
                        real.getDataNascimento())
                .enderecos(contato.getEnderecos() != null ? contato.getEnderecos() :
                        real.getEnderecos())
                .id(real.getId())
                .build();

        repository.saveAndFlush(contatoAtualizado);
    }


    @Transactional
    public void deletar(Long id) { // deleta o id mencionado
        repository.deleteById(id);
    }

}
