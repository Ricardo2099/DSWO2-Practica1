package com.example.empleados.service;

import com.example.empleados.domain.CredencialEmpleado;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.CredencialEmpleadoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmpleadoUserDetailsService implements UserDetailsService {

    private final EmpleadoRepository empleadoRepository;
    private final CredencialEmpleadoRepository credencialEmpleadoRepository;

    public EmpleadoUserDetailsService(EmpleadoRepository empleadoRepository,
                                      CredencialEmpleadoRepository credencialEmpleadoRepository) {
        this.empleadoRepository = empleadoRepository;
        this.credencialEmpleadoRepository = credencialEmpleadoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Empleado empleado = empleadoRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales invalidas"));

        CredencialEmpleado credencial = credencialEmpleadoRepository.findByClaveEmpleado(empleado.getClave())
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales invalidas"));

        boolean locked = credencial.getBloqueadoHasta() != null
                && credencial.getBloqueadoHasta().isAfter(LocalDateTime.now());

        return User.withUsername(empleado.getCorreo())
                .password(credencial.getContrasenaHash())
                .disabled(!empleado.isHabilitado())
                .accountLocked(locked)
                .roles(empleado.getRol())
                .build();
    }
}
