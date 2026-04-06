package com.example.empleados.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "correo es obligatorio")
        @Email(message = "correo debe tener formato valido")
        String correo,

        @NotBlank(message = "contrasena es obligatoria")
        String contrasena
) {
}
