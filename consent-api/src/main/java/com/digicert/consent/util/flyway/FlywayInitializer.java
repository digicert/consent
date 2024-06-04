package com.digicert.consent.util.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;

public class FlywayInitializer implements CommandLineRunner {

    private final Flyway flyway;

    public FlywayInitializer(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void run(String... args) {
        flyway.baseline();
        flyway.migrate();
    }
}
