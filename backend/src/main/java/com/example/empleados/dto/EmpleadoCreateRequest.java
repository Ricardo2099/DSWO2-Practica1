package com.example.empleados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record EmpleadoCreateRequest(
        @NotBlank(message = "nombre es obligatorio")
        @Size(max = 100, message = "nombre debe tener máximo 100 caracteres")
        String nombre,

        @NotBlank(message = "direccion es obligatoria")
        @Size(max = 100, message = "direccion debe tener máximo 100 caracteres")
        String direccion,

        @NotBlank(message = "telefono es obligatorio")
        @Size(max = 100, message = "telefono debe tener máximo 100 caracteres")
        String telefono,

        @NotBlank(message = "correo es obligatorio")
        @Email(message = "correo debe tener formato valido")
        @Size(max = 255, message = "correo debe tener máximo 255 caracteres")
        String correo,

        @NotBlank(message = "departamentoClave es obligatorio")
        @Size(max = 50, message = "departamentoClave debe tener maximo 50 caracteres")
        String departamentoClave
) {
}
