package com.example.empleados.controller;

import com.example.empleados.dto.DepartamentoCreateRequest;
import com.example.empleados.dto.DepartamentoResponse;
import com.example.empleados.dto.DepartamentoUpdateRequest;
import com.example.empleados.mapper.DepartamentoMapper;
import com.example.empleados.service.DepartamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/v1/departamentos")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;
    private final DepartamentoMapper departamentoMapper;

    public DepartamentoController(DepartamentoService departamentoService,
                                  DepartamentoMapper departamentoMapper) {
        this.departamentoService = departamentoService;
        this.departamentoMapper = departamentoMapper;
    }

    @PostMapping
    @Operation(summary = "Crear departamento")
    public ResponseEntity<DepartamentoResponse> crear(@Valid @RequestBody DepartamentoCreateRequest request) {
        DepartamentoResponse response = departamentoMapper.toResponse(departamentoService.crear(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{clave}")
                .buildAndExpand(response.clave())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar departamentos")
    public ResponseEntity<List<DepartamentoResponse>> listar() {
        List<DepartamentoResponse> response = departamentoService.listar().stream()
                .map(departamentoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clave}")
    @Operation(summary = "Obtener departamento por clave")
    public ResponseEntity<DepartamentoResponse> obtener(@PathVariable String clave) {
        return ResponseEntity.ok(departamentoMapper.toResponse(departamentoService.obtenerPorClave(clave)));
    }

    @PutMapping("/{clave}")
    @Operation(summary = "Actualizar departamento")
    public ResponseEntity<DepartamentoResponse> actualizar(@PathVariable String clave,
                                                           @Valid @RequestBody DepartamentoUpdateRequest request) {
        return ResponseEntity.ok(departamentoMapper.toResponse(departamentoService.actualizar(clave, request)));
    }

    @DeleteMapping("/{clave}")
    @Operation(summary = "Eliminar departamento")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        departamentoService.eliminar(clave);
        return ResponseEntity.noContent().build();
    }
}
