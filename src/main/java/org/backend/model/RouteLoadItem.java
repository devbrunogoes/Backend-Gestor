package org.backend.model;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class RouteLoadItem {

    private Long productId;
    private String productName;
    private BigDecimal quantity = BigDecimal.ZERO;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
