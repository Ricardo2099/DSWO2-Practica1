package com.example.empleados.integration;

import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmpleadoUpdateDeleteIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @BeforeEach
    void setup() {
        empleadoRepository.deleteAll();
        Empleado empleado = new Empleado();
        empleado.setClave("EMP-100");
        empleado.setNombre("Rosa");
        empleado.setDireccion("Inicial");
        empleado.setTelefono("000");
        empleadoRepository.save(empleado);
    }

    @Test
    void debeActualizarYEliminar() throws Exception {
        mockMvc.perform(put("/api/v1/empleados/EMP-100")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Rosa2",
                                  "direccion": "Nueva",
                                  "telefono": "999"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/empleados/EMP-100").with(httpBasic("admin", "admin123")))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/empleados/EMP-100").with(httpBasic("admin", "admin123")))
                .andExpect(status().isNotFound());
    }
}
