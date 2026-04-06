package com.example.empleados.repository;

import com.example.empleados.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, String> {

	Optional<Empleado> findByCorreoIgnoreCase(String correo);

	long countByDepartamento_Clave(String clave);
}
