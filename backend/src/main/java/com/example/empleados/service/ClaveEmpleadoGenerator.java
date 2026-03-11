package com.example.empleados.service;

import com.example.empleados.exception.ClaveGeneracionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClaveEmpleadoGenerator {

    private static final String PREFIJO = "EMP-";
    private static final int MAX_REINTENTOS = 3;

    private final JdbcTemplate jdbcTemplate;

    public ClaveEmpleadoGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generar() {
        for (int intento = 1; intento <= MAX_REINTENTOS; intento++) {
            Long numero = jdbcTemplate.queryForObject("SELECT nextval('empleados_clave_seq')", Long.class);
            if (numero != null && numero > 0) {
                return PREFIJO + numero;
            }
        }
        throw new ClaveGeneracionException("No fue posible generar una clave de empleado tras 3 intentos");
    }
}
