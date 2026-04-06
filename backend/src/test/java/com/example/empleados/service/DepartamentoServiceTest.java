package com.example.empleados.service;

import com.example.empleados.domain.Departamento;
import com.example.empleados.dto.DepartamentoCreateRequest;
import com.example.empleados.dto.DepartamentoUpdateRequest;
import com.example.empleados.exception.DepartamentoInUseException;
import com.example.empleados.exception.DepartamentoNotFoundException;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartamentoServiceTest {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private DepartamentoService departamentoService;

    @Test
    void crearDebeGuardarDepartamento() {
        DepartamentoCreateRequest req = new DepartamentoCreateRequest("IT", "Tecnologia");
        when(departamentoRepository.existsById("IT")).thenReturn(false);
        when(departamentoRepository.save(any(Departamento.class))).thenAnswer(i -> i.getArgument(0));

        Departamento creado = departamentoService.crear(req);

        assertThat(creado.getClave()).isEqualTo("IT");
        assertThat(creado.getNombre()).isEqualTo("Tecnologia");
    }

    @Test
    void actualizarDebeLanzarCuandoNoExiste() {
        when(departamentoRepository.findById("NO")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departamentoService.actualizar("NO", new DepartamentoUpdateRequest("X")))
                .isInstanceOf(DepartamentoNotFoundException.class);
    }

    @Test
    void eliminarDebeFallarSiEstaEnUso() {
        Departamento departamento = new Departamento();
        departamento.setClave("IT");
        when(departamentoRepository.findById("IT")).thenReturn(Optional.of(departamento));
        when(empleadoRepository.countByDepartamento_Clave("IT")).thenReturn(1L);

        assertThatThrownBy(() -> departamentoService.eliminar("IT"))
                .isInstanceOf(DepartamentoInUseException.class);
    }

    @Test
    void eliminarDebeBorrarSiNoTieneEmpleados() {
        Departamento departamento = new Departamento();
        departamento.setClave("IT");
        when(departamentoRepository.findById("IT")).thenReturn(Optional.of(departamento));
        when(empleadoRepository.countByDepartamento_Clave("IT")).thenReturn(0L);

        departamentoService.eliminar("IT");

        verify(departamentoRepository).delete(eq(departamento));
    }
}
