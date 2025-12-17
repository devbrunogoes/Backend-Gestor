package org.backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonAlias;

@Entity
@Table(name = "delivery_points")
public class DeliveryPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonAlias({"nomePonto"})
    private String name; // nome do ponto

    // Endereço básico
    @JsonAlias({"cepPonto"})
    private String cep;

    @JsonAlias({"enderecoPonto"})
    private String address; // logradouro

    @JsonAlias({"numeroPonto"})
    private String number;  // número

    @JsonAlias({"bairroPonto"})
    private String neighborhood; // bairro

    @JsonAlias({"cidadePonto"})
    private String city;

    @JsonAlias({"ufPonto"})
    private String state; // UF

    @JsonAlias({"descricaoPonto"})
    private String description; // observação/descrição

    @JsonAlias({"ativo"})
    private boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
