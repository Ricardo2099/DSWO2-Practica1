package com.example.empleados.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
abstract class IntegrationTestBase {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("empleados_db")
            .withUsername("empleados_user")
            .withPassword("empleados_pass");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        String externalUrl = System.getenv("SPRING_DATASOURCE_URL");
        if (externalUrl != null && !externalUrl.isBlank()) {
            return;
        }

        if (!postgres.isRunning()) {
            postgres.start();
        }
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
