package org.backend.controller;

import org.backend.model.*;
import org.backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService svc;

    public ProductController(ProductService svc) {
        this.svc = svc;
    }

    // --- Categories ---
    @GetMapping("/categories")
    public List<Category> listCategories() { return svc.listCategories(); }

    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable Long id) { return svc.getCategory(id); }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category c) {
        Category created = svc.createCategory(c);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/categories/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category c) { return svc.updateCategory(id, c); }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        svc.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // --- Suppliers ---
    @GetMapping("/suppliers")
    public List<Supplier> listSuppliers() { return svc.listSuppliers(); }

    @GetMapping("/suppliers/{id}")
    public Supplier getSupplier(@PathVariable Long id) { return svc.getSupplier(id); }

    @PostMapping("/suppliers")
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier s) {
        Supplier created = svc.createSupplier(s);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/suppliers/{id}")
    public Supplier updateSupplier(@PathVariable Long id, @RequestBody Supplier s) { return svc.updateSupplier(id, s); }

    @DeleteMapping("/suppliers/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        svc.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    // --- Products ---
    @GetMapping
    public List<Product> listProducts() { return svc.listProducts(); }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) { return svc.getProduct(id); }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product p) {
        Product created = svc.createProduct(p);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product p) { return svc.updateProduct(id, p); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        svc.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // --- Stock Movements ---
    @PostMapping("/{id}/movements")
    public ResponseEntity<StockMovement> createMovement(@PathVariable("id") Long productId, @RequestBody org.backend.dto.StockMovementRequest req) {
        StockMovement m = svc.createMovement(productId, req.getType(), req.getQuantity(), req.getSupplierId(), req.getNote());
        return ResponseEntity.ok(m);
    }

    @GetMapping("/{id}/movements")
    public List<StockMovement> listMovements(@PathVariable("id") Long productId) {
        return svc.listMovements(productId);
    }

    // --- Alerts / Reports ---
    @GetMapping("/low-stock")
    public List<Product> lowStock() { return svc.listLowStock(); }

    @GetMapping("/report")
    public List<Product> stockReport() { return svc.stockReport(); }

    @GetMapping("/{id}/history")
    public List<StockMovement> movementHistory(@PathVariable("id") Long productId) { return svc.movementHistory(productId); }
}
