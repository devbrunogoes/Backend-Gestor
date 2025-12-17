package org.backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonAlias({"nome", "nomeRota", "name"})
    private String name;

    private boolean modelo = false;

    // lista de ids de clientes (antes: pontos). Mantemos a tabela/coluna para compatibilidade.
    @ElementCollection
    @CollectionTable(name = "route_points", joinColumns = @JoinColumn(name = "route_id"))
    @Column(name = "point_id")
    @JsonProperty("clientes")
    @JsonAlias({"pontos"})
    private List<Long> clientes = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isModelo() { return modelo; }
    public void setModelo(boolean modelo) { this.modelo = modelo; }

    // API preferencial
    public List<Long> getClientes() { return clientes; }
    public void setClientes(List<Long> clientes) { this.clientes = clientes; }

    // Backward-compat helpers (aceitar/levar o nome antigo em código legado)
    @Deprecated
    public List<Long> getPontos() { return clientes; }
    @Deprecated
    public void setPontos(List<Long> pontos) { this.clientes = pontos; }
}
