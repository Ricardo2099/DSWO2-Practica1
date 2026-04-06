# Implementation Plan: CRUD de Departamentos Frontend

**Branch**: `007-crud-departamentos-frontend` | **Date**: 2026-03-25 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/007-crud-departamentos-frontend/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar en Angular 22 una pantalla de departamentos con CRUD completo contra `http://localhost:8080`, control de permisos por rol (`ADMIN`/`USER`), formulario con modo crear/editar, notificaciones por snackbar, manejo de conflicto de concurrencia `409` con recarga y confirmación de reintento, y paginación en cliente para el listado.

## Technical Context

**Language/Version**: TypeScript 5.x + Angular 22 LTS  
**Primary Dependencies**: Angular standalone APIs, Angular Material (table/paginator/snackbar), RxJS, HttpClient  
**Storage**: N/A en frontend (solo estado en memoria y sesión/token en `localStorage`)  
**Testing**: Jasmine + Karma (unit tests), Angular TestBed, HttpClientTestingModule  
**Target Platform**: Navegadores modernos en desktop y móvil (frontend SPA)
**Project Type**: web application frontend (cliente Angular desacoplado de backend)  
**Performance Goals**: listado visible en < 2s en red local; actualización de UI tras operaciones exitosas sin recarga completa  
**Constraints**: base URL única `http://localhost:8080`; `USER` solo lectura; `clave` inmutable en edición; bloqueo de borrado con asociados; manejo 409 con recarga+confirmación  
**Scale/Scope**: 1 feature frontend nueva/extendida (`departamentos`) con 1 vista CRUD, 1 servicio de API y reglas de autorización por rol

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Research Gate

- [x] Frontend scope is explicitly isolated in `/frontend` and backend scope in `/backend`.
- [x] Proposed frontend structure enforces `src/app/core`, `src/app/features/{auth,empleados,departamentos}`, and `src/app/shared`.
- [x] Authentication flow uses `POST /auth/login`, Bearer token injection by interceptor, and session redirection to `/login`.
- [x] Authorization matrix is specified for `ADMIN` (CRUD) and `USER` (read-only) with UI adaptation rules.
- [x] API integration targets `http://localhost:8080` through a single base URL configuration strategy.
- [x] Data model and UX flows preserve "empleado pertenece a departamento" constraints via bloqueo de eliminación con empleados asociados.
- [x] Test strategy includes backend contract/integration coverage (existente) and frontend unit coverage for changed service/component/guards-interceptor si aplican.

**Gate Result**: PASS

## Project Structure

### Documentation (this feature)

```text
specs/007-crud-departamentos-frontend/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md        # Puede completarse/actualizarse durante /speckit.tasks
├── contracts/           # Puede completarse/actualizarse durante /speckit.tasks
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
├── angular.json
└── package.json

specs/007-crud-departamentos-frontend/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
└── contracts/

docker-compose.yml
```

**Structure Decision**: Se mantiene topología full-stack separada. La implementación de este feature se concentra en `frontend/src/app/features/departamentos`, reutilizando `core` para autenticación/interceptor/base URL y `shared` para componentes comunes de tabla/formulario si ya existen.

## Post-Design Constitution Check

- [x] Frontend y backend continúan aislados en sus directorios sin mezclar responsabilidades.
- [x] El diseño mantiene arquitectura por features y ubica CRUD de departamentos en `features/departamentos`.
- [x] Se preserva autenticación por Bearer con guard/interceptor y redirección a `/login` en sesión inválida.
- [x] Matriz de rol aplicada: `ADMIN` puede escribir, `USER` solo consulta, con adaptación explícita de UI.
- [x] Integración API unificada contra `http://localhost:8080` desde estrategia de base URL única.
- [x] Modelo y flujos respetan regla de negocio: no borrar departamento con empleados asociados.
- [x] Se documenta estrategia de pruebas para servicio/componente/seguridad y casos negativos de token/rol.

**Gate Result**: PASS

## Complexity Tracking

No se identifican violaciones a la constitución para esta feature.
