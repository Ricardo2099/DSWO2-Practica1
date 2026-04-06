package com.example.empleados.contract;

import com.example.empleados.config.BearerTokenFilter;
import com.example.empleados.controller.DepartamentoController;
import com.example.empleados.domain.Departamento;
import com.example.empleados.exception.DepartamentoNotFoundException;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, DepartamentoMapper.class})
class DepartamentoQueryContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartamentoService departamentoService;

    @MockBean
    private BearerTokenFilter bearerTokenFilter;

    @Test
    void debeListarYObtenerDepartamento() throws Exception {
        Departamento departamento = new Departamento();
        departamento.setClave("IT");
        departamento.setNombre("Tecnologia");

        Mockito.when(departamentoService.listar()).thenReturn(List.of(departamento));
        Mockito.when(departamentoService.obtenerPorClave("IT")).thenReturn(departamento);

        mockMvc.perform(get("/api/v1/departamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clave").value("IT"));

        mockMvc.perform(get("/api/v1/departamentos/IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tecnologia"));
    }

    @Test
    void debeRetornar404CuandoNoExiste() throws Exception {
        Mockito.when(departamentoService.obtenerPorClave("NO-EXISTE"))
                .thenThrow(new DepartamentoNotFoundException("NO-EXISTE"));

        mockMvc.perform(get("/api/v1/departamentos/NO-EXISTE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Departamento no encontrado"));
    }
}
