package com.example.empleados.integration;

import com.example.empleados.domain.Departamento;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DepartamentoRelationIntegrationTest extends IntegrationTestBase {

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

        Empleado empleado = new Empleado();
        empleado.setClave("EMP-200");
        empleado.setNombre("Rosa");
        empleado.setDireccion("Inicial");
        empleado.setTelefono("000");
        empleado.setCorreo("rosa200@empleados.local");
        empleado.setHabilitado(true);
        empleado.setRol("USER");
        empleado.setDepartamento(departamento);
        empleadoRepository.save(empleado);
    }

    @Test
    void debeBloquearBorradoDeDepartamentoConEmpleadosAsociados() throws Exception {
        mockMvc.perform(delete("/api/v1/departamentos/IT"))
                .andExpect(status().isConflict());
    }

    @Test
    void debeRechazarAltaEmpleadoSiDepartamentoNoExiste() throws Exception {
        mockMvc.perform(post("/api/v1/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Mario",
                                  "direccion": "Calle 1",
                                  "telefono": "123456",
                                  "correo": "mario@empleados.local",
                                  "departamentoClave": "NO-EXISTE"
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}
