package org.backend.service;

import org.backend.dto.RouteExecutionRequest;
import org.backend.dto.RouteLoadItemRequest;
import org.backend.model.MovementType;
import org.backend.model.Product;
import org.backend.model.Route;
import org.backend.model.RouteCheckpoint;
import org.backend.model.RouteExecution;
import org.backend.model.RouteLoadItem;
import org.backend.repository.RouteExecutionRepository;
import org.backend.repository.RouteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RouteExecutionService {

    private final RouteExecutionRepository repo;
    private final RouteRepository routeRepo;

    private final ProductService productService;

    public RouteExecutionService(RouteExecutionRepository repo, RouteRepository routeRepo, ProductService productService) {
        this.repo = repo;
        this.routeRepo = routeRepo;
        this.productService = productService;
    }

    private void ensureCheckpoints(RouteExecution e) {
        if (e == null) return;
        List<RouteCheckpoint> cps = e.getCheckpoints();
        if (cps != null && !cps.isEmpty()) return;
        if (e.getRouteId() == null) return;
        Route route = routeRepo.findById(e.getRouteId()).orElse(null);
        if (route == null) return;
        List<Long> ids = route.getClientes();
        if (ids == null || ids.isEmpty()) return;
        List<RouteCheckpoint> rebuilt = new ArrayList<>();
        for (Long id : ids) {
            RouteCheckpoint cp = new RouteCheckpoint();
            cp.setPid(id);
            cp.setChegada(null);
            cp.setSaida(null);
            cp.setEntregue(false);
            rebuilt.add(cp);
        }
        e.setCheckpoints(rebuilt);
        repo.save(e);
    }

    @Transactional
    public List<RouteExecution> list() {
        List<RouteExecution> all = repo.findAll();
        for (RouteExecution e : all) {
            ensureCheckpoints(e);
        }
        return all;
    }

    @Transactional
    public RouteExecution get(Long id) {
        RouteExecution e = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Execução não encontrada"));
        ensureCheckpoints(e);
        return e;
    }

    @Transactional
    public RouteExecution create(RouteExecution exec) {
        exec.setId(null);
        return repo.save(exec);
    }

    @Transactional
    public RouteExecution startFromRequest(RouteExecutionRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da execução são obrigatórios");
        }

        Long routeId = request.getRouteId();
        if (routeId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "routeId é obrigatório");
        }

        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rota não encontrada"));
        RouteExecution exec = new RouteExecution();
        exec.setRouteId(route.getId());
        exec.setRouteName(route.getName());
        exec.setDriver(request.getDriver());
        exec.setVehicle(request.getVehicle());

        String prevInicioIso = request.getPrevInicio();
        if (prevInicioIso != null) {
            try {
                exec.setPrevInicio(java.time.LocalDateTime.parse(prevInicioIso));
            } catch (DateTimeParseException ex) { /* ignore parse */ }
        }
        exec.setInicioReal(LocalDateTime.now());
        List<RouteCheckpoint> cps = new ArrayList<>();
        List<Long> customerIds = route.getClientes();
        if (customerIds != null) {
            for (Long id : customerIds) {
                RouteCheckpoint cp = new RouteCheckpoint();
                cp.setPid(id);
                cp.setChegada(null);
                cp.setSaida(null);
                cp.setEntregue(false);
                cps.add(cp);
            }
        }
        exec.setCheckpoints(cps);

        List<RouteLoadItem> loadItems = registerLoadManifest(route, request.getLoadItems());
        exec.setLoadItems(loadItems);
        return repo.save(exec);
    }

    @Transactional
    public RouteExecution finish(Long id) {
        RouteExecution e = get(id);
        e.setFimReal(LocalDateTime.now());
        return repo.save(e);
    }

    @Transactional
    public RouteExecution deliverAt(Long executionId, int checkpointIdx, String deliveryDataJson) {
        RouteExecution e = get(executionId);
        List<RouteCheckpoint> cps = e.getCheckpoints();
        if (checkpointIdx < 0 || checkpointIdx >= cps.size()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Checkpoint inválido");
        RouteCheckpoint cp = cps.get(checkpointIdx);
        cp.setChegada(cp.getChegada() == null ? LocalDateTime.now() : cp.getChegada());
        cp.setSaida(LocalDateTime.now());
        cp.setEntregue(true);
        cp.setDeliveryData(deliveryDataJson);
        e.setCheckpoints(cps);
        return repo.save(e);
    }

    @Transactional
    public RouteExecution markArrival(Long executionId, int checkpointIdx) {
        RouteExecution e = get(executionId);
        List<RouteCheckpoint> cps = e.getCheckpoints();
        if (checkpointIdx < 0 || checkpointIdx >= cps.size()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Checkpoint inválido");
        RouteCheckpoint cp = cps.get(checkpointIdx);
        if (cp.getChegada() == null) cp.setChegada(LocalDateTime.now());
        e.setCheckpoints(cps);
        return repo.save(e);
    }

    @Transactional
    public RouteExecution markDeparture(Long executionId, int checkpointIdx) {
        RouteExecution e = get(executionId);
        List<RouteCheckpoint> cps = e.getCheckpoints();
        if (checkpointIdx < 0 || checkpointIdx >= cps.size()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Checkpoint inválido");
        RouteCheckpoint cp = cps.get(checkpointIdx);
        cp.setSaida(LocalDateTime.now());
        e.setCheckpoints(cps);
        return repo.save(e);
    }

    private List<RouteLoadItem> registerLoadManifest(Route route, List<RouteLoadItemRequest> loadRequests) {
        if (loadRequests == null || loadRequests.isEmpty()) {
            return new ArrayList<>();
        }

        List<RouteLoadItem> loadItems = new ArrayList<>();
        for (RouteLoadItemRequest req : loadRequests) {
            if (req == null) continue;
            Long productId = req.getProductId();
            BigDecimal quantity = req.getQuantity();
            if (productId == null || quantity == null) continue;
            if (quantity.compareTo(BigDecimal.ZERO) <= 0) continue;

            Product product = productService.getProduct(productId);
            String note = buildRouteNote(route);
            productService.createMovement(productId, MovementType.SAIDA, quantity, null, note);

            RouteLoadItem item = new RouteLoadItem();
            item.setProductId(productId);
            item.setProductName(product.getName());
            item.setQuantity(quantity);
            loadItems.add(item);
        }

        return loadItems;
    }

    private String buildRouteNote(Route route) {
        if (route == null) {
            return "Separação para execução de rota";
        }
        String name = route.getName();
        if (name == null || name.isBlank()) {
            return "Separação para execução de rota";
        }
        return "Separação para rota " + name;
    }
}
