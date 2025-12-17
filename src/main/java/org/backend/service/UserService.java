package org.backend.service;

import org.backend.model.Permission;
import org.backend.model.Role;
import org.backend.model.User;
import org.backend.model.UserSession;
import org.backend.repository.UserRepository;
import org.backend.repository.UserSessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;
    private final AuthService authService;

    public UserService(UserRepository userRepository, UserSessionRepository sessionRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.authService = authService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public User create(User payload) {
        if (payload.getEmail() == null || payload.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email é obrigatório");
        }
        if (userRepository.findByEmailIgnoreCase(payload.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }
        if (payload.getPassword() == null || payload.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória");
        }
        sanitizeAndApplyRules(payload);
        payload.setPasswordHash(authService.encodePassword(payload.getPassword()));
        payload.setPassword(null);
        payload.setId(null); // garantir criação
        return userRepository.save(payload);
    }

    public User update(Long id, User payload) {
        User existing = findById(id);
        if (payload.getEmail() != null && !payload.getEmail().equalsIgnoreCase(existing.getEmail())) {
            if (userRepository.findByEmailIgnoreCase(payload.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
            }
            existing.setEmail(payload.getEmail());
        }
        existing.setNome(payload.getNome());
        existing.setRole(payload.getRole());
        existing.setPermissions(payload.getPermissions());
        if (payload.getPassword() != null && !payload.getPassword().isBlank()) {
            existing.setPasswordHash(authService.encodePassword(payload.getPassword()));
        }
        sanitizeAndApplyRules(existing);
        existing.setPassword(null);
        return userRepository.save(existing);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    public User updateActive(Long id, boolean active) {
        User u = findById(id);
        u.setActive(active);
        User saved = userRepository.save(u);
        if (!active) {
            // desativar sessões do usuário
            for (UserSession s : sessionRepository.findByUser_IdAndActiveTrue(id)) {
                s.setActive(false);
                sessionRepository.save(s);
            }
        }
        return saved;
    }

    public User updatePermissions(Long id, Set<Permission> permissions) {
        User u = findById(id);
        u.setPermissions(permissions);
        sanitizeAndApplyRules(u);
        return userRepository.save(u);
    }

    public Set<Permission> getDefaultPermissions(Role role) {
        return switch (role) {
            case ADMINISTRADOR -> EnumSet.allOf(Permission.class);
            case GESTOR, FINANCEIRO -> EnumSet.of(
                    Permission.CLIENTES, Permission.VENDAS, Permission.FINANCEIRO, Permission.RELATORIOS
            );
            case MOTORISTA, VENDEDOR -> EnumSet.of(
                    Permission.ROTAS, Permission.VENDAS, Permission.CLIENTES, Permission.ESTOQUE
            );
            case USUARIO_COMUM -> EnumSet.noneOf(Permission.class);
        };
    }

    private void sanitizeAndApplyRules(User user) {
        if (user.getRole() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role é obrigatória");
        }

        Role role = user.getRole();
        // ADMIN sempre tem todas as permissões
        if (role == Role.ADMINISTRADOR) {
            user.setPermissions(EnumSet.allOf(Permission.class));
            return;
        }

        // Papéis com conjunto fixo
        if (role == Role.GESTOR || role == Role.FINANCEIRO) {
            user.setPermissions(EnumSet.of(
                    Permission.CLIENTES, Permission.VENDAS, Permission.FINANCEIRO, Permission.RELATORIOS
            ));
            return;
        }
        if (role == Role.MOTORISTA || role == Role.VENDEDOR) {
            user.setPermissions(EnumSet.of(
                    Permission.ROTAS, Permission.VENDAS, Permission.CLIENTES, Permission.ESTOQUE
            ));
            return;
        }

        // USUARIO_COMUM: aceita customizado; se vazio aplica none
        Set<Permission> provided = Optional.ofNullable(user.getPermissions()).orElseGet(HashSet::new);
        if (provided.isEmpty()) {
            user.setPermissions(EnumSet.noneOf(Permission.class));
        } else {
            Set<Permission> sanitized = provided.stream()
                    .collect(Collectors.toCollection(() -> EnumSet.noneOf(Permission.class)));
            user.setPermissions(sanitized);
        }
    }

    public List<Role> listRoles() {
        return Arrays.asList(Role.values());
    }

    public List<Permission> listPermissions() {
        return Arrays.asList(Permission.values());
    }
}
