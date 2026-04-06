package com.example.empleados.exception;

public class DepartamentoNotFoundException extends RuntimeException {

    public DepartamentoNotFoundException(String clave) {
        super("Departamento no encontrado: " + clave);
    }
}
