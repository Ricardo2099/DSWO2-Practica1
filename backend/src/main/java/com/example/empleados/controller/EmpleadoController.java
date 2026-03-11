package com.example.empleados.controller;

import com.example.empleados.dto.EmpleadoCreateRequest;
import com.example.empleados.dto.EmpleadoResponse;
import com.example.empleados.dto.EmpleadoUpdateRequest;
import com.example.empleados.mapper.EmpleadoMapper;
import com.example.empleados.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/empleados")
@Tag(name = "Empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final EmpleadoMapper empleadoMapper;

    public EmpleadoController(EmpleadoService empleadoService, EmpleadoMapper empleadoMapper) {
        this.empleadoService = empleadoService;
        this.empleadoMapper = empleadoMapper;
    }

    @PostMapping
    @Operation(summary = "Crear empleado")
    public ResponseEntity<EmpleadoResponse> crear(@Valid @RequestBody EmpleadoCreateRequest request) {
        EmpleadoResponse response = empleadoMapper.toResponse(empleadoService.crear(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{clave}")
                .buildAndExpand(response.clave())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar empleados")
    public ResponseEntity<List<EmpleadoResponse>> listar() {
        List<EmpleadoResponse> response = empleadoService.listar().stream()
                .map(empleadoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clave}")
    @Operation(summary = "Obtener empleado por clave")
    public ResponseEntity<EmpleadoResponse> obtener(@PathVariable String clave) {
        return ResponseEntity.ok(empleadoMapper.toResponse(empleadoService.obtenerPorClave(clave)));
    }

    @PutMapping("/{clave}")
    @Operation(summary = "Actualizar empleado")
    public ResponseEntity<EmpleadoResponse> actualizar(@PathVariable String clave,
                                                       @Valid @RequestBody EmpleadoUpdateRequest request) {
        return ResponseEntity.ok(empleadoMapper.toResponse(empleadoService.actualizar(clave, request)));
    }

    @DeleteMapping("/{clave}")
    @Operation(summary = "Eliminar empleado")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        empleadoService.eliminar(clave);
        return ResponseEntity.noContent().build();
    }
}
