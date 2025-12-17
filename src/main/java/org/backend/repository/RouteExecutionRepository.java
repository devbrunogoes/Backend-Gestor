package org.backend.repository;

import org.backend.model.RouteExecution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteExecutionRepository extends JpaRepository<RouteExecution, Long> {
}

