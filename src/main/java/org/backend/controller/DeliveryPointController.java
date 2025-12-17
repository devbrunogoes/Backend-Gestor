package org.backend.controller;

import org.backend.model.DeliveryPoint;
import org.backend.service.DeliveryPointService;
import org.backend.dto.DeliveryPointRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/delivery-points")
public class DeliveryPointController {

    // OBS: Esta entidade está em descontinuação; rotas agora associam diretamente clientes.

    private final DeliveryPointService svc;

    public DeliveryPointController(DeliveryPointService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<DeliveryPoint> list() { return svc.list(); }

    @GetMapping("/{id}")
    public DeliveryPoint get(@PathVariable Long id) { return svc.get(id); }

    @PostMapping
    public ResponseEntity<DeliveryPoint> create(@Valid @RequestBody DeliveryPointRequest req) {
        DeliveryPoint p = toEntity(req);
        DeliveryPoint created = svc.create(p);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public DeliveryPoint update(@PathVariable Long id, @Valid @RequestBody DeliveryPointRequest req) {
        DeliveryPoint p = toEntity(req);
        return svc.update(id, p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Novo endpoint para ativar/desativar rapidamente
    @PatchMapping("/{id}/active")
    public DeliveryPoint setActive(@PathVariable Long id, @RequestParam boolean active) {
        return svc.setActive(id, active);
    }

    private DeliveryPoint toEntity(DeliveryPointRequest req) {
        DeliveryPoint p = new DeliveryPoint();
        p.setName(req.getName());
        p.setCep(req.getCep());
        p.setAddress(req.getAddress());
        p.setNumber(req.getNumber());
        p.setNeighborhood(req.getNeighborhood());
        p.setCity(req.getCity());
        p.setState(req.getState());
        p.setDescription(req.getDescription());
        if (req.getActive() != null) p.setActive(req.getActive());
        return p;
    }
}
