package com.example.empleados.contract;

import com.example.empleados.config.BearerTokenFilter;
import com.example.empleados.controller.EmpleadoController;
import com.example.empleados.domain.Empleado;
import com.example.empleados.exception.EmpleadoNotFoundException;
import com.example.empleados.exception.GlobalExceptionHandler;
import com.example.empleados.mapper.EmpleadoMapper;
import com.example.empleados.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, EmpleadoMapper.class})
class EmpleadoQueryContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

        @MockBean
        private BearerTokenFilter bearerTokenFilter;

    @Test
    void debeListarYObtenerEmpleado() throws Exception {
        Empleado empleado = new Empleado();
        empleado.setClave("EMP-2");
        empleado.setNombre("Ana");
        empleado.setDireccion("Calle 2");
        empleado.setTelefono("555");
        empleado.setCorreo("ana@empleados.local");

        Mockito.when(empleadoService.listar()).thenReturn(List.of(empleado));
        Mockito.when(empleadoService.obtenerPorClave("EMP-2")).thenReturn(empleado);

        mockMvc.perform(get("/api/v1/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clave").value("EMP-2"));

        mockMvc.perform(get("/api/v1/empleados/EMP-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clave").value("EMP-2"));
    }

    @Test
    void debeRetornar404CuandoNoExiste() throws Exception {
        Mockito.when(empleadoService.obtenerPorClave("EMP-404"))
                .thenThrow(new EmpleadoNotFoundException("EMP-404"));

        mockMvc.perform(get("/api/v1/empleados/EMP-404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Empleado no encontrado"));
    }
}
