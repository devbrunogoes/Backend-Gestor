package org.backend.service;

import org.backend.dto.AuthResponse;
import org.backend.dto.LoginRequest;
import org.backend.model.User;
import org.backend.model.UserSession;
import org.backend.repository.UserRepository;
import org.backend.repository.UserSessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    // sessão expira em 8 horas por padrão
    private final Duration defaultTtl = Duration.ofHours(8);

    public AuthService(UserRepository userRepository, UserSessionRepository sessionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = Clock.systemUTC();
    }

    public String encodePassword(String raw) {
        return passwordEncoder.encode(raw);
    }

    public AuthResponse login(LoginRequest req) {
        if (req.getEmail() == null || req.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email e senha são obrigatórios");
        }
        User user = userRepository.findByEmailIgnoreCase(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário inativo");
        }
        String hash = user.getPasswordHash();
        if (hash == null || !passwordEncoder.matches(req.getPassword(), hash)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }
        Instant now = Instant.now(clock);
        String token = UUID.randomUUID().toString();
        UserSession s = new UserSession();
        s.setToken(token);
        s.setUser(user);
        s.setCreatedAt(now);
        s.setLastSeenAt(now);
        s.setExpiresAt(now.plus(defaultTtl));
        s.setActive(true);
        sessionRepository.save(s);
        String roleStr = user.getRole() != null ? user.getRole().name() : null;
        return new AuthResponse(token, user.getId(), user.getNome(), user.getEmail(), roleStr, s.getExpiresAt());
    }

    public void logout(String token) {
        sessionRepository.findByToken(token).ifPresent(session -> {
            session.setActive(false);
            sessionRepository.save(session);
        });
    }

    public List<UserSession> listActiveSessions() {
        return sessionRepository.findByActiveTrue();
    }

    public void terminateSession(Long sessionId) {
        UserSession s = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão não encontrada"));
        s.setActive(false);
        sessionRepository.save(s);
    }
}
