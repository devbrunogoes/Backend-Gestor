package org.backend.service;

import org.backend.model.Client;
import org.backend.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repo;

    public ClientService(ClientRepository repo) {
        this.repo = repo;
    }

    public List<Client> list() { return repo.findAll(); }

    public Client get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @Transactional
    public Client create(Client c) {
        if (c.getNome() == null || c.getNome().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome é obrigatório");
        if (c.getCpfCnpj() != null && !c.getCpfCnpj().isBlank() && repo.findByCpfCnpj(c.getCpfCnpj()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF/CNPJ já cadastrado");
        c.setId(null);
        // endereço já vem no payload; apenas salva
        return repo.save(c);
    }

    @Transactional
    public Client update(Long id, Client payload) {
        Client existing = get(id);
        if (payload.getNome() != null) existing.setNome(payload.getNome());
        if (payload.getCpfCnpj() != null) {
            if (!payload.getCpfCnpj().equals(existing.getCpfCnpj()) && repo.findByCpfCnpj(payload.getCpfCnpj()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF/CNPJ já cadastrado");
            }
            existing.setCpfCnpj(payload.getCpfCnpj());
        }
        existing.setTelefone(payload.getTelefone());
        existing.setEmail(payload.getEmail());
        existing.setActive(payload.isActive());
        // Atualiza endereço completo
        existing.setCep(payload.getCep());
        existing.setEndereco(payload.getEndereco());
        existing.setComplemento(payload.getComplemento());
        existing.setNumero(payload.getNumero());
        existing.setBairro(payload.getBairro());
        existing.setCidade(payload.getCidade());
        existing.setEstado(payload.getEstado());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        repo.deleteById(id);
    }
}
