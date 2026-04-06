package com.example.empleados.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.mockito.Mockito.mock;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    private ServletWebRequest request() {
        return new ServletWebRequest(new MockHttpServletRequest("GET", "/test"));
    }

    @Test
    void handleNotFoundDebeRetornar404() {
        ProblemDetail problem = handler.handleNotFound(new EmpleadoNotFoundException("EMP-1"), request());
        assertThat(problem.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void handleClaveDebeRetornar500() {
        ProblemDetail problem = handler.handleClave(new ClaveGeneracionException("error"), request());
        assertThat(problem.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void handleGenericDebeRetornar500() {
        ProblemDetail problem = handler.handleGeneric(new RuntimeException("boom"), request());
        assertThat(problem.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void handleValidationDebeRetornar400() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
        bindingResult.addError(new FieldError("obj", "nombre", "nombre es obligatorio"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
            mock(MethodParameter.class),
                bindingResult
        );

        ProblemDetail problem = handler.handleValidation(ex, request());
        assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
