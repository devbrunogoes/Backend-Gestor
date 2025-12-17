package org.backend.controller;

import org.backend.dto.RouteExecutionRequest;
import org.backend.model.RouteExecution;
import org.backend.service.RouteExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/execucoes","/api/route-executions","/api/executions"})
public class RouteExecutionController {

    private final RouteExecutionService svc;

    public RouteExecutionController(RouteExecutionService svc) { this.svc = svc; }

    @GetMapping
    public List<RouteExecution> list() { return svc.list(); }

    @GetMapping("/{id}")
    public RouteExecution get(@PathVariable Long id) { return svc.get(id); }

    @PostMapping
    public ResponseEntity<RouteExecution> create(@RequestBody RouteExecutionRequest req) {
        RouteExecution created = svc.startFromRequest(req);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/finish")
    public RouteExecution finish(@PathVariable Long id) { return svc.finish(id); }

    @PostMapping("/{id}/arrival/{idx}")
    public RouteExecution arrival(@PathVariable Long id, @PathVariable int idx) { return svc.markArrival(id, idx); }

    @PostMapping("/{id}/departure/{idx}")
    public RouteExecution departure(@PathVariable Long id, @PathVariable int idx) { return svc.markDeparture(id, idx); }

    @PostMapping("/{id}/deliver/{idx}")
    public RouteExecution deliver(@PathVariable Long id, @PathVariable int idx, @RequestBody(required = false) String deliveryData) {
        return svc.deliverAt(id, idx, deliveryData);
    }
}

