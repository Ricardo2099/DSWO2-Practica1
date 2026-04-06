package com.example.empleados.contract;

import com.example.empleados.config.BearerTokenFilter;
import com.example.empleados.controller.DepartamentoController;
import com.example.empleados.domain.Departamento;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, DepartamentoMapper.class})
class DepartamentoCreateContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartamentoService departamentoService;

    @MockBean
    private BearerTokenFilter bearerTokenFilter;

    @Test
    void debeCrearDepartamento() throws Exception {
        Departamento departamento = new Departamento();
        departamento.setClave("IT");
        departamento.setNombre("Tecnologia");

        Mockito.when(departamentoService.crear(Mockito.any())).thenReturn(departamento);

        mockMvc.perform(post("/api/v1/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clave": "IT",
                                  "nombre": "Tecnologia"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/departamentos/IT"))
                .andExpect(jsonPath("$.clave").value("IT"));
    }

    @Test
    void debeRechazarNombreMayorA50() throws Exception {
        String nombreLargo = "A".repeat(51);

        mockMvc.perform(post("/api/v1/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clave": "IT",
                                  "nombre": "%s"
                                }
                                """.formatted(nombreLargo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Solicitud inválida"));
    }
}
