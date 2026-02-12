package com.contactsSystem.service;

import com.contactsSystem.domain.Contato;
import com.contactsSystem.domain.Endereco;
import com.contactsSystem.exception.CepInvalidoException;
import com.contactsSystem.exception.DataInvalidaException;
import com.contactsSystem.exception.InfosJaExistenteException;
import com.contactsSystem.repository.ContatosRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContatoService { // RESTful



    private final ContatosRepository repository;
    private final ViaCepService viaCepService; // injeção do viaCep em contatos

    public ContatoService(ContatosRepository repository, ViaCepService viaCepService) {
        this.repository = repository;
        this.viaCepService = viaCepService;
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
    public void salvar(Contato contato) {

        if (contato.getEnderecos() != null) {

            contato.getEnderecos().forEach(endereco -> {

                if (endereco.getCep() != null) {
                    validarCep(endereco.getCep()); //valida o cep
                    var viaCep = viaCepService.buscarPorCep(endereco.getCep());

                    endereco.setRua(viaCep.logradouro());
                    endereco.setBairro(viaCep.bairro());
                    endereco.setCidade(viaCep.localidade());
                    endereco.setUf(viaCep.uf());


                    endereco.setContato(contato);
                }
            });
        }

        if (repository.existsByEmail(contato.getEmail())) {
            throw new InfosJaExistenteException("E-mail já cadastrado");
        }
        if (repository.existsByTelefone(contato.getTelefone())) {
            throw new InfosJaExistenteException("Telefone já cadastrado");
        }
        validarData(contato.getDataNascimento());
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

        if (contato.getEnderecos() != null) {

            for (Endereco novo : contato.getEnderecos()) {

                Endereco existente = real.getEnderecos().stream()
                        .filter(e -> e.getId().equals(novo.getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

                if (novo.getCep() != null) {
                    validarCep(novo.getCep()); //valida o cep
                    var viaCep = viaCepService.buscarPorCep(novo.getCep());


                    existente.setCep(novo.getCep());
                    existente.setRua(viaCep.logradouro());
                    existente.setBairro(viaCep.bairro());
                    existente.setCidade(viaCep.localidade());
                    existente.setUf(viaCep.uf());
                }

                if (novo.getNumero() != null)
                    existente.setNumero(novo.getNumero());

            }
        }

        if (repository.existsByEmail(contato.getEmail())) {
            throw new InfosJaExistenteException("E-mail já cadastrado");
        }
        if (repository.existsByTelefone(contato.getTelefone())) {
            throw new InfosJaExistenteException("Telefone já cadastrado");
        }
        validarData(contato.getDataNascimento());


        repository.save(real);
    }

    @Transactional
    public void atualizar(Long id, Contato contato) {

        Contato real = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado"));

        if (contato.getNome() != null)
            real.setNome(contato.getNome());

        if (contato.getEmail() != null)
            real.setEmail(contato.getEmail());

        if (contato.getTelefone() != null)
            real.setTelefone(contato.getTelefone());

        if (contato.getDataNascimento() != null)
            real.setDataNascimento(contato.getDataNascimento());

        if (contato.getEnderecos() != null) {

            for (Endereco novo : contato.getEnderecos()) {

                Endereco existente = real.getEnderecos().stream()
                        .filter(e -> e.getId().equals(novo.getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

                if (novo.getCep() != null) {
                    validarCep(novo.getCep()); //valida o cep
                    var viaCep = viaCepService.buscarPorCep(novo.getCep());


                    existente.setCep(novo.getCep());
                    existente.setRua(viaCep.logradouro());
                    existente.setBairro(viaCep.bairro());
                    existente.setCidade(viaCep.localidade());
                    existente.setUf(viaCep.uf());
                }

                if (novo.getNumero() != null)
                    existente.setNumero(novo.getNumero());

                if (repository.existsByEmail(contato.getEmail())) {
                    throw new InfosJaExistenteException("E-mail já cadastrado");
                }
                if (repository.existsByTelefone(contato.getTelefone())) {
                    throw new InfosJaExistenteException("Telefone já cadastrado");
                }
                validarData(contato.getDataNascimento());

            }
        }
    }

    @Transactional
    public void deletar(Long id) { // deleta o id mencionado
        repository.deleteById(id);
    }

    private void validarCep(String cep) {

        if (cep == null || cep.isBlank()) {
            throw new CepInvalidoException("O CEP é obrigatório");
        }
        //replace ajuda a retirar os "-" e dar um replacement ""
        String cepNumerico = cep.replace("-", "");

        //matches serve para identificar se a variavel passada tem um determinado numero de caracteres
        if (!cepNumerico.matches("\\d{8}")) {
            throw new CepInvalidoException("CEP invalido, o mesmo precisa conter 8 digitos.");
        }
    }

    private void validarData(LocalDate dataNascimento) {

        if (dataNascimento == null) {
            throw new DataInvalidaException("Data de nascimento é obrigatoria");
        }

        if (dataNascimento.isAfter(LocalDate.now())) {
            throw new DataInvalidaException("Data de nascimento não pode ser maior que o dia de hoje");
        }

        if (dataNascimento.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new DataInvalidaException("Data de nascimento inválida");
        }
    }

}
