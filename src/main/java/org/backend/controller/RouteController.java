package org.backend.controller;

import org.backend.dto.RouteRequest;
import org.backend.model.Route;
import org.backend.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/routes","/api/rotas"})
public class RouteController {

    private final RouteService svc;

    public RouteController(RouteService svc) { this.svc = svc; }

    @GetMapping
    public List<Route> list() { return svc.list(); }

    @GetMapping("/{id}")
    public Route get(@PathVariable Long id) { return svc.get(id); }

    @PostMapping
    public ResponseEntity<Route> create(@RequestBody RouteRequest req) {
        Route r = toEntity(req);
        Route created = svc.create(r);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public Route update(@PathVariable Long id, @RequestBody RouteRequest req) {
        // merge com existente para evitar sobrescrever campos não enviados
        Route existing = svc.get(id);
        Route payload = new Route();
        payload.setName(req.getName() != null ? req.getName() : existing.getName());
        if (req.getModelo() != null) { payload.setModelo(req.getModelo()); } else { payload.setModelo(existing.isModelo()); }
        payload.setClientes(req.getClientes() != null ? req.getClientes() : existing.getClientes());
        return svc.update(id, payload);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Route toEntity(RouteRequest req) {
        Route r = new Route();
        r.setName(req.getName());
        if (req.getModelo() != null) r.setModelo(req.getModelo());
        if (req.getClientes() != null) r.setClientes(req.getClientes());
        return r;
    }
}
