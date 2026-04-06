package com.example.empleados.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EmpleadoNotFoundException.class)
    public ProblemDetail handleNotFound(EmpleadoNotFoundException ex, ServletWebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Empleado no encontrado");
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(request.getRequest().getRequestURI()));
        return problem;
    }

    @ExceptionHandler(DepartamentoNotFoundException.class)
    public ProblemDetail handleDepartamentoNotFound(DepartamentoNotFoundException ex, ServletWebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Departamento no encontrado");
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(request.getRequest().getRequestURI()));
        return problem;
    }

    @ExceptionHandler(DepartamentoDuplicadoException.class)
    public ProblemDetail handleDepartamentoDuplicado(DepartamentoDuplicadoException ex, ServletWebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Departamento duplicado");
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(request.getRequest().getRequestURI()));
        return problem;
    }

    @ExceptionHandler(DepartamentoInUseException.class)
    public ProblemDetail handleDepartamentoInUse(DepartamentoInUseException ex, ServletWebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Departamento en uso");
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(request.getRequest().getRequestURI()));
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, ServletWebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Error de validación");
        problem.setTitle("Solicitud inválida");
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(request.getRequest().getRequestURI()));
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        problem.setProperty("errors", errores);
        return problem;
    }

    @ExceptionHandler(ClaveGeneracionException.class)
    public ProblemDetail handleClave(ClaveGeneracionException ex, ServletWebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problem.setTitle("Error de generación de clave");
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(request.getRequest().getRequestURI()));
        return problem;
    }

    @ExceptionHandler({AuthenticationException.class, AuthenticationCredentialsNotFoundException.class})
    public ProblemDetail handleAuthentication(Exception ex, ServletWebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        problem.setTitle("No autorizado");
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(request.getRequest().getRequestURI()));
        return problem;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex, ServletWebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "No tiene permisos para esta operacion");
        problem.setTitle("Acceso denegado");
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(request.getRequest().getRequestURI()));
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, ServletWebRequest request) {
        log.error("Error no controlado en {} {}", request.getHttpMethod(), request.getRequest().getRequestURI(), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        problem.setTitle("Error interno");
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(request.getRequest().getRequestURI()));
        return problem;
    }
}
