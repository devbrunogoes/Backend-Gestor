package org.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record SettingsDTO(
        Long id,
        @Size(max = 255) String companyName,
        @Size(max = 32) String cnpj,
        @Size(max = 32) String companyPhone,
        @Email @Size(max = 255) String companyEmail,
        @Size(max = 1000) String companyAddress,
        @Size(max = 8) String currency,
        @Pattern(regexp = "[a-z]{2}_[A-Z]{2}", message = "locale deve ser no formato ll_LL, ex: pt_BR") String locale,
        @Size(max = 64) String timezone,
        @Size(max = 32) String theme,
        Boolean notifyEmail,
        Boolean notifySystem,
        LocalDateTime updatedAt
) {}

