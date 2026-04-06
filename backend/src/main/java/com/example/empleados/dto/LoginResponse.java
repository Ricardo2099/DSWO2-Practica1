package com.example.empleados.dto;

import java.time.LocalDateTime;

public record LoginResponse(
        String token,
        LocalDateTime expiraEn
) {
}
