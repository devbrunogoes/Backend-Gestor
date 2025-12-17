package org.backend.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import java.time.LocalDateTime;

@Embeddable
public class RouteCheckpoint {
    @Column(name = "chegada")
    private LocalDateTime chegada;

    @Lob
    @Column(name = "z_delivery_data", columnDefinition = "text")
    private String deliveryData; // JSON string com itens/obs se necessário

    @Column(name = "entregue")
    private boolean entregue = false;

    @Column(name = "pid")
    private Long pid; // delivery point id

    @Column(name = "saida")
    private LocalDateTime saida;

    public Long getPid() { return pid; }
    public void setPid(Long pid) { this.pid = pid; }

    public LocalDateTime getChegada() { return chegada; }
    public void setChegada(LocalDateTime chegada) { this.chegada = chegada; }

    public LocalDateTime getSaida() { return saida; }
    public void setSaida(LocalDateTime saida) { this.saida = saida; }

    public boolean isEntregue() { return entregue; }
    public void setEntregue(boolean entregue) { this.entregue = entregue; }

    public String getDeliveryData() { return deliveryData; }
    public void setDeliveryData(String deliveryData) { this.deliveryData = deliveryData; }
}
