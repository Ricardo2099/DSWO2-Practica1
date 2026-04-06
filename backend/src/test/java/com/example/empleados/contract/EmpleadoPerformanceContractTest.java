package com.example.empleados.contract;

import com.example.empleados.config.BearerTokenFilter;
import com.example.empleados.controller.EmpleadoController;
import com.example.empleados.domain.Empleado;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, EmpleadoMapper.class})
class EmpleadoPerformanceContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @MockBean
    private BearerTokenFilter bearerTokenFilter;

    @Test
    void p95DeListarDebeSerMenorA300ms() throws Exception {
        Empleado empleado = new Empleado();
        empleado.setClave("EMP-500");
        empleado.setNombre("Perf");
        empleado.setDireccion("PerfDir");
        empleado.setTelefono("123");
        empleado.setCorreo("perf@empleados.local");
        Mockito.when(empleadoService.listar()).thenReturn(List.of(empleado));

        List<Long> tiemposMs = new ArrayList<>();
        int iteraciones = 120;

        for (int i = 0; i < iteraciones; i++) {
            long inicio = System.nanoTime();
                mockMvc.perform(get("/api/v1/empleados"))
                    .andExpect(status().isOk());
            long fin = System.nanoTime();
            tiemposMs.add((fin - inicio) / 1_000_000);
        }

        tiemposMs.sort(Comparator.naturalOrder());
        int indice95 = (int) Math.ceil(iteraciones * 0.95) - 1;
        long p95 = tiemposMs.get(Math.max(indice95, 0));

        assertThat(p95).isLessThan(300);
    }
}
