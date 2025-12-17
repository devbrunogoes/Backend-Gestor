package org.backend.model;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class SaleItem {
    private String nome;
    private Integer qtd;
    private BigDecimal preco;
    private BigDecimal desconto;
    private boolean brinde;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getQtd() { return qtd; }
    public void setQtd(Integer qtd) { this.qtd = qtd; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    public boolean isBrinde() { return brinde; }
    public void setBrinde(boolean brinde) { this.brinde = brinde; }
}

