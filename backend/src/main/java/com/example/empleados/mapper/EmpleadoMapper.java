package com.example.empleados.mapper;

import com.example.empleados.domain.Empleado;
import com.example.empleados.dto.EmpleadoResponse;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {

    public EmpleadoResponse toResponse(Empleado empleado) {
        return new EmpleadoResponse(
                empleado.getClave(),
                empleado.getNombre(),
                empleado.getDireccion(),
                empleado.getTelefono()
        );
    }
}
