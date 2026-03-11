package com.example.empleados.contract;

import com.example.empleados.config.SecurityConfig;
import com.example.empleados.controller.EmpleadoController;
import com.example.empleados.domain.Empleado;
import com.example.empleados.exception.GlobalExceptionHandler;
import com.example.empleados.mapper.EmpleadoMapper;
import com.example.empleados.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class, EmpleadoMapper.class})
class EmpleadoCreateContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Test
    void debeCrearEmpleadoConClaveAutogenerada() throws Exception {
        Empleado empleado = new Empleado();
        empleado.setClave("EMP-1");
        empleado.setNombre("Juan");
        empleado.setDireccion("Calle 1");
        empleado.setTelefono("123456");

        Mockito.when(empleadoService.crear(Mockito.any())).thenReturn(empleado);

        mockMvc.perform(post("/api/v1/empleados")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Juan",
                                  "direccion": "Calle 1",
                                  "telefono": "123456"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/empleados/EMP-1"))
                .andExpect(jsonPath("$.clave").value("EMP-1"));
    }

    @Test
    void debeRechazarNombreMayorA100() throws Exception {
        String nombreLargo = "A".repeat(101);

        mockMvc.perform(post("/api/v1/empleados")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "%s",
                                  "direccion": "Calle 1",
                                  "telefono": "123456"
                                }
                                """.formatted(nombreLargo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Solicitud inválida"));
    }
}
