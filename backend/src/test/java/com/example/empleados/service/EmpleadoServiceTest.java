package com.example.empleados.service;

import com.example.empleados.domain.Empleado;
import com.example.empleados.dto.EmpleadoCreateRequest;
import com.example.empleados.dto.EmpleadoUpdateRequest;
import com.example.empleados.exception.EmpleadoNotFoundException;
import com.example.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private ClaveEmpleadoGenerator claveEmpleadoGenerator;

    @InjectMocks
    private EmpleadoService empleadoService;

    @Test
    void crearDebeAsignarClaveYGuardar() {
        EmpleadoCreateRequest req = new EmpleadoCreateRequest("Ana", "Dir", "123");
        when(claveEmpleadoGenerator.generar()).thenReturn("EMP-1");
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(i -> i.getArgument(0));

        Empleado creado = empleadoService.crear(req);

        assertThat(creado.getClave()).isEqualTo("EMP-1");
        assertThat(creado.getNombre()).isEqualTo("Ana");
        verify(empleadoRepository, times(1)).save(any(Empleado.class));
    }

    @Test
    void listarDebeDelegarEnRepositorio() {
        when(empleadoRepository.findAll()).thenReturn(List.of(new Empleado()));
        assertThat(empleadoService.listar()).hasSize(1);
    }

    @Test
    void obtenerPorClaveDebeLanzarNoEncontrado() {
        when(empleadoRepository.findById("EMP-99")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> empleadoService.obtenerPorClave("EMP-99"))
                .isInstanceOf(EmpleadoNotFoundException.class);
    }

    @Test
    void actualizarDebeModificarCamposSinCambiarClave() {
        Empleado existente = new Empleado();
        existente.setClave("EMP-2");
        existente.setNombre("Old");
        when(empleadoRepository.findById("EMP-2")).thenReturn(Optional.of(existente));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(i -> i.getArgument(0));

        Empleado actualizado = empleadoService.actualizar("EMP-2", new EmpleadoUpdateRequest("New", "Dir2", "999"));

        assertThat(actualizado.getClave()).isEqualTo("EMP-2");
        assertThat(actualizado.getNombre()).isEqualTo("New");
        verify(empleadoRepository).save(eq(existente));
    }

    @Test
    void eliminarDebeBorrarCuandoExiste() {
        Empleado existente = new Empleado();
        existente.setClave("EMP-3");
        when(empleadoRepository.findById("EMP-3")).thenReturn(Optional.of(existente));

        empleadoService.eliminar("EMP-3");

        verify(empleadoRepository).delete(existente);
    }
}
