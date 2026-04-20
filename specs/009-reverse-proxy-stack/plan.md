# Implementation Plan: Reverse Proxy En Stack Docker

**Branch**: `009-reverse-proxy-stack` | **Date**: 2026-04-09 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/009-reverse-proxy-stack/spec.md`

## Summary

Implementar un servicio `reverse-proxy` con Nginx en `docker-compose.yml` como unico punto de entrada HTTP, enrutando rutas API (`/api`, `/auth`, `/swagger-ui`, `/v3/api-docs`) hacia backend Spring Boot y rutas de aplicacion hacia frontend Angular, con preservacion de headers de forwarding, compatibilidad Bearer, healthchecks de proxy/backend y aislamiento de red interna con exposicion publica minima.

## Technical Context

**Language/Version**: Java 17 + Spring Boot 3.x (backend), TypeScript + Angular (frontend), Nginx 1.27 (proxy), Docker Compose v2  
**Primary Dependencies**: Spring Security Bearer auth, PostgreSQL 15, Angular HttpClient interceptor, Nginx reverse proxy, Docker healthcheck  
**Storage**: PostgreSQL en contenedor `db` con volumen `postgres_data`  
**Testing**: Backend Maven tests existentes, validacion operativa por `docker compose`, `curl`/PowerShell HTTP checks, smoke checks de enrutamiento  
**Target Platform**: Entorno local Docker Desktop en Windows/Linux/macOS con acceso HTTP en `http://localhost`  
**Project Type**: web application full-stack containerizada (frontend + backend + db + reverse proxy)  
**Performance Goals**: Stack operativo tras build limpio en < 10 minutos; rutas criticas responden HTTP 200 en 3 ejecuciones consecutivas  
**Constraints**: Un solo endpoint publico; `api`, `frontend`, `db` internos por defecto; conservar headers `Host`, `X-Real-IP`, `X-Forwarded-*`; no romper auth Bearer  
**Scale/Scope**: 4 servicios en compose (`db`, `api`, `frontend`, `reverse-proxy`), 1 red interna dedicada, 1 override opcional para debug DB

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Research Gate

- [x] Frontend scope is explicitly isolated in `/frontend` and backend scope in `/backend`.
- [x] Proposed frontend structure enforces `src/app/core`, `src/app/features/{auth,empleados,departamentos}`, and `src/app/shared`.
- [x] Authentication flow uses `POST /auth/login`, Bearer token injection by interceptor, and session redirection to `/login`.
- [x] Authorization matrix is specified for `ADMIN` (CRUD) and `USER` (read-only) with UI adaptation rules.
- [ ] API integration targets `http://localhost:8080` through a single base URL configuration strategy.
- [x] Data model and UX flows preserve "empleado pertenece a departamento" constraints.
- [x] Test strategy includes backend contract/integration coverage and frontend unit coverage for changed guards/interceptors/services.

**Gate Result**: CONDITIONAL (la feature requiere origen unificado por proxy en `http://localhost`; se registra excepcion justificada en Complexity Tracking)

## Project Structure

### Documentation (this feature)

```text
specs/009-reverse-proxy-stack/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── reverse-proxy-routing-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/
│   ├── main/
│   └── test/
└── pom.xml

frontend/
├── src/
│   └── app/
│       ├── core/
│       ├── features/
│       │   ├── auth/
│       │   ├── empleados/
│       │   └── departamentos/
│       └── shared/
├── public/
└── package.json

docker-compose.yml
docker-compose.debug-db.yml (nuevo, opcional)
reverse-proxy/
└── nginx.conf (nuevo)
```

**Structure Decision**: Se mantiene la topologia full-stack existente (`backend/` y `frontend/`) y se agrega un directorio de infraestructura `reverse-proxy/` para configuracion Nginx versionada; compose base publica solo proxy y usa override opcional para debug de DB.

## Post-Design Constitution Check

- [x] Frontend y backend permanecen aislados en sus directorios.
- [x] La arquitectura por features de frontend no se altera; solo se ajusta estrategia de base URL.
- [x] Se preserva flujo de autenticacion por `POST /auth/login` con Bearer token en interceptor.
- [x] La matriz de autorizacion `ADMIN`/`USER` permanece en backend sin delegarla al proxy.
- [ ] La constitucion fija base URL directa `http://localhost:8080`; el diseno adopta origen unificado `http://localhost` via reverse proxy.
- [x] La relacion empleado-departamento no cambia ni se degrada por la capa proxy.
- [x] La estrategia de pruebas incluye checks operativos (healthcheck+HTTP) y cobertura backend/frontend existente.

**Gate Result**: CONDITIONAL (excepcion documentada para base URL unificada; no hay violaciones de seguridad, separacion de capas ni contratos funcionales)

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Constitution item de URL base `http://localhost:8080` | El objetivo funcional exige endpoint unico para eliminar CORS y exponer solo el proxy | Mantener URL directa a backend impediria cumplir FR-001, FR-006 y SC-004 del feature |

## Arquitectura Final Del Stack

```text
Cliente HTTP (browser/curl)
	|
	v
reverse-proxy (nginx:80) [unico puerto publico]
   |-- /api/*, /auth/*, /swagger-ui*, /v3/api-docs* --> api:8080 (Spring Boot)
   \-- / y rutas no API --> frontend:80 (Angular static + SPA fallback)

Servicios internos en red `internal-net`:
- db (PostgreSQL)
- api (Spring Boot)
- frontend (Nginx static)
- reverse-proxy (Nginx)

Exposicion externa en compose base:
- permitido: reverse-proxy:80
- bloqueado: db/api/frontend

Debug local opcional:
- docker-compose.debug-db.yml publica db:5432 solo cuando se usa override
```
