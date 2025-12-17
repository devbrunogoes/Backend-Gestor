package org.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "route_executions")
public class RouteExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long routeId;
    private String routeName;
    private String driver;
    private String vehicle;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime prevInicio;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime inicioReal;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fimReal;

    @ElementCollection
    @CollectionTable(name = "route_execution_checkpoints", joinColumns = @JoinColumn(name = "execution_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "chegada", column = @Column(name = "chegada")),
        @AttributeOverride(name = "deliveryData", column = @Column(name = "z_delivery_data", columnDefinition = "text")),
        @AttributeOverride(name = "entregue", column = @Column(name = "entregue")),
        @AttributeOverride(name = "pid", column = @Column(name = "pid")),
        @AttributeOverride(name = "saida", column = @Column(name = "saida"))
    })
    private List<RouteCheckpoint> checkpoints = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "route_execution_loads", joinColumns = @JoinColumn(name = "execution_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "productId", column = @Column(name = "product_id")),
        @AttributeOverride(name = "productName", column = @Column(name = "product_name")),
        @AttributeOverride(name = "quantity", column = @Column(name = "quantity"))
    })
    private List<RouteLoadItem> loadItems = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getDriver() { return driver; }
    public void setDriver(String driver) { this.driver = driver; }

    public String getVehicle() { return vehicle; }
    public void setVehicle(String vehicle) { this.vehicle = vehicle; }

    public LocalDateTime getPrevInicio() { return prevInicio; }
    public void setPrevInicio(LocalDateTime prevInicio) { this.prevInicio = prevInicio; }

    public LocalDateTime getInicioReal() { return inicioReal; }
    public void setInicioReal(LocalDateTime inicioReal) { this.inicioReal = inicioReal; }

    public LocalDateTime getFimReal() { return fimReal; }
    public void setFimReal(LocalDateTime fimReal) { this.fimReal = fimReal; }

    public List<RouteCheckpoint> getCheckpoints() { return checkpoints; }
    public void setCheckpoints(List<RouteCheckpoint> checkpoints) { this.checkpoints = checkpoints; }

    public List<RouteLoadItem> getLoadItems() { return loadItems; }
    public void setLoadItems(List<RouteLoadItem> loadItems) { this.loadItems = loadItems; }
}
