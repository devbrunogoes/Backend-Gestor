package org.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class DeliveryPointRequest {
    @NotBlank(message = "Nome do ponto é obrigatório")
    private String name;

    private String cep;
    private String address;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String description;
    private Boolean active;

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

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}

