# Tasks: Autenticacion de Empleados por Correo

**Feature Branch**: `002-empleados-email-auth`
**Spec**: [spec.md](spec.md)
**Stack**: Java 17 Â· Spring Boot 3.2.8 Â· Spring Security Â· PostgreSQL Â· Flyway Â· Maven Â· Testcontainers

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Puede ejecutarse en paralelo (archivos distintos, sin dependencias incompletas)
- **[Story]**: Historia de usuario a la que pertenece la tarea (US1 / US2 / US3)

---

## Phase 1: Setup (Infraestructura Compartida)

**Purpose**: Confirmar linea base de dependencias. No se necesita nueva libreria: BCrypt ya esta disponible via `spring-boot-starter-security`; los tokens de sesion son UUIDs opacos persistidos en DB.

- [X] T001 Confirmar que `BCryptPasswordEncoder` esta disponible en `spring-boot-starter-security` y documentar estrategia de token (UUID opaco almacenado en DB) en `specs/002-empleados-email-auth/plan.md`

---

## Phase 2: Foundational (Prerequisitos Bloqueantes)

**Purpose**: Migraciones, entidades de dominio, repositorios y DTOs que TODAS las historias de usuario necesitan.

**âš ï¸ CRITICO**: Ninguna historia de usuario puede comenzar hasta que esta fase este completa.

- [X] T002 Crear migracion Flyway `V2__add_auth_fields_empleados.sql` â€” agregar `correo VARCHAR(255) UNIQUE NOT NULL` y `habilitado BOOLEAN NOT NULL DEFAULT TRUE` a tabla `empleados` en `backend/src/main/resources/db/migration/V2__add_auth_fields_empleados.sql`
- [X] T003 Crear migracion Flyway `V3__create_credencial_empleado.sql` â€” tabla `credencial_empleado(clave_empleado VARCHAR(32) PK FK empleados.clave, contrasena_hash VARCHAR(255) NOT NULL, intentos_fallidos INT NOT NULL DEFAULT 0, bloqueado_hasta TIMESTAMP, updated_at TIMESTAMP NOT NULL)` en `backend/src/main/resources/db/migration/V3__create_credencial_empleado.sql`
- [X] T004 Crear migracion Flyway `V4__create_sesion_acceso.sql` â€” tabla `sesion_acceso(token VARCHAR(36) PK, clave_empleado VARCHAR(32) NOT NULL FK empleados.clave, iniciada_en TIMESTAMP NOT NULL, expira_en TIMESTAMP NOT NULL, activa BOOLEAN NOT NULL DEFAULT TRUE)` en `backend/src/main/resources/db/migration/V4__create_sesion_acceso.sql`
- [X] T005 Crear migracion Flyway `V5__create_evento_seguridad.sql` â€” tabla `evento_seguridad(id BIGSERIAL PK, clave_empleado VARCHAR(32), tipo VARCHAR(32) NOT NULL, ocurrido_en TIMESTAMP NOT NULL, detalle TEXT)` en `backend/src/main/resources/db/migration/V5__create_evento_seguridad.sql`
- [X] T006 [P] Actualizar `Empleado.java` â€” agregar campos `correo` (unique, not null) y `habilitado` (not null, default true) con sus getters/setters en `backend/src/main/java/com/example/empleados/domain/Empleado.java`
- [X] T007 [P] Crear entidad `CredencialEmpleado.java` â€” campos `claveEmpleado` (PK, FK), `contrasenaHash`, `intentosFallidos`, `bloqueadoHasta`, `updatedAt` en `backend/src/main/java/com/example/empleados/domain/CredencialEmpleado.java`
- [X] T008 [P] Crear entidad `SesionAcceso.java` â€” campos `token` (PK UUID), `claveEmpleado` (FK), `iniciadaEn`, `expiraEn`, `activa` en `backend/src/main/java/com/example/empleados/domain/SesionAcceso.java`
- [X] T009 [P] Crear entidad `EventoSeguridad.java` â€” campos `id` (BIGSERIAL PK), `claveEmpleado`, `tipo` (enum: LOGIN_OK, LOGIN_FAIL, LOGOUT, UNLOCK), `ocurridoEn`, `detalle` en `backend/src/main/java/com/example/empleados/domain/EventoSeguridad.java`
- [X] T010 [P] Crear `CredencialEmpleadoRepository.java` â€” `JpaRepository<CredencialEmpleado, String>` con metodo `findByClaveEmpleado` en `backend/src/main/java/com/example/empleados/repository/CredencialEmpleadoRepository.java`
- [X] T011 [P] Crear `SesionAccesoRepository.java` â€” `JpaRepository<SesionAcceso, String>` con metodo `findByTokenAndActivaTrue` y `deactivateExpiredSessions` en `backend/src/main/java/com/example/empleados/repository/SesionAccesoRepository.java`
- [X] T012 [P] Crear `EventoSeguridadRepository.java` â€” `JpaRepository<EventoSeguridad, Long>` en `backend/src/main/java/com/example/empleados/repository/EventoSeguridadRepository.java`
- [X] T013 [P] Crear `LoginRequest.java` DTO â€” campos `correo` (`@Email @NotBlank`) y `contrasena` (`@NotBlank`) en `backend/src/main/java/com/example/empleados/dto/LoginRequest.java`
- [X] T014 [P] Crear `LoginResponse.java` DTO â€” campos `token` (String), `expiraEn` (LocalDateTime) en `backend/src/main/java/com/example/empleados/dto/LoginResponse.java`

**Checkpoint**: Migraciones, entidades, repositorios y DTOs completos â€” implementacion por historia de usuario puede comenzar.

---

## Phase 3: User Story 1 â€” Iniciar sesion con correo y contrasena (Priority: P1) ðŸŽ¯ MVP

**Goal**: Empleados pueden autenticarse con correo y contrasena validos; credenciales invalidas son rechazadas sin revelar cual campo fallo; la cuenta se bloquea tras 10 intentos fallidos durante 15 minutos y requiere desbloqueo automatico o manual de administrador.

**Independent Test**: Registrar empleado activo con correo y contrasena (politica: 8+ chars, 1 mayuscula, 1 minuscula, 1 numero), llamar `POST /auth/login` con credenciales correctas y verificar que la respuesta devuelve un `token` de sesion valido.

### Implementacion para User Story 1

- [X] T015 [US1] Implementar `EmpleadoUserDetailsService.java` â€” `UserDetailsService` que carga empleado por correo desde DB, lanza `DisabledException` si `habilitado=false` y `LockedException` si `bloqueado_hasta` es futuro en `backend/src/main/java/com/example/empleados/service/EmpleadoUserDetailsService.java`
- [X] T016 [US1] Implementar `AuthService.java` â€” metodos `login()` (valida politica de contrasena, emite `SesionAcceso`, registra evento), `registerFailedAttempt()` (incrementa contador, bloquea a los 10 intentos), `unlockAccount()` (limpia `bloqueado_hasta` e `intentosFallidos`) en `backend/src/main/java/com/example/empleados/service/AuthService.java`
- [X] T017 [P] [US1] Implementar `EventoSeguridadService.java` â€” metodo `registrar(tipo, claveEmpleado, detalle)` que persiste `EventoSeguridad` de forma transaccional en `backend/src/main/java/com/example/empleados/service/EventoSeguridadService.java`
- [X] T018 [US1] Crear `AuthController.java` â€” endpoint `POST /auth/login` que acepta `LoginRequest`, retorna `LoginResponse` con token y tiempo de expiracion; manejo de errores con `ProblemDetail` en `backend/src/main/java/com/example/empleados/controller/AuthController.java`
- [X] T019 [US1] Crear `BearerTokenFilter.java` â€” `OncePerRequestFilter` que extrae `Authorization: Bearer <token>`, valida `SesionAcceso` en DB (activa + no expirada), establece `SecurityContext`; responde 401 si invalido en `backend/src/main/java/com/example/empleados/config/BearerTokenFilter.java`
- [X] T020 [US1] Actualizar `SecurityConfig.java` â€” reemplazar `InMemoryUserDetailsManager` con `EmpleadoUserDetailsService`, registrar `BearerTokenFilter` antes de `UsernamePasswordAuthenticationFilter`, declarar `/auth/login` como publica, deshabilitar HTTP Basic en `backend/src/main/java/com/example/empleados/config/SecurityConfig.java`
- [X] T021 [US1] Agregar bean `PasswordEncoder` (`BCryptPasswordEncoder`) en `SecurityConfig.java` y usado en `AuthService` para hashear y verificar contrasenas en `backend/src/main/java/com/example/empleados/config/SecurityConfig.java`
- [X] T022 [US1] Actualizar `application.yml` â€” eliminar bloque `app.security.basic`; agregar `app.auth.session-timeout-minutes: 30`, `app.auth.max-failed-attempts: 10`, `app.auth.lock-duration-minutes: 15` en `backend/src/main/resources/application.yml`

**Checkpoint**: `POST /auth/login` funcional â€” credenciales validas emiten token, invalidas devuelven 401 generico, bloqueo tras 10 intentos fallidos. US1 completo e independientemente testeable. **MVP alcanzado.**

---

## Phase 4: User Story 2 â€” Proteger acciones solo para autenticados (Priority: P2)

**Goal**: Todos los endpoints CRUD existentes requieren token de sesion Bearer valido; solicitudes sin token o con token expirado/invalido reciben 401 con estructura `ProblemDetail` uniforme.

**Independent Test**: Con token valido de US1, llamar `GET /empleados` â†’ 200; sin token o con token invalido â†’ 401 con cuerpo `ProblemDetail`.

### Implementacion para User Story 2

- [X] T023 [US2] Actualizar `GlobalExceptionHandler.java` â€” manejar `AuthenticationException` y `AccessDeniedException` devolviendo `ProblemDetail` con campos `type`, `title`, `status`, `detail`, `instance` en `backend/src/main/java/com/example/empleados/exception/GlobalExceptionHandler.java`
- [X] T024 [US2] Configurar `AuthenticationEntryPoint` y `AccessDeniedHandler` en `SecurityConfig.java` â€” emitir respuesta JSON `ProblemDetail` 401/403 sin redireccion en `backend/src/main/java/com/example/empleados/config/SecurityConfig.java`
- [X] T025 [US2] Agregar comprobacion de expiracion lazy en `BearerTokenFilter.java` â€” si `expira_en < now()` marcar sesion como inactiva y devolver 401, sin esperar job periodico en `backend/src/main/java/com/example/empleados/config/BearerTokenFilter.java`
- [X] T026 [US2] Crear `SesionAccesoService.java` con `@Scheduled` â€” desactivar sesiones expiradas en DB cada 5 minutos; habilitar `@EnableScheduling` en la clase principal o en `SecurityConfig` en `backend/src/main/java/com/example/empleados/service/SesionAccesoService.java`

**Checkpoint**: CRUD de empleados protegido por token Bearer. Solicitudes no autenticadas reciben 401 uniforme. US2 completo.

---

## Phase 5: User Story 3 â€” Cerrar sesion de forma segura (Priority: P3)

**Goal**: Empleado autenticado puede cerrar sesion invalidando su token; reusar ese token despues del cierre devuelve 401.

**Independent Test**: Llamar `POST /auth/logout` con token valido â†’ 204; llamar `GET /empleados` con el mismo token â†’ 401.

### Implementacion para User Story 3

- [X] T027 [US3] Agregar metodo `logout()` en `AuthService.java` â€” marca `SesionAcceso.activa = false` para el token dado, es idempotente si la sesion ya estaba cerrada, registra evento LOGOUT en `backend/src/main/java/com/example/empleados/service/AuthService.java`
- [X] T028 [US3] Agregar endpoint `POST /auth/logout` en `AuthController.java` â€” requiere autenticacion Bearer, llama `authService.logout(token)`, devuelve 204 No Content en `backend/src/main/java/com/example/empleados/controller/AuthController.java`
- [X] T029 [US3] Actualizar `SecurityConfig.java` â€” asegurar que `/auth/logout` requiera autenticacion valida (token Bearer) y no este en lista de rutas publicas en `backend/src/main/java/com/example/empleados/config/SecurityConfig.java`

**Checkpoint**: Logout invalida token de inmediato. Reuso del token post-logout devuelve 401. US3 completo.

---

## Phase 6: Polish y Preocupaciones Transversales

**Purpose**: Desbloqueo manual de administrador, migracion de semilla, documentacion OpenAPI y limpieza de variables de entorno legacy.

- [X] T030 [P] Crear endpoint `POST /admin/empleados/{clave}/unlock` en `AuthController.java` â€” desbloquea manualmente `CredencialEmpleado` (limpia `bloqueado_hasta` e `intentosFallidos`), registra evento UNLOCK; requiere rol ADMIN en `backend/src/main/java/com/example/empleados/controller/AuthController.java`
- [X] T031 [P] Crear migracion `V6__seed_admin_credencial.sql` â€” insertar empleado administrador inicial con correo y contrasena BCrypt para facilitar onboarding en `backend/src/main/resources/db/migration/V6__seed_admin_credencial.sql`
- [X] T032 [P] Actualizar `OpenApiConfig.java` â€” agregar `SecurityScheme` Bearer HTTP, documentar endpoints `/auth/login`, `/auth/logout` y `/admin/empleados/{clave}/unlock` en `backend/src/main/java/com/example/empleados/config/OpenApiConfig.java`
- [X] T033 [P] Actualizar `docker-compose.yml` â€” eliminar vars `APP_SECURITY_BASIC_USERNAME` y `APP_SECURITY_BASIC_PASSWORD`; agregar `APP_AUTH_SESSION_TIMEOUT_MINUTES`, `APP_AUTH_MAX_FAILED_ATTEMPTS`, `APP_AUTH_LOCK_DURATION_MINUTES` en `docker-compose.yml`

---

## Grafo de Dependencias (Orden de Completacion por Historia)

```
Phase 1 (Setup)
  â””â”€â”€ Phase 2 (Foundational)
        â””â”€â”€ Phase 3 (US1 - Login) â† MVP
              â”œâ”€â”€ Phase 4 (US2 - Proteger endpoints)  â† paralelo con US3
              â””â”€â”€ Phase 5 (US3 - Logout)              â† paralelo con US2
                    â””â”€â”€ Phase 6 (Polish)
```

US2 y US3 son independientes entre si una vez que US1 esta completo â€” dos personas pueden trabajar simultaneamente.

## Ejemplos de Ejecucion en Paralelo

| Grupo Paralelizable | Tareas |
|---------------------|--------|
| Migraciones Flyway | T002, T003, T004, T005 (archivos distintos) |
| Entidades de dominio | T006, T007, T008, T009 (archivos distintos) |
| Repositorios y DTOs | T010, T011, T012, T013, T014 (archivos distintos) |
| US2 + US3 (tras US1) | T023â€“T026 en paralelo con T027â€“T029 |
| Phase 6 | T030, T031, T032, T033 todos independientes |

## Estrategia de Implementacion

1. **MVP primero**: Completar Phases 1â€“3 (T001â€“T022) para tener `POST /auth/login` funcional.
2. **Entrega incremental**: Phase 4 (US2) reutiliza el token ya emitido; Phase 5 (US3) solo agrega la ruta de logout.
3. **Compatibilidad hacia atras**: Los endpoints CRUD ya tienen `anyRequest().authenticated()` en `SecurityConfig`; el cambio en Phase 3 solo sustituye el mecanismo de autenticacion (de HTTP Basic a Bearer), no el modelo de permisos.
4. **Politica de contrasena**: Validar en `AuthService.validatePasswordPolicy()` con regex `^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$` antes de hashear o autenticar.

---

## Resumen

| Seccion | Tareas |
|---------|--------|
| Setup (Phase 1) | 1 |
| Foundational (Phase 2) | 13 |
| US1 â€” Login (Phase 3) | 8 |
| US2 â€” Proteccion (Phase 4) | 4 |
| US3 â€” Logout (Phase 5) | 3 |
| Polish (Phase 6) | 4 |
| **Total** | **33** |

**Oportunidades paralelas identificadas**: 5 grupos  
**Criterio de prueba independiente por historia**: definido en cada fase  
**Alcance MVP sugerido**: Phases 1â€“3 (T001â€“T022)
