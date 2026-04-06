package com.example.empleados.service;

import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.Departamento;
import com.example.empleados.dto.EmpleadoCreateRequest;
import com.example.empleados.dto.EmpleadoUpdateRequest;
import com.example.empleados.exception.DepartamentoNotFoundException;
import com.example.empleados.exception.EmpleadoNotFoundException;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final ClaveEmpleadoGenerator claveEmpleadoGenerator;

    public EmpleadoService(EmpleadoRepository empleadoRepository,
                           DepartamentoRepository departamentoRepository,
                           ClaveEmpleadoGenerator claveEmpleadoGenerator) {
        this.empleadoRepository = empleadoRepository;
        this.departamentoRepository = departamentoRepository;
        this.claveEmpleadoGenerator = claveEmpleadoGenerator;
    }

    @Transactional
    public Empleado crear(EmpleadoCreateRequest request) {
        Departamento departamento = departamentoRepository.findById(request.departamentoClave().trim())
            .orElseThrow(() -> new DepartamentoNotFoundException(request.departamentoClave()));

        Empleado empleado = new Empleado();
        empleado.setClave(claveEmpleadoGenerator.generar());
        empleado.setNombre(request.nombre());
        empleado.setDireccion(request.direccion());
        empleado.setTelefono(request.telefono());
        empleado.setCorreo(request.correo().trim().toLowerCase());
        empleado.setDepartamento(departamento);
        empleado.setHabilitado(true);
        empleado.setRol("USER");
        return empleadoRepository.save(empleado);
    }

    @Transactional(readOnly = true)
    public List<Empleado> listar() {
        return empleadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Empleado obtenerPorClave(String clave) {
        return empleadoRepository.findById(clave)
                .orElseThrow(() -> new EmpleadoNotFoundException(clave));
    }

    @Transactional
    public Empleado actualizar(String clave, EmpleadoUpdateRequest request) {
        Empleado empleado = obtenerPorClave(clave);
        Departamento departamento = departamentoRepository.findById(request.departamentoClave().trim())
                .orElseThrow(() -> new DepartamentoNotFoundException(request.departamentoClave()));

        empleado.setNombre(request.nombre());
        empleado.setDireccion(request.direccion());
        empleado.setTelefono(request.telefono());
        empleado.setDepartamento(departamento);
        return empleadoRepository.save(empleado);
    }

    @Transactional
    public void eliminar(String clave) {
        Empleado empleado = obtenerPorClave(clave);
        empleadoRepository.delete(empleado);
    }
}
