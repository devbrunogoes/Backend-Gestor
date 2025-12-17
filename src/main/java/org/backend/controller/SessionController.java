package org.backend.controller;

import org.backend.model.UserSession;
import org.backend.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final AuthService authService;

    public SessionController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public List<UserSession> listActive() {
        return authService.listActiveSessions();
    }

    @PostMapping("/{sessionId}/terminate")
    public void terminate(@PathVariable Long sessionId) {
        authService.terminateSession(sessionId);
    }
}
