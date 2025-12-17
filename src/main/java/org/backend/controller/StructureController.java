package org.backend.controller;

import org.backend.model.Structure;
import org.backend.repository.StructureRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/structures")
public class StructureController {

    private final StructureRepository repo;

    public StructureController(StructureRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Structure> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Structure get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    public ResponseEntity<Structure> create(@RequestBody Structure s) { return ResponseEntity.ok(repo.save(s)); }

    @PutMapping("/{id}")
    public Structure update(@PathVariable Long id, @RequestBody Structure payload) {
        Structure s = repo.findById(id).orElseThrow();
        s.setNome(payload.getNome());
        s.setDescricao(payload.getDescricao());
        s.setAtivo(payload.isAtivo());
        return repo.save(s);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
}

