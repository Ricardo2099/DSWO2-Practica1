package com.example.empleados.controller;

import com.example.empleados.dto.LoginRequest;
import com.example.empleados.dto.LoginResponse;
import com.example.empleados.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Tag(name = "Autenticacion")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    @Operation(summary = "Iniciar sesion con correo y contrasena")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/auth/logout")
    @Operation(summary = "Cerrar sesion actual")
    public ResponseEntity<Void> logout(@RequestHeader(name = "Authorization", required = false) String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            authService.logout(authorization.substring(7));
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/empleados/{clave}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desbloquear cuenta de empleado")
    public ResponseEntity<Void> unlock(@PathVariable String clave, Authentication authentication) {
        authService.unlockAccount(clave, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
