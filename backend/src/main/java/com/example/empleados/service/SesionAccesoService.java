package com.example.empleados.service;

import com.example.empleados.repository.SesionAccesoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SesionAccesoService {

    private final SesionAccesoRepository sesionAccesoRepository;

    public SesionAccesoService(SesionAccesoRepository sesionAccesoRepository) {
        this.sesionAccesoRepository = sesionAccesoRepository;
    }

    @Transactional
    @Scheduled(fixedDelay = 300000)
    public void desactivarSesionesExpiradas() {
        sesionAccesoRepository.deactivateExpiredSessions(LocalDateTime.now());
    }
}
