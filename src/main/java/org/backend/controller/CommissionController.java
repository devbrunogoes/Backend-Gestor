package org.backend.controller;

import org.backend.model.Commission;
import org.backend.repository.CommissionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commissions")
public class CommissionController {

    private final CommissionRepository repo;

    public CommissionController(CommissionRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Commission> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Commission get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    public ResponseEntity<Commission> create(@RequestBody Commission c) { return ResponseEntity.ok(repo.save(c)); }

    @PutMapping("/{id}")
    public Commission update(@PathVariable Long id, @RequestBody Commission payload) {
        Commission c = repo.findById(id).orElseThrow();
        c.setDescription(payload.getDescription());
        c.setType(payload.getType());
        c.setValue(payload.getValue());
        c.setActive(payload.isActive());
        return repo.save(c);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
}

