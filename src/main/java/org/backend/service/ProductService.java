package org.backend.service;

import org.backend.model.*;
import org.backend.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository movementRepository;

    public ProductService(CategoryRepository categoryRepository, SupplierRepository supplierRepository, ProductRepository productRepository, StockMovementRepository movementRepository) {
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
    }

    // --- Categories ---
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    public Category createCategory(Category c) {
        if (c.getName() == null || c.getName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome da categoria é obrigatório");
        if (categoryRepository.findByNameIgnoreCase(c.getName()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Categoria já existe");
        c.setId(null);
        return categoryRepository.save(c);
    }

    public Category updateCategory(Long id, Category payload) {
        Category existing = getCategory(id);
        if (payload.getName() != null && !payload.getName().equalsIgnoreCase(existing.getName())) {
            if (categoryRepository.findByNameIgnoreCase(payload.getName()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Categoria já existe");
            existing.setName(payload.getName());
        }
        existing.setDescription(payload.getDescription());
        existing.setActive(payload.isActive());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada");
        categoryRepository.deleteById(id);
    }

    // --- Suppliers ---
    public List<Supplier> listSuppliers() { return supplierRepository.findAll(); }

    public Supplier getSupplier(Long id) { return supplierRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado")); }

    public Supplier createSupplier(Supplier s) {
        if (s.getName() == null || s.getName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do fornecedor é obrigatório");
        if (supplierRepository.findByNameIgnoreCase(s.getName()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Fornecedor já existe");
        s.setId(null);
        // ensure optional defaults
        if (s.getCnpj() == null) s.setCnpj(null);
        return supplierRepository.save(s);
    }

    public Supplier updateSupplier(Long id, Supplier payload) {
        Supplier existing = getSupplier(id);
        if (payload.getName() != null && !payload.getName().equalsIgnoreCase(existing.getName())) {
            if (supplierRepository.findByNameIgnoreCase(payload.getName()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Fornecedor já existe");
            existing.setName(payload.getName());
        }
        existing.setContact(payload.getContact());
        existing.setEmail(payload.getEmail());
        existing.setPhone(payload.getPhone());
        existing.setCnpj(payload.getCnpj());
        existing.setActive(payload.isActive());
        return supplierRepository.save(existing);
    }

    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado");
        supplierRepository.deleteById(id);
    }

    // --- Products ---
    public List<Product> listProducts() { return productRepository.findAll(); }

    public Page<Product> listProductsPaged(int page, int size, String q) {
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 200));
        if (q != null && !q.isBlank()) {
            String term = q.trim();
            return productRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(term, term, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public Product getProduct(Long id) { return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado")); }

    public Product createProduct(Product p) {
        if (p.getCode() == null || p.getCode().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código do produto é obrigatório");
        if (productRepository.findByCodeIgnoreCase(p.getCode()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Código já existe");
        p.setId(null);
        if (p.getStockQuantity() == null) p.setStockQuantity(BigDecimal.ZERO);
        if (p.getMinStock() == null) p.setMinStock(BigDecimal.ZERO);
        // defaults for factory/commercial context
        if (p.getType() == null) p.setType(ProductType.FABRICADO);
        if (p.getUnit() == null) p.setUnit(Unit.UN);
        if (p.getPrice() == null) p.setPrice(BigDecimal.ZERO);
        if (p.getCost() == null) p.setCost(BigDecimal.ZERO);
        normalizeImage(p);
        return productRepository.save(p);
    }

    public Product updateProduct(Long id, Product payload) {
        Product existing = getProduct(id);
        if (payload.getCode() != null && !payload.getCode().equalsIgnoreCase(existing.getCode())) {
            if (productRepository.findByCodeIgnoreCase(payload.getCode()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Código já existe");
            existing.setCode(payload.getCode());
        }
        if (payload.getName() != null) existing.setName(payload.getName());
        existing.setDescription(payload.getDescription());
        existing.setCategory(payload.getCategory());
        existing.setSupplier(payload.getSupplier());
        if (payload.getType() != null) existing.setType(payload.getType());
        if (payload.getUnit() != null) existing.setUnit(payload.getUnit());
        if (payload.getPrice() != null) existing.setPrice(payload.getPrice());
        if (payload.getCost() != null) existing.setCost(payload.getCost());
        if (payload.getStockQuantity() != null) existing.setStockQuantity(payload.getStockQuantity());
        if (payload.getMinStock() != null) existing.setMinStock(payload.getMinStock());
        existing.setActive(payload.isActive());
        existing.setImageData(payload.getImageData());
        existing.setImageContentType(payload.getImageContentType());
        normalizeImage(existing);
        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        productRepository.deleteById(id);
    }

    // --- Stock Movements ---
    @Transactional
    public StockMovement createMovement(Long productId, MovementType type, BigDecimal quantity, Long supplierId, String note) {
        if (quantity == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade é obrigatória");
        Product p = getProduct(productId);
        BigDecimal newQty = p.getStockQuantity();

        switch (type) {
            case ENTRADA, DEVOLUCAO -> {
                if (quantity.compareTo(BigDecimal.ZERO) <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade deve ser positiva para ENTRADA/DEVOLUCAO");
                newQty = newQty.add(quantity);
            }
            case SAIDA -> {
                if (quantity.compareTo(BigDecimal.ZERO) <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade deve ser positiva para SAIDA");
                if (newQty.subtract(quantity).compareTo(BigDecimal.ZERO) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque insuficiente");
                }
                newQty = newQty.subtract(quantity);
            }
            case AJUSTE -> {
                // ajuste aceita quantidade positiva (aumenta) ou negativa (reduz)
                newQty = newQty.add(quantity);
                if (newQty.compareTo(BigDecimal.ZERO) < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ajuste resultaria em estoque negativo");
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de movimentação inválido");
        }

        p.setStockQuantity(newQty);
        productRepository.save(p);

        StockMovement m = new StockMovement();
        m.setProduct(p);
        m.setType(type);
        m.setQuantity(quantity);
        if (supplierId != null) {
            Supplier s = supplierRepository.findById(supplierId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fornecedor não encontrado"));
            m.setSupplier(s);
        }
        m.setNote(note);
        m.setCreatedAt(Instant.now());
        return movementRepository.save(m);
    }

    public List<StockMovement> listMovements(Long productId) {
        return movementRepository.findByProduct_IdOrderByCreatedAtDesc(productId);
    }

    // --- Alerts / Reports ---
    public List<Product> listLowStock() {
        return productRepository.findAll().stream()
                .filter(p -> p.getMinStock() != null && p.getStockQuantity().compareTo(p.getMinStock()) <= 0)
                .collect(Collectors.toList());
    }

    public List<Product> stockReport() {
        return productRepository.findAll();
    }

    public List<StockMovement> movementHistory(Long productId) {
        return movementRepository.findByProduct_IdOrderByCreatedAtDesc(productId);
    }

    private void normalizeImage(Product product) {
        if (product == null) return;
        if (product.getImageData() != null && product.getImageData().length == 0) {
            product.setImageData(null);
        }
        if (product.getImageContentType() != null && product.getImageContentType().isBlank()) {
            product.setImageContentType(null);
        }
        if (product.getImageData() == null) {
            product.setImageContentType(null);
        }
    }
}
