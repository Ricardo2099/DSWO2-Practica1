package com.example.empleados.service;

import com.example.empleados.domain.CredencialEmpleado;
import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.SecurityEventType;
import com.example.empleados.domain.SesionAcceso;
import com.example.empleados.dto.LoginRequest;
import com.example.empleados.dto.LoginResponse;
import com.example.empleados.repository.CredencialEmpleadoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.repository.SesionAccesoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final EmpleadoRepository empleadoRepository;
    private final CredencialEmpleadoRepository credencialEmpleadoRepository;
    private final SesionAccesoRepository sesionAccesoRepository;
    private final EventoSeguridadService eventoSeguridadService;
    private final PasswordEncoder passwordEncoder;

    private final int sessionTimeoutMinutes;
    private final int maxFailedAttempts;
    private final int lockDurationMinutes;

    public AuthService(EmpleadoRepository empleadoRepository,
                       CredencialEmpleadoRepository credencialEmpleadoRepository,
                       SesionAccesoRepository sesionAccesoRepository,
                       EventoSeguridadService eventoSeguridadService,
                       PasswordEncoder passwordEncoder,
                       @Value("${app.auth.session-timeout-minutes:30}") int sessionTimeoutMinutes,
                       @Value("${app.auth.max-failed-attempts:10}") int maxFailedAttempts,
                       @Value("${app.auth.lock-duration-minutes:15}") int lockDurationMinutes) {
        this.empleadoRepository = empleadoRepository;
        this.credencialEmpleadoRepository = credencialEmpleadoRepository;
        this.sesionAccesoRepository = sesionAccesoRepository;
        this.eventoSeguridadService = eventoSeguridadService;
        this.passwordEncoder = passwordEncoder;
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
        this.maxFailedAttempts = maxFailedAttempts;
        this.lockDurationMinutes = lockDurationMinutes;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Empleado empleado = empleadoRepository.findByCorreoIgnoreCase(request.correo())
                .orElseThrow(this::invalidCredentials);

        if (!empleado.isHabilitado()) {
            eventoSeguridadService.registrar(SecurityEventType.LOGIN_FAIL, empleado.getClave(), "Cuenta deshabilitada");
            throw new DisabledException("Cuenta deshabilitada");
        }

        CredencialEmpleado credencial = credencialEmpleadoRepository.findByClaveEmpleado(empleado.getClave())
                .orElseThrow(this::invalidCredentials);

        releaseLockIfExpired(credencial);

        if (isBlocked(credencial)) {
            eventoSeguridadService.registrar(SecurityEventType.LOGIN_FAIL, empleado.getClave(), "Cuenta bloqueada");
            throw new LockedException("Cuenta bloqueada temporalmente");
        }

        if (!passwordEncoder.matches(request.contrasena(), credencial.getContrasenaHash())) {
            registerFailedAttempt(credencial, empleado.getClave(), "Contrasena incorrecta");
            throw invalidCredentials();
        }

        credencial.setIntentosFallidos(0);
        credencial.setBloqueadoHasta(null);
        credencialEmpleadoRepository.save(credencial);

        // Mantiene una sola sesion activa por empleado.
        sesionAccesoRepository.deactivateActiveSessionsByEmpleado(empleado.getClave());

        LocalDateTime now = LocalDateTime.now();
        SesionAcceso sesion = new SesionAcceso();
        sesion.setToken(UUID.randomUUID().toString());
        sesion.setClaveEmpleado(empleado.getClave());
        sesion.setIniciadaEn(now);
        sesion.setExpiraEn(now.plusMinutes(sessionTimeoutMinutes));
        sesion.setActiva(true);
        sesionAccesoRepository.save(sesion);

        eventoSeguridadService.registrar(SecurityEventType.LOGIN_OK, empleado.getClave(), "Inicio de sesion exitoso");
        return new LoginResponse(sesion.getToken(), sesion.getExpiraEn());
    }

    @Transactional
    public void logout(String token) {
        sesionAccesoRepository.findByTokenAndActivaTrue(token)
                .ifPresent(sesion -> {
                    sesion.setActiva(false);
                    sesionAccesoRepository.save(sesion);
                    eventoSeguridadService.registrar(SecurityEventType.LOGOUT, sesion.getClaveEmpleado(), "Cierre de sesion");
                });
    }

    @Transactional
    public void unlockAccount(String claveEmpleado, String actor) {
        CredencialEmpleado credencial = credencialEmpleadoRepository.findByClaveEmpleado(claveEmpleado)
                .orElseThrow(() -> new BadCredentialsException("Cuenta no encontrada"));

        credencial.setIntentosFallidos(0);
        credencial.setBloqueadoHasta(null);
        credencialEmpleadoRepository.save(credencial);
        eventoSeguridadService.registrar(SecurityEventType.UNLOCK, claveEmpleado, "Desbloqueada por " + actor);
    }

    private void registerFailedAttempt(CredencialEmpleado credencial, String claveEmpleado, String detail) {
        int intentos = credencial.getIntentosFallidos() + 1;
        credencial.setIntentosFallidos(intentos);
        if (intentos >= maxFailedAttempts) {
            credencial.setBloqueadoHasta(LocalDateTime.now().plusMinutes(lockDurationMinutes));
        }
        credencialEmpleadoRepository.save(credencial);
        eventoSeguridadService.registrar(SecurityEventType.LOGIN_FAIL, claveEmpleado, detail);
    }

    private void releaseLockIfExpired(CredencialEmpleado credencial) {
        if (credencial.getBloqueadoHasta() != null && credencial.getBloqueadoHasta().isBefore(LocalDateTime.now())) {
            credencial.setBloqueadoHasta(null);
            credencial.setIntentosFallidos(0);
            credencialEmpleadoRepository.save(credencial);
        }
    }

    private boolean isBlocked(CredencialEmpleado credencial) {
        return credencial.getBloqueadoHasta() != null && credencial.getBloqueadoHasta().isAfter(LocalDateTime.now());
    }

    private BadCredentialsException invalidCredentials() {
        return new BadCredentialsException("Credenciales invalidas");
    }
}
