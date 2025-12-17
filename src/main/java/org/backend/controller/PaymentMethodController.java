package org.backend.controller;

import org.backend.model.PaymentMethod;
import org.backend.repository.PaymentMethodRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodRepository repo;

    public PaymentMethodController(PaymentMethodRepository repo) { this.repo = repo; }

    @GetMapping
    public List<PaymentMethod> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public PaymentMethod get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    public ResponseEntity<PaymentMethod> create(@RequestBody PaymentMethod e) { return ResponseEntity.ok(repo.save(e)); }

    @PutMapping("/{id}")
    public PaymentMethod update(@PathVariable Long id, @RequestBody PaymentMethod payload) {
        PaymentMethod e = repo.findById(id).orElseThrow();
        e.setName(payload.getName());
        e.setType(payload.getType());
        e.setFee(payload.getFee());
        e.setActive(payload.isActive());
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
}

