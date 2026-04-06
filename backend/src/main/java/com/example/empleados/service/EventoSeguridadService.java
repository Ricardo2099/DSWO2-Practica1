package com.example.empleados.service;

import com.example.empleados.domain.EventoSeguridad;
import com.example.empleados.domain.SecurityEventType;
import com.example.empleados.repository.EventoSeguridadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EventoSeguridadService {

    private final EventoSeguridadRepository eventoSeguridadRepository;

    public EventoSeguridadService(EventoSeguridadRepository eventoSeguridadRepository) {
        this.eventoSeguridadRepository = eventoSeguridadRepository;
    }

    @Transactional
    public void registrar(SecurityEventType tipo, String claveEmpleado, String detalle) {
        EventoSeguridad evento = new EventoSeguridad();
        evento.setTipo(tipo);
        evento.setClaveEmpleado(claveEmpleado);
        evento.setDetalle(detalle);
        evento.setOcurridoEn(LocalDateTime.now());
        eventoSeguridadRepository.save(evento);
    }
}
