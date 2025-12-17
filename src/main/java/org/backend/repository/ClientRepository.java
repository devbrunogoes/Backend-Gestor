package org.backend.repository;

import org.backend.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByNomeIgnoreCase(String nome);
    Optional<Client> findByCpfCnpj(String cpfCnpj);
}

