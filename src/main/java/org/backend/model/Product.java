package org.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // codigo identificador

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Supplier supplier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true) // permitir nulo durante migração; depois fixamos via DbFixer/SQL
    private ProductType type = ProductType.FABRICADO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true) // permitir nulo durante migração; depois fixamos via DbFixer/SQL
    private Unit unit = Unit.UN;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal cost = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal stockQuantity = BigDecimal.ZERO;

    // nível mínimo para alerta de estoque
    @Column(nullable = false)
    private BigDecimal minStock = BigDecimal.ZERO;

    private boolean active = true;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    @Column(name = "image_content_type")
    private String imageContentType;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public BigDecimal getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(BigDecimal stockQuantity) { this.stockQuantity = stockQuantity; }

    public BigDecimal getMinStock() { return minStock; }
    public void setMinStock(BigDecimal minStock) { this.minStock = minStock; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    public String getImageContentType() { return imageContentType; }
    public void setImageContentType(String imageContentType) { this.imageContentType = imageContentType; }
}
