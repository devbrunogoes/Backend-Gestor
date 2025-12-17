package org.backend.controller;

import jakarta.validation.Valid;
import org.backend.dto.SettingsDTO;
import org.backend.model.Settings;
import org.backend.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/settings","/api/configuracoes"})
public class SettingsController {

    private final SettingsService svc;

    public SettingsController(SettingsService svc) {
        this.svc = svc;
    }

    @GetMapping
    public SettingsDTO get() {
        Settings s = svc.get();
        return toDto(s);
    }

    @PutMapping
    public ResponseEntity<SettingsDTO> update(@Valid @RequestBody SettingsDTO payload) {
        Settings saved = svc.update(toEntity(payload));
        return ResponseEntity.ok(toDto(saved));
    }

    private SettingsDTO toDto(Settings s) {
        if (s == null) return null;
        return new SettingsDTO(
                s.getId(),
                s.getCompanyName(),
                s.getCnpj(),
                s.getCompanyPhone(),
                s.getCompanyEmail(),
                s.getCompanyAddress(),
                s.getCurrency(),
                s.getLocale(),
                s.getTimezone(),
                s.getTheme(),
                s.getNotifyEmail(),
                s.getNotifySystem(),
                s.getUpdatedAt()
        );
    }

    private Settings toEntity(SettingsDTO d) {
        if (d == null) return null;
        Settings s = new Settings();
        s.setId(d.id());
        s.setCompanyName(d.companyName());
        s.setCnpj(d.cnpj());
        s.setCompanyPhone(d.companyPhone());
        s.setCompanyEmail(d.companyEmail());
        s.setCompanyAddress(d.companyAddress());
        s.setCurrency(d.currency());
        s.setLocale(d.locale());
        s.setTimezone(d.timezone());
        s.setTheme(d.theme());
        s.setNotifyEmail(d.notifyEmail());
        s.setNotifySystem(d.notifySystem());
        s.setUpdatedAt(d.updatedAt());
        return s;
    }
}
