package com.contactsSystem.controller;

import com.contactsSystem.domain.Contato;
import com.contactsSystem.service.ContatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contatos")
@Tag(name = "Contatos", description = "Gerenciamento de contatos")
public class ContactsController {


    @Autowired
    private ContatoService service;

    @Operation(summary = "Listar todos os contatos")
    @GetMapping
    public ResponseEntity<Contato> buscar(@RequestParam Long id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Criar novo contato")
    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody Contato contato){
        service.salvar(contato);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Editar todas as informações do contato")
    @PatchMapping
    public ResponseEntity<Void> atualizar(@RequestParam Long id, @RequestBody Contato contato){
        service.atualizar(id, contato);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Editar campo expecífico do  contato")
    @PutMapping
    public ResponseEntity<Void> atualizarPut(@RequestParam Long id, @RequestBody Contato contato){
        service.atualizarPut(id, contato);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Deletar contato")
    @DeleteMapping
    public ResponseEntity<Void> deletar(@RequestParam Long id){

        service.deletar(id);
        return ResponseEntity.ok().build();
    }

}
