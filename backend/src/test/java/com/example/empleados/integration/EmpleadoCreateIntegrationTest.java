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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EmpleadoCreateIntegrationTest extends IntegrationTestBase {

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

        Departamento departamento = new Departamento();
        departamento.setClave("IT");
        departamento.setNombre("Tecnologia");
        departamentoRepository.save(departamento);
    }

    @Test
    void debeCrearEmpleadoEnPostgres() throws Exception {
        mockMvc.perform(post("/api/v1/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Maria",
                                  "direccion": "Calle 10",
                                                                    "telefono": "300000",
                                                                    "correo": "maria@empleados.local",
                                                                    "departamentoClave": "IT"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clave").value(org.hamcrest.Matchers.startsWith("EMP-")));
    }
}
