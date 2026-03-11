package com.example.empleados.service;

import com.example.empleados.exception.ClaveGeneracionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaveEmpleadoGeneratorTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void debeGenerarClaveConPrefijo() {
        when(jdbcTemplate.queryForObject("SELECT nextval('empleados_clave_seq')", Long.class)).thenReturn(42L);

        ClaveEmpleadoGenerator generator = new ClaveEmpleadoGenerator(jdbcTemplate);
        String clave = generator.generar();

        assertThat(clave).isEqualTo("EMP-42");
    }

    @Test
    void debeLanzarExcepcionTrasReintentos() {
        when(jdbcTemplate.queryForObject("SELECT nextval('empleados_clave_seq')", Long.class)).thenReturn(null);

        ClaveEmpleadoGenerator generator = new ClaveEmpleadoGenerator(jdbcTemplate);

        assertThatThrownBy(generator::generar)
                .isInstanceOf(ClaveGeneracionException.class);
    }
}
