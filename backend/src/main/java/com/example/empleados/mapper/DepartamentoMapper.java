package com.example.empleados.mapper;

import com.example.empleados.domain.Departamento;
import com.example.empleados.dto.DepartamentoResponse;
import org.springframework.stereotype.Component;

@Component
public class DepartamentoMapper {

    public DepartamentoResponse toResponse(Departamento departamento) {
        return new DepartamentoResponse(departamento.getClave(), departamento.getNombre());
    }
}
