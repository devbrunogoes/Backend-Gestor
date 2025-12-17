package org.backend.dto;

import java.math.BigDecimal;

public class SaleItemRequest {
    private String nome;
    private Integer qtd;
    private BigDecimal preco;
    private BigDecimal desconto;
    private Boolean brinde;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getQtd() { return qtd; }
    public void setQtd(Integer qtd) { this.qtd = qtd; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    public Boolean getBrinde() { return brinde; }
    public void setBrinde(Boolean brinde) { this.brinde = brinde; }
}

