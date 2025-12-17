package org.backend.dto;

import java.time.Instant;

public class AuthResponse {
    private String token;
    private Long userId;
    private String nome;
    private String email;
    private String role;
    private Instant expiresAt;

    public AuthResponse(String token, Long userId, String nome, String email, String role, Instant expiresAt) {
        this.token = token;
        this.userId = userId;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.expiresAt = expiresAt;
    }

    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Instant getExpiresAt() { return expiresAt; }
}
