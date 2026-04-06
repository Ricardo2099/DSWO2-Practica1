package com.example.empleados.config;

import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.SesionAcceso;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.repository.SesionAccesoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class BearerTokenFilter extends OncePerRequestFilter {

    private final SesionAccesoRepository sesionAccesoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ObjectMapper objectMapper;

    public BearerTokenFilter(SesionAccesoRepository sesionAccesoRepository,
                             EmpleadoRepository empleadoRepository,
                             ObjectMapper objectMapper) {
        this.sesionAccesoRepository = sesionAccesoRepository;
        this.empleadoRepository = empleadoRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.equals("/actuator/health")
                || path.equals("/auth/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);
        Optional<SesionAcceso> sessionOpt = sesionAccesoRepository.findByTokenAndActivaTrue(token);
        if (sessionOpt.isEmpty()) {
            writeUnauthorized(response, request.getRequestURI(), "Token invalido");
            return;
        }

        SesionAcceso sesion = sessionOpt.get();
        if (sesion.getExpiraEn().isBefore(LocalDateTime.now())) {
            sesion.setActiva(false);
            sesionAccesoRepository.save(sesion);
            writeUnauthorized(response, request.getRequestURI(), "Token expirado");
            return;
        }

        Empleado empleado = empleadoRepository.findById(sesion.getClaveEmpleado())
                .orElse(null);
        if (empleado == null || !empleado.isHabilitado()) {
            writeUnauthorized(response, request.getRequestURI(), "Sesion invalida");
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                empleado.getCorreo(),
                token,
                List.of(new SimpleGrantedAuthority("ROLE_" + empleado.getRol()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletResponse response, String instance, String detail) throws IOException {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, detail);
        problem.setTitle("No autorizado");
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(instance));

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), problem);
    }
}
