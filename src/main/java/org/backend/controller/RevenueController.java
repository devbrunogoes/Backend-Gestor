package org.backend.controller;

import org.backend.model.Revenue;
import org.backend.repository.RevenueRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/revenues")
public class RevenueController {

    private final RevenueRepository repository;

    public RevenueController(RevenueRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Revenue> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Revenue get(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Revenue> create(@RequestBody Revenue payload) {
        return ResponseEntity.ok(repository.save(payload));
    }

    @PutMapping("/{id}")
    public Revenue update(@PathVariable Long id, @RequestBody Revenue payload) {
        Revenue revenue = repository.findById(id).orElseThrow();
        revenue.setDescription(payload.getDescription());
        revenue.setCategory(payload.getCategory());
        revenue.setAmount(payload.getAmount());
        revenue.setDate(payload.getDate());
        revenue.setReceived(payload.isReceived());
        return repository.save(revenue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}