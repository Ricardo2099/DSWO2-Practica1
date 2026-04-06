package com.example.empleados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "sesion_acceso")
public class SesionAcceso {

    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String token;

    @Column(name = "clave_empleado", nullable = false, length = 32)
    private String claveEmpleado;

    @Column(name = "iniciada_en", nullable = false)
    private LocalDateTime iniciadaEn;

    @Column(name = "expira_en", nullable = false)
    private LocalDateTime expiraEn;

    @Column(nullable = false)
    private boolean activa;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClaveEmpleado() {
        return claveEmpleado;
    }

    public void setClaveEmpleado(String claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    public LocalDateTime getIniciadaEn() {
        return iniciadaEn;
    }

    public void setIniciadaEn(LocalDateTime iniciadaEn) {
        this.iniciadaEn = iniciadaEn;
    }

    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(LocalDateTime expiraEn) {
        this.expiraEn = expiraEn;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
