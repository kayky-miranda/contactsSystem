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
    // injeção do viaCep em contatos
    private final ViaCepService viaCepService;

    public ContatoService(ContatosRepository repository, ViaCepService viaCepService) {
        this.repository = repository;
        this.viaCepService = viaCepService;
    }

    public List<Contato> listar(){ // metodo listar contatos salvos
        return repository.findAll();
    }

    // busca um contato salvo pelo id
    public Contato buscarPorId(Long id){

        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Contato não encontrado")
        );
    }


    @Transactional
    public void salvar(Contato contato) {


        // realiza o preenchimento dos campos endereço pelo CEP
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
        // evita adicionar ID de sub bancos de dados (endereco) no JSON
        if (contato.getEnderecos() != null) {
            contato.getEnderecos().forEach(e -> {
                e.setId(null);
                e.setContato(contato);
            });
        }

        // trata a exception de informações existentes
        if (repository.existsByEmail(contato.getEmail())) {
            throw new InfosJaExistenteException("E-mail já cadastrado");
        }
        if (repository.existsByTelefone(contato.getTelefone())) {
            throw new InfosJaExistenteException("Telefone já cadastrado");
        }

        // valida a data
        validarData(contato.getDataNascimento());
        repository.saveAndFlush(contato);
    }

    @Transactional
    public void atualizarPut(Long id, Contato contato) {

        Contato real = buscarPorId(id);

        real.setNome(contato.getNome());
        real.setEmail(contato.getEmail());
        real.setTelefone(contato.getTelefone());
        real.setDataNascimento(contato.getDataNascimento());

        if (repository.existsByEmailAndIdNot(contato.getEmail(), id)) {
            throw new InfosJaExistenteException("E-mail já cadastrado");
        }

        if (repository.existsByTelefoneAndIdNot(contato.getTelefone(), id)) {
            throw new InfosJaExistenteException("Telefone já cadastrado");
        }

        validarData(contato.getDataNascimento());

        if (contato.getEnderecos() != null) {

            for (Endereco novo : contato.getEnderecos()) {

                Endereco existente = real.getEnderecos().stream()
                        .filter(e -> e.getId().equals(novo.getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

                if (novo.getCep() != null) {
                    validarCep(novo.getCep());
                    var viaCep = viaCepService.buscarPorCep(novo.getCep());

                    existente.setCep(novo.getCep());
                    existente.setRua(viaCep.logradouro());
                    existente.setBairro(viaCep.bairro());
                    existente.setCidade(viaCep.localidade());
                    existente.setUf(viaCep.uf());
                }

                if (novo.getNumero() != null) {
                    existente.setNumero(novo.getNumero());
                }
            }
        }

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

        // realiza o preenchimento dos campos endereço pelo CEP
        if (contato.getEnderecos() != null) {

            for (Endereco novo : contato.getEnderecos()) {

                Endereco existente = real.getEnderecos().stream()
                        .filter(e -> e.getId().equals(novo.getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

                if (novo.getCep() != null) {
                    validarCep(novo.getCep()); //valida o cep
                    var viaCep = viaCepService.buscarPorCep(novo.getCep());

                    if (novo.getNumero() != null)
                        existente.setNumero(novo.getNumero());

                    // verifica se existe o valor duplicado no db porem nao consideraa o id atual
                    if (repository.existsByEmailAndIdNot(contato.getEmail(), id)) {
                        throw new InfosJaExistenteException("E-mail já cadastrado");
                    }
                    if (repository.existsByTelefoneAndIdNot(contato.getTelefone(),id)) {
                        throw new InfosJaExistenteException("Telefone já cadastrado");
                    }
                    validarData(contato.getDataNascimento());
                    existente.setCep(novo.getCep());
                    existente.setRua(viaCep.logradouro());
                    existente.setBairro(viaCep.bairro());
                    existente.setCidade(viaCep.localidade());
                    existente.setUf(viaCep.uf());
                }



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

        // valida se o campo está preenchido
        if (dataNascimento == null) {
            throw new DataInvalidaException("Data de nascimento é obrigatoria");
        }

        // valida se a data de nascimento é maior que o dia de hoje
        if (dataNascimento.isAfter(LocalDate.now())) {
            throw new DataInvalidaException("Data de nascimento não pode ser maior que o dia de hoje");
        }

        // valida se a data preenchida está conforme
        if (dataNascimento.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new DataInvalidaException("Data de nascimento inválida");
        }
    }

}
