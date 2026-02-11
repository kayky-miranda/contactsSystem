package com.contactsSystem.controller;

import com.contactsSystem.domain.Contato;
import com.contactsSystem.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contatos")
public class ContactsController {

    @Autowired
    private ContatoService service;

    @GetMapping
    public ResponseEntity<Contato> buscar(@RequestParam Long id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody Contato contato){
        service.salvar(contato);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> atualizar(@RequestParam Long id, @RequestBody Contato contato){
        service.atualizar(id, contato);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarPut(@RequestParam Long id, @RequestBody Contato contato){
        service.atualizarPut(id, contato);
        return ResponseEntity.ok().build();
    }



    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestParam Long id){

        service.deletar(id);
        return ResponseEntity.ok().build();
    }

}
