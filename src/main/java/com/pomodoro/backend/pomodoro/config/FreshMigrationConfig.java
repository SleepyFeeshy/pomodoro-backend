package com.pomodoro.backend.pomodoro.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy; // Boot 4 package; Boot 3 used ...autoconfigure.flyway
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.db.fresh", havingValue = "true")
public class FreshMigrationConfig {

    @Bean
    FlywayMigrationStrategy cleanThenMigrate() {
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }
}