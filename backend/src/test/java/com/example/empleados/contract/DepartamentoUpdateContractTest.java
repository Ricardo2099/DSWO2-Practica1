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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, DepartamentoMapper.class})
class DepartamentoUpdateContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartamentoService departamentoService;

    @MockBean
    private BearerTokenFilter bearerTokenFilter;

    @Test
    void debeActualizarDepartamento() throws Exception {
        Departamento departamento = new Departamento();
        departamento.setClave("IT");
        departamento.setNombre("Tecnologia y Sistemas");

        Mockito.when(departamentoService.actualizar(Mockito.eq("IT"), Mockito.any())).thenReturn(departamento);

        mockMvc.perform(put("/api/v1/departamentos/IT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Tecnologia y Sistemas"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clave").value("IT"))
                .andExpect(jsonPath("$.nombre").value("Tecnologia y Sistemas"));
    }

    @Test
    void debeRetornar404EnActualizacionCuandoNoExiste() throws Exception {
        Mockito.when(departamentoService.actualizar(Mockito.eq("NO-EXISTE"), Mockito.any()))
                .thenThrow(new DepartamentoNotFoundException("NO-EXISTE"));

        mockMvc.perform(put("/api/v1/departamentos/NO-EXISTE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Cualquier"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Departamento no encontrado"));
    }
}
