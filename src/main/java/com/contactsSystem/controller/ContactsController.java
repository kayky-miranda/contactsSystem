package com.contactsSystem.controller;

import com.contactsSystem.domain.Contato;
import com.contactsSystem.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contatos")
public class ContactsController {

    @Autowired
    private ContatoService service;

    @GetMapping
    public List<Contato> listar(){
        return service.listar();
    }

    @GetMapping("/{id}")
    public Contato buscar(@PathVariable Long id){
        return service.buscarPorId(id);
    }

    @PostMapping
    public Contato cria(@RequestBody Contato contato){
        return service.salvar(contato);
    }

    @PutMapping("/{id}")
    public Contato atualizar(@PathVariable Long id, @RequestBody Contato contato){
        return service.atualizar(id, contato);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        service.deletar(id);
    }

}
