package com.example.empleados.contract;

import com.example.empleados.config.BearerTokenFilter;
import com.example.empleados.controller.DepartamentoController;
import com.example.empleados.exception.DepartamentoInUseException;
import com.example.empleados.exception.GlobalExceptionHandler;
import com.example.empleados.mapper.DepartamentoMapper;
import com.example.empleados.service.DepartamentoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, DepartamentoMapper.class})
class DepartamentoDeleteRelationContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartamentoService departamentoService;

    @MockBean
    private BearerTokenFilter bearerTokenFilter;

    @Test
    void debeRetornar409CuandoDepartamentoTieneEmpleados() throws Exception {
        Mockito.doThrow(new DepartamentoInUseException("IT"))
                .when(departamentoService).eliminar("IT");

        mockMvc.perform(delete("/api/v1/departamentos/IT"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Departamento en uso"));
    }
}
