package com.example.empleados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartamentoCreateRequest(
        @NotBlank(message = "clave es obligatoria")
        @Size(max = 50, message = "clave debe tener maximo 50 caracteres")
        String clave,

        @NotBlank(message = "nombre es obligatorio")
        @Size(max = 50, message = "nombre debe tener maximo 50 caracteres")
        String nombre
) {
}
