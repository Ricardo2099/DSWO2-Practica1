package com.example.empleados.repository;

import com.example.empleados.domain.SesionAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SesionAccesoRepository extends JpaRepository<SesionAcceso, String> {

    Optional<SesionAcceso> findByTokenAndActivaTrue(String token);

    @Modifying
    @Query("update SesionAcceso s set s.activa = false where s.expiraEn < :now and s.activa = true")
    int deactivateExpiredSessions(@Param("now") LocalDateTime now);

    @Modifying
    @Query("update SesionAcceso s set s.activa = false where s.claveEmpleado = :claveEmpleado and s.activa = true")
    int deactivateActiveSessionsByEmpleado(@Param("claveEmpleado") String claveEmpleado);
}
