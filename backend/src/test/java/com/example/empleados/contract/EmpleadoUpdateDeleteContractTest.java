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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class, EmpleadoMapper.class})
class EmpleadoUpdateDeleteContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Test
    void debeActualizarSinCambiarClave() throws Exception {
        Empleado empleado = new Empleado();
        empleado.setClave("EMP-3");
        empleado.setNombre("Luis");
        empleado.setDireccion("Nueva");
        empleado.setTelefono("777");

        Mockito.when(empleadoService.actualizar(Mockito.eq("EMP-3"), Mockito.any())).thenReturn(empleado);

        mockMvc.perform(put("/api/v1/empleados/EMP-3")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Luis",
                                  "direccion": "Nueva",
                                  "telefono": "777"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clave").value("EMP-3"));
    }

    @Test
    void debeEliminarEmpleado() throws Exception {
        mockMvc.perform(delete("/api/v1/empleados/EMP-3").with(httpBasic("admin", "admin123")))
                .andExpect(status().isNoContent());
    }
}
