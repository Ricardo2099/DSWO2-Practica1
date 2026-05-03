package com.example.empleados.contract;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityNegativeContractTest {

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

    @AfterAll
    static void stopContainer() {
        if (postgres != null && postgres.isRunning()) {
            postgres.stop();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void login() throws Exception {
        String response = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("""
                                {
                                  "correo": "admin@empleados.local",
                                  "contrasena": "password"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        JsonNode tokenNode = json.get("token");
        if (tokenNode == null || tokenNode.asText().isBlank()) {
            throw new IllegalStateException("Login response did not include a non-empty 'token': " + response);
        }
        token = tokenNode.asText();
    }

    @Test
    void debeRetornar401SinCredenciales() throws Exception {
        mockMvc.perform(get("/api/v1/empleados"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void debeRetornar401ConTokenInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/empleados").header("Authorization", "Bearer bad-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void departamentosDebeRetornar401SinCredenciales() throws Exception {
        mockMvc.perform(get("/api/v1/departamentos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void departamentosDebeRetornar401ConTokenInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/departamentos").header("Authorization", "Bearer bad-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void debeRetornar401CuandoTokenFueCerrado() throws Exception {
        mockMvc.perform(post("/auth/logout").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/empleados").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }
}
