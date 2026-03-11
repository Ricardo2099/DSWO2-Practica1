package com.example.empleados.exception;

public class EmpleadoNotFoundException extends RuntimeException {

    public EmpleadoNotFoundException(String clave) {
        super("Empleado no encontrado para clave: " + clave);
    }
}
