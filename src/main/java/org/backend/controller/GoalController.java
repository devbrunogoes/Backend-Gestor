package org.backend.controller;

import org.backend.model.Goal;
import org.backend.repository.GoalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalRepository repo;

    public GoalController(GoalRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Goal> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Goal get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    public ResponseEntity<Goal> create(@RequestBody Goal g) { return ResponseEntity.ok(repo.save(g)); }

    @PutMapping("/{id}")
    public Goal update(@PathVariable Long id, @RequestBody Goal payload) {
        Goal g = repo.findById(id).orElseThrow();
        g.setTitle(payload.getTitle());
        g.setPeriod(payload.getPeriod());
        g.setType(payload.getType());
        g.setTarget(payload.getTarget());
        g.setActive(payload.isActive());
        return repo.save(g);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
}

