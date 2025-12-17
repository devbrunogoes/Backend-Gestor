package org.backend.repository;

import org.backend.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByToken(String token);
    List<UserSession> findByActiveTrue();
    List<UserSession> findByUser_IdAndActiveTrue(Long userId);
}

