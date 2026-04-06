package com.example.empleados.exception;

public class DepartamentoDuplicadoException extends RuntimeException {

    public DepartamentoDuplicadoException(String clave) {
        super("Ya existe un departamento con clave " + clave);
    }
}
