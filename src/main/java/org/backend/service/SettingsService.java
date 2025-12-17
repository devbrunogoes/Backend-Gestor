package org.backend.service;

import java.time.LocalDateTime;
import org.backend.model.Settings;
import org.backend.repository.SettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SettingsService {

    private final SettingsRepository repo;

    public SettingsService(SettingsRepository repo) { this.repo = repo; }

    public Settings get() {
        return repo.findTopByOrderByIdAsc().orElseGet(() -> repo.save(new Settings()));
    }

    @Transactional
    public Settings update(Settings incoming) {
        if (incoming == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configurações inválidas");
        Settings s = get();
        // merge company data
        s.setCompanyName(incoming.getCompanyName());
        s.setCnpj(incoming.getCnpj());
        s.setCompanyPhone(incoming.getCompanyPhone());
        s.setCompanyEmail(incoming.getCompanyEmail());
        s.setCompanyAddress(incoming.getCompanyAddress());
        // merge preferences
        s.setCurrency(incoming.getCurrency());
        s.setLocale(incoming.getLocale());
        s.setTimezone(incoming.getTimezone());
        s.setTheme(incoming.getTheme());
        s.setNotifyEmail(incoming.getNotifyEmail());
        s.setNotifySystem(incoming.getNotifySystem());
        s.setUpdatedAt(LocalDateTime.now());
        return repo.save(s);
    }
}

