package org.backend.dto;

import java.util.List;

public class RouteExecutionRequest {
    private Long routeId;
    private String routeName;
    private String driver;
    private String vehicle;
    private String prevInicio; // ISO datetime string
    private List<Long> checkpoints; // list of delivery point ids
    private List<RouteLoadItemRequest> loadItems;

    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getDriver() { return driver; }
    public void setDriver(String driver) { this.driver = driver; }

    public String getVehicle() { return vehicle; }
    public void setVehicle(String vehicle) { this.vehicle = vehicle; }

    public String getPrevInicio() { return prevInicio; }
    public void setPrevInicio(String prevInicio) { this.prevInicio = prevInicio; }

    public List<Long> getCheckpoints() { return checkpoints; }
    public void setCheckpoints(List<Long> checkpoints) { this.checkpoints = checkpoints; }

    public List<RouteLoadItemRequest> getLoadItems() { return loadItems; }
    public void setLoadItems(List<RouteLoadItemRequest> loadItems) { this.loadItems = loadItems; }
}

