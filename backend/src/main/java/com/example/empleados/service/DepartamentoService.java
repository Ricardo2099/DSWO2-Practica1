package com.example.empleados.service;

import com.example.empleados.domain.Departamento;
import com.example.empleados.dto.DepartamentoCreateRequest;
import com.example.empleados.dto.DepartamentoUpdateRequest;
import com.example.empleados.exception.DepartamentoDuplicadoException;
import com.example.empleados.exception.DepartamentoInUseException;
import com.example.empleados.exception.DepartamentoNotFoundException;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository,
                               EmpleadoRepository empleadoRepository) {
        this.departamentoRepository = departamentoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional
    public Departamento crear(DepartamentoCreateRequest request) {
        String clave = request.clave().trim();
        if (departamentoRepository.existsById(clave)) {
            throw new DepartamentoDuplicadoException(clave);
        }

        Departamento departamento = new Departamento();
        departamento.setClave(clave);
        departamento.setNombre(request.nombre().trim());
        return departamentoRepository.save(departamento);
    }

    @Transactional(readOnly = true)
    public List<Departamento> listar() {
        return departamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Departamento obtenerPorClave(String clave) {
        return departamentoRepository.findById(clave)
                .orElseThrow(() -> new DepartamentoNotFoundException(clave));
    }

    @Transactional
    public Departamento actualizar(String clave, DepartamentoUpdateRequest request) {
        Departamento departamento = obtenerPorClave(clave);
        departamento.setNombre(request.nombre().trim());
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public void eliminar(String clave) {
        Departamento departamento = obtenerPorClave(clave);
        if (empleadoRepository.countByDepartamento_Clave(clave) > 0) {
            throw new DepartamentoInUseException(clave);
        }
        departamentoRepository.delete(departamento);
    }
}
