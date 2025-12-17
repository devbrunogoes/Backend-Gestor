package org.backend.controller;

import org.backend.dto.SaleRequest;
import org.backend.dto.SaleReturnRequest;
import org.backend.model.Sale;
import org.backend.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/sales","/api/vendas"})
public class SaleController {

    private final SaleService svc;

    public SaleController(SaleService svc) { this.svc = svc; }

    @GetMapping
    public List<Sale> list() { return svc.list(); }

    @GetMapping("/{id}")
    public Sale get(@PathVariable Long id) { return svc.get(id); }

    @PostMapping
    public ResponseEntity<Sale> create(@RequestBody SaleRequest req) {
        Sale created = svc.createFromRequest(req);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/returns")
    public ResponseEntity<Sale> registerReturn(@PathVariable Long id, @RequestBody SaleReturnRequest req) {
        return ResponseEntity.ok(svc.registerReturn(id, req));
    }
}
