package com.ecocity.controller;

import com.ecocity.model.LixeiraInteligente;
import com.ecocity.repository.LixeiraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lixeiras")
public class LixeiraController {

    private final LixeiraRepository repository;

    public LixeiraController(LixeiraRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<LixeiraInteligente> listarTodas() {
        return repository.findAll();
    }

    @GetMapping("/urgentes")
    public List<LixeiraInteligente> listarUrgentes() {
        return repository.findByOcupacaoAtualPctGreaterThanEqual(80);
    }

    @PostMapping
    public ResponseEntity<LixeiraInteligente> criar(@RequestBody LixeiraInteligente lixeira) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(lixeira));
    }
}
