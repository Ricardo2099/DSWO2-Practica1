package com.example.empleados.integration;

import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DepartamentoCreateIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @BeforeEach
    void clean() {
        empleadoRepository.deleteAll(
                empleadoRepository.findAll().stream()
                        .filter(e -> !"EMP-ADMIN".equals(e.getClave()))
                        .toList()
        );
        departamentoRepository.deleteAll();
    }

    @Test
    void debeCrearDepartamentoEnPostgres() throws Exception {
        mockMvc.perform(post("/api/v1/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clave": "IT",
                                  "nombre": "Tecnologia"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clave").value("IT"));
    }
}
