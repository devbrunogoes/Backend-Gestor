package org.backend.repository;

import org.backend.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByProduct_IdOrderByCreatedAtDesc(Long productId);
    List<StockMovement> findByProduct_Id(Long productId);
}

