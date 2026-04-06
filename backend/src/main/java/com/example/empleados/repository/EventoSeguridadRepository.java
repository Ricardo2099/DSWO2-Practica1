package com.example.empleados.repository;

import com.example.empleados.domain.EventoSeguridad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoSeguridadRepository extends JpaRepository<EventoSeguridad, Long> {
}
