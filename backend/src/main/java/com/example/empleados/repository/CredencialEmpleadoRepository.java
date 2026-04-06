package com.example.empleados.repository;

import com.example.empleados.domain.CredencialEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredencialEmpleadoRepository extends JpaRepository<CredencialEmpleado, String> {

    Optional<CredencialEmpleado> findByClaveEmpleado(String claveEmpleado);
}
