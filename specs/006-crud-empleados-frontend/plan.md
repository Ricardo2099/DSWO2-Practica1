# Implementation Plan: CRUD de Empleados Frontend

**Branch**: `006-crud-empleados-frontend` | **Date**: 2026-03-24 | **Spec**: `specs/006-crud-empleados-frontend/spec.md`
**Input**: Feature specification from `/specs/006-crud-empleados-frontend/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar en el frontend Angular una vista de empleados con listado paginado server-side, alta y baja controladas por rol, selector de departamento y feedback por snackbar. La solucion se estructura en `features/empleados`, consume API en `http://localhost:8080`, integra contratos paginados (`content`, `totalElements`, `totalPages`, `number`, `size`) y aplica visibilidad de acciones segun rol (`ADMIN`/`USER`) respetando que el backend mantiene la autoridad final.

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: TypeScript 5.x + Angular 22 LTS  
**Primary Dependencies**: Angular Router, HttpClient, Reactive Forms, Angular Material, RxJS  
**Storage**: N/A (cliente web; sesion ya gestionada por flujo auth existente)  
**Testing**: Jasmine/Karma (unitarias de servicios/componentes/guards/interceptor)  
**Target Platform**: Navegador web moderno en desktop y movil
**Project Type**: Web application (frontend independiente conectado a backend existente)  
**Performance Goals**: listado p95 < 2s en red local; refresco post-create/delete sin recarga manual  
**Constraints**: base URL fija `http://localhost:8080`; paginacion server-side obligatoria (`page`, `size`); roles `ADMIN`/`USER`  
**Scale/Scope**: 1 feature frontend (`empleados`) + integracion de catalogo `departamentos` + visibilidad por rol

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Frontend scope is explicitly isolated in `/frontend` and backend scope in `/backend`.
- [x] Proposed frontend structure enforces `src/app/core`, `src/app/features/{auth,empleados,departamentos}`, and `src/app/shared`.
- [x] Authentication flow uses `POST /auth/login`, Bearer token injection by interceptor, and session redirection to `/login`.
- [x] Authorization matrix is specified for `ADMIN` (CRUD) and `USER` (read-only) with UI adaptation rules.
- [x] API integration targets `http://localhost:8080` through a single base URL configuration strategy.
- [x] Data model and UX flows preserve "empleado pertenece a departamento" constraints.
- [x] Test strategy includes backend contract/integration coverage and frontend unit coverage for changed guards/interceptors/services.

## Project Structure

### Documentation (this feature)

```text
specs/[###-feature]/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->

```text
backend/
├── src/
│   └── main/java/
└── src/test/

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
├── angular.json
└── package.json

specs/006-crud-empleados-frontend/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
└── contracts/
```

**Structure Decision**: Se adopta estructura de web app con frontend independiente en `frontend/` y backend existente en `backend/`, implementando la feature en `frontend/src/app/features/empleados` y reutilizando `core` para auth/roles/interceptor.

## Post-Design Constitution Check

- [x] Separacion frontend/backend preservada: feature planificada en `frontend/` sin mezclar capas de backend.
- [x] Arquitectura por features preservada: `core`, `features/empleados`, `shared`.
- [x] Flujo de sesion y seguridad respetado: consumo con Bearer y rutas protegidas.
- [x] Matriz de autorizacion aplicada: `ADMIN` crea/elimina, `USER` solo lectura.
- [x] Integracion API consistente: base URL unica y contrato paginado definido.
- [x] Relacion empleado-departamento preservada en formulario y payload.
- [x] Estrategia de pruebas definida para unidades de frontend en artefactos de diseno.

**Gate Result**: PASS

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |
