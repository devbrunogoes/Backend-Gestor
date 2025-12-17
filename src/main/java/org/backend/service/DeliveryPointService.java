package org.backend.service;

import org.backend.model.DeliveryPoint;
import org.backend.repository.DeliveryPointRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DeliveryPointService {

    private final DeliveryPointRepository repo;

    public DeliveryPointService(DeliveryPointRepository repo) {
        this.repo = repo;
    }

    public List<DeliveryPoint> list() {
        return repo.findAll();
    }

    public DeliveryPoint get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ponto de entrega não encontrado"));
    }

    public DeliveryPoint create(DeliveryPoint p) {
        if (p.getName() == null || p.getName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do ponto é obrigatório");
        if (repo.findByNameIgnoreCase(p.getName()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Ponto de entrega já existe");
        p.setId(null);
        return repo.save(p);
    }

    public DeliveryPoint update(Long id, DeliveryPoint payload) {
        DeliveryPoint existing = get(id);
        if (payload.getName() != null && !payload.getName().equalsIgnoreCase(existing.getName())) {
            if (repo.findByNameIgnoreCase(payload.getName()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Ponto de entrega já existe");
            existing.setName(payload.getName());
        }
        existing.setCep(payload.getCep());
        existing.setAddress(payload.getAddress());
        existing.setNumber(payload.getNumber());
        existing.setNeighborhood(payload.getNeighborhood());
        existing.setCity(payload.getCity());
        existing.setState(payload.getState());
        existing.setDescription(payload.getDescription());
        existing.setActive(payload.isActive());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ponto de entrega não encontrado");
        repo.deleteById(id);
    }

    // Novo: método para ativar/desativar um ponto de entrega
    public DeliveryPoint setActive(Long id, boolean active) {
        DeliveryPoint existing = get(id);
        existing.setActive(active);
        return repo.save(existing);
    }
}
