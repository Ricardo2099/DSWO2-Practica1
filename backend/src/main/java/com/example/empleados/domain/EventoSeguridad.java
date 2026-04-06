package com.example.empleados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "evento_seguridad")
public class EventoSeguridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clave_empleado", length = 32)
    private String claveEmpleado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SecurityEventType tipo;

    @Column(name = "ocurrido_en", nullable = false)
    private LocalDateTime ocurridoEn;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    public Long getId() {
        return id;
    }

    public String getClaveEmpleado() {
        return claveEmpleado;
    }

    public void setClaveEmpleado(String claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    public SecurityEventType getTipo() {
        return tipo;
    }

    public void setTipo(SecurityEventType tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getOcurridoEn() {
        return ocurridoEn;
    }

    public void setOcurridoEn(LocalDateTime ocurridoEn) {
        this.ocurridoEn = ocurridoEn;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
