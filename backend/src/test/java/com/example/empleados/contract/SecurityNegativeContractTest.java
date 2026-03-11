package com.example.empleados.contract;

import com.example.empleados.config.SecurityConfig;
import com.example.empleados.controller.EmpleadoController;
import com.example.empleados.exception.GlobalExceptionHandler;
import com.example.empleados.mapper.EmpleadoMapper;
import com.example.empleados.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class, EmpleadoMapper.class})
class SecurityNegativeContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Test
    void debeRetornar401SinCredenciales() throws Exception {
        mockMvc.perform(get("/api/v1/empleados"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void debeRetornar401ConCredencialesInvalidas() throws Exception {
        mockMvc.perform(get("/api/v1/empleados").with(httpBasic("bad", "bad")))
                .andExpect(status().isUnauthorized());
    }
}
