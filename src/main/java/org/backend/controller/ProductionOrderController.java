package org.backend.controller;

import org.backend.model.ProductionOrder;
import org.backend.repository.ProductionOrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/production-orders")
public class ProductionOrderController {

    private final ProductionOrderRepository repo;

    public ProductionOrderController(ProductionOrderRepository repo) { this.repo = repo; }

    @GetMapping
    public List<ProductionOrder> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ProductionOrder get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    public ResponseEntity<ProductionOrder> create(@RequestBody ProductionOrder o) {
        if (o.getDataInicio() == null) o.setDataInicio(LocalDateTime.now());
        if (o.getStatus() == null) o.setStatus("aberta");
        return ResponseEntity.ok(repo.save(o));
    }

    @PutMapping("/{id}")
    public ProductionOrder update(@PathVariable Long id, @RequestBody ProductionOrder payload) {
        ProductionOrder o = repo.findById(id).orElseThrow();
        o.setProduto(payload.getProduto());
        o.setQuantidade(payload.getQuantidade());
        o.setStatus(payload.getStatus());
        o.setDataInicio(payload.getDataInicio());
        o.setDataFim(payload.getDataFim());
        o.setObservacoes(payload.getObservacoes());
        return repo.save(o);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
}

