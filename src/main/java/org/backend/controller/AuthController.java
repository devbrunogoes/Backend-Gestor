package org.backend.controller;

import org.backend.dto.AuthResponse;
import org.backend.dto.LoginRequest;
import org.backend.model.User;
import org.backend.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            authService.logout(token);
        }
    }

    @GetMapping("/me")
    public User me(@AuthenticationPrincipal User user) {
        return user; // passwordHash é @JsonIgnore
    }
}
