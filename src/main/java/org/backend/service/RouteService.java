package org.backend.service;

import org.backend.model.Route;
import org.backend.repository.RouteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository repo;

    public RouteService(RouteRepository repo) {
        this.repo = repo;
    }

    public List<Route> list() {
        return repo.findAll();
    }

    public Route get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rota não encontrada"));
    }

    @Transactional
    public Route create(Route r) {
        if (r.getName() == null || r.getName().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome da rota é obrigatório");
        if (repo.findByNameIgnoreCase(r.getName()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Rota já existe");
        r.setId(null);
        return repo.save(r);
    }

    @Transactional
    public Route update(Long id, Route payload) {
        Route existing = get(id);
        if (payload.getName() != null && !payload.getName().equalsIgnoreCase(existing.getName())) {
            if (repo.findByNameIgnoreCase(payload.getName()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Rota já existe");
            existing.setName(payload.getName());
        }
        existing.setModelo(payload.isModelo());
        if (payload.getClientes() != null) existing.setClientes(payload.getClientes());
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rota não encontrada");
        repo.deleteById(id);
    }
}
