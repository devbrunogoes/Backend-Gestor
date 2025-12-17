package org.backend.repository;

import org.backend.model.DeliveryPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryPointRepository extends JpaRepository<DeliveryPoint, Long> {
    Optional<DeliveryPoint> findByNameIgnoreCase(String name);
}

