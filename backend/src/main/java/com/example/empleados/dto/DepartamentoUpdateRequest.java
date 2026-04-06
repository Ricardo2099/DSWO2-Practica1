package com.example.empleados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartamentoUpdateRequest(
        @NotBlank(message = "nombre es obligatorio")
        @Size(max = 50, message = "nombre debe tener maximo 50 caracteres")
        String nombre
) {
}
