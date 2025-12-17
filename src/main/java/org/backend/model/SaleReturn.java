package org.backend.model;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class SaleReturn {
    private Integer itemIndex;
    private Integer qtd;
    private String motivo;
    private LocalDateTime data;

    public Integer getItemIndex() { return itemIndex; }
    public void setItemIndex(Integer itemIndex) { this.itemIndex = itemIndex; }
    public Integer getQtd() { return qtd; }
    public void setQtd(Integer qtd) { this.qtd = qtd; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }
}
