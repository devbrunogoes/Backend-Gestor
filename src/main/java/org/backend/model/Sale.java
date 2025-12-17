package org.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private Long clientId; // can be null for guest
    private String clientName; // for guest or display

    private String origem; // sistema or rota
    private Long execucaoId; // optional

    private String paymentMethod;
    private String paymentStatus;

    @ElementCollection
    @CollectionTable(name = "sale_items", joinColumns = @JoinColumn(name = "sale_id"))
    private List<SaleItem> items = new ArrayList<>();

    private BigDecimal total = BigDecimal.ZERO;

    private String observacoes;

    @ElementCollection
    @CollectionTable(name = "sale_returns", joinColumns = @JoinColumn(name = "sale_id"))
    private List<SaleReturn> returns = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

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

    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { this.items = items; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public List<SaleReturn> getReturns() { return returns; }
    public void setReturns(List<SaleReturn> returns) { this.returns = returns; }
}
