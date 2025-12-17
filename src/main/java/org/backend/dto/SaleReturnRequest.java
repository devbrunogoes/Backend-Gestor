package org.backend.dto;

public class SaleReturnRequest {
    private Integer itemIndex;
    private Integer qtd;
    private String motivo;

    public Integer getItemIndex() { return itemIndex; }
    public void setItemIndex(Integer itemIndex) { this.itemIndex = itemIndex; }
    public Integer getQtd() { return qtd; }
    public void setQtd(Integer qtd) { this.qtd = qtd; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}

