package org.backend.controller;

import org.backend.model.Receivable;
import org.backend.repository.ReceivableRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receivables")
public class ReceivableController {

    private final ReceivableRepository repo;

    public ReceivableController(ReceivableRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Receivable> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Receivable get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    public ResponseEntity<Receivable> create(@RequestBody Receivable r) { return ResponseEntity.ok(repo.save(r)); }

    @PutMapping("/{id}")
    public Receivable update(@PathVariable Long id, @RequestBody Receivable payload) {
        Receivable r = repo.findById(id).orElseThrow();
        r.setDescription(payload.getDescription());
        r.setAmount(payload.getAmount());
        r.setDueDate(payload.getDueDate());
        r.setPaid(payload.isPaid());
        return repo.save(r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
}

