package org.backend.controller;

import org.backend.model.Expense;
import org.backend.repository.ExpenseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository repo;

    public ExpenseController(ExpenseRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Expense> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Expense get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    public ResponseEntity<Expense> create(@RequestBody Expense e) { return ResponseEntity.ok(repo.save(e)); }

    @PutMapping("/{id}")
    public Expense update(@PathVariable Long id, @RequestBody Expense payload) {
        Expense e = repo.findById(id).orElseThrow();
        e.setDescription(payload.getDescription());
        e.setAmount(payload.getAmount());
        e.setDate(payload.getDate());
        e.setPaid(payload.isPaid());
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
}

