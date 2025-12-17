package org.backend.dto;

import java.util.List;
import java.math.BigDecimal;

public class SaleRequest {
    private Long clientId;
    private String clientName;
    private String origem;
    private Long execucaoId;
    private String paymentMethod;
    private String paymentStatus;
    private List<SaleItemRequest> items;
    private BigDecimal total;
    private String observacoes;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public Long getExecucaoId() { return execucaoId; }
    public void setExecucaoId(Long execucaoId) { this.execucaoId = execucaoId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public List<SaleItemRequest> getItems() { return items; }
    public void setItems(List<SaleItemRequest> items) { this.items = items; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}

