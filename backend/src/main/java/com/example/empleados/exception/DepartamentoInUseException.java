package com.example.empleados.exception;

public class DepartamentoInUseException extends RuntimeException {

    public DepartamentoInUseException(String clave) {
        super("No se puede eliminar el departamento " + clave + " porque tiene empleados asociados");
    }
}
