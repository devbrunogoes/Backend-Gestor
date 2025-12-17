package org.backend;

import org.backend.model.Role;
import org.backend.model.User;
import org.backend.model.Permission;
import org.backend.repository.UserRepository;
import org.backend.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("unused")
    @Bean
    CommandLineRunner seedAdmin(org.springframework.context.ApplicationContext ctx, Environment env) {
        return args -> {
            boolean enableSeed = Boolean.parseBoolean(env.getProperty("app.dev.createAdmin", "false"));
            boolean resetAdmin = Boolean.parseBoolean(env.getProperty("app.dev.resetAdmin", "false"));
            if (!enableSeed) {
                return; // somente semeia quando explicitamente habilitado no profile
            }
        };
    }
}
