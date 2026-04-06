package com.example.empleados.contract;

import com.example.empleados.config.BearerTokenFilter;
import com.example.empleados.controller.EmpleadoController;
import com.example.empleados.exception.DepartamentoNotFoundException;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, EmpleadoMapper.class})
class EmpleadoDepartamentoRelationContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @MockBean
    private BearerTokenFilter bearerTokenFilter;

    @Test
    void debeRetornar404CuandoDepartamentoNoExiste() throws Exception {
        Mockito.when(empleadoService.crear(Mockito.any()))
                .thenThrow(new DepartamentoNotFoundException("NO-EXISTE"));

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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Departamento no encontrado"));
    }
}
