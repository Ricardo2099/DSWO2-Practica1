package com.example.empleados.dto;

public record EmpleadoResponse(
        String clave,
        String nombre,
        String direccion,
        String telefono
) {
}
