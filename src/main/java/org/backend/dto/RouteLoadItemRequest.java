package org.backend.dto;

import java.math.BigDecimal;

public class RouteLoadItemRequest {
    private Long productId;
    private BigDecimal quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
