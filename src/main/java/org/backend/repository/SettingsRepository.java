package org.backend.repository;

import java.util.Optional;
import org.backend.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Optional<Settings> findTopByOrderByIdAsc();
}

