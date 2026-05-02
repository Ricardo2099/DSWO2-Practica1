package com.example.empleados.integration;

import com.example.empleados.domain.Departamento;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DepartamentoQueryUpdateIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @BeforeEach
    void setup() {
        empleadoRepository.deleteAll(
            empleadoRepository.findAll().stream()
                .filter(e -> !"EMP-ADMIN".equals(e.getClave()))
                .toList()
        );
        departamentoRepository.deleteAll();

        Departamento departamento = new Departamento();
        departamento.setClave("IT");
        departamento.setNombre("Tecnologia");
        departamentoRepository.save(departamento);
    }

    @Test
    void debeConsultarYActualizar() throws Exception {
        mockMvc.perform(get("/api/v1/departamentos/IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tecnologia"));

        mockMvc.perform(put("/api/v1/departamentos/IT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Tecnologia y Sistemas"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tecnologia y Sistemas"));
    }
}
