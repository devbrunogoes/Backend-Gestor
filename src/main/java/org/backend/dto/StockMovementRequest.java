package org.backend.dto;

import org.backend.model.MovementType;

import java.math.BigDecimal;

public class StockMovementRequest {
    private MovementType type;
    private BigDecimal quantity;
    private Long supplierId;
    private String note;

    public MovementType getType() { return type; }
    public void setType(MovementType type) { this.type = type; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}

