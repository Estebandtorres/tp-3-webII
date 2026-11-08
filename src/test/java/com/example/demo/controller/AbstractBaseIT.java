package com.example.demo.controller;

import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractBaseIT {

    static final PostgreSQLContainer postgres;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));

        postgres = new PostgreSQLContainer("postgres:15-alpine")
                .withDatabaseName("test_db")
                .withUsername("test")
                .withPassword("test");
        postgres.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.flyway.user", postgres::getUsername);
        registry.add("spring.flyway.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void limpiarBaseDeDatos() {
        jdbcTemplate.execute("TRUNCATE TABLE favoritos, listas, usuarios RESTART IDENTITY CASCADE");
    }
}