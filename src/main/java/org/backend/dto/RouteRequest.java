package org.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RouteRequest {
    private String name;

    private Boolean modelo;

    @JsonProperty("clientes")
    @JsonAlias({"pontos"})
    private List<Long> clientes;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getModelo() { return modelo; }
    public void setModelo(Boolean modelo) { this.modelo = modelo; }

    public List<Long> getClientes() { return clientes; }
    public void setClientes(List<Long> clientes) { this.clientes = clientes; }

    // Backward-compat
    @Deprecated
    public List<Long> getPontos() { return clientes; }
    @Deprecated
    public void setPontos(List<Long> pontos) { this.clientes = pontos; }
}
