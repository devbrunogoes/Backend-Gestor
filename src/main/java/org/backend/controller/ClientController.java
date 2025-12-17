package org.backend.controller;

import org.backend.dto.ClientRequest;
import org.backend.model.Client;
import org.backend.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/clients","/api/clientes"})
public class ClientController {

    private final ClientService svc;

    public ClientController(ClientService svc) { this.svc = svc; }

    @GetMapping
    public List<Client> list() { return svc.list(); }

    @GetMapping("/{id}")
    public Client get(@PathVariable Long id) { return svc.get(id); }

    @PostMapping
    public ResponseEntity<Client> create(@RequestBody ClientRequest req) {
        Client c = toEntity(req);
        return ResponseEntity.ok(svc.create(c));
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable Long id, @RequestBody ClientRequest req) {
        Client payload = toEntity(req);
        payload.setId(id);
        return svc.update(id, payload);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Client toEntity(ClientRequest req) {
        Client c = new Client();
        c.setNome(req.getNome());
        c.setCpfCnpj(req.getCpfCnpj());
        c.setTelefone(req.getTelefone());
        c.setEmail(req.getEmail());
        c.setCep(req.getCep());
        c.setEndereco(req.getEndereco());
        c.setComplemento(req.getComplemento());
        c.setNumero(req.getNumero());
        c.setBairro(req.getBairro());
        c.setCidade(req.getCidade());
        c.setEstado(req.getEstado());
        if (req.getActive() != null) c.setActive(req.getActive());
        return c;
    }
}
