# Implementation Plan: Suite E2E de Sistema Completo

**Branch**: `008-cypress-e2e-suite` | **Date**: 2026-03-26 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/008-cypress-e2e-suite/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar una suite E2E con Cypress en `frontend/` para validar autenticacion, autorizacion por rol y CRUD principal de departamentos/empleados contra backend real (`http://localhost:8080`), usando login por API para estabilidad, un test dedicado de login UI para cobertura de experiencia, datos unicos por test con limpieza interna y evidencia visual balanceada (video siempre + screenshots en fallos y hitos).

## Technical Context

**Language/Version**: TypeScript 5.9 + Angular 22 LTS (baseline constitucional; si el workspace permanece en 21.x, requiere ADR de excepción antes de implementar) + Cypress 13.x  
**Primary Dependencies**: Cypress, Angular standalone app, HttpClient, Angular Material, JWT Bearer backend auth  
**Storage**: `localStorage` para token/rol durante escenarios autenticados; evidencias en `frontend/cypress/screenshots` y `frontend/cypress/videos`  
**Testing**: Cypress E2E (open/run), login API helper + login UI spec, fixtures y custom commands  
**Target Platform**: Navegadores Chromium/Electron en entorno local con frontend `http://localhost:4200` y backend `http://localhost:8080`
**Project Type**: web application full-stack (suite E2E en frontend consumiendo backend real)  
**Performance Goals**: suite completa < 10 minutos; cero dependencia secuencial entre specs; ejecucion repetible >= 95% sin flakes  
**Constraints**: backend real obligatorio, credenciales por variables de entorno, datos unicos por test con cleanup, evidencia visual obligatoria  
**Scale/Scope**: 3 specs principales (`auth`, `departamentos`, `empleados`), 2 comandos custom de login, configuracion Cypress y soporte compartido

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Research Gate

- [x] Frontend scope is explicitly isolated in `/frontend` and backend scope in `/backend`.
- [x] Proposed frontend structure enforces `src/app/core`, `src/app/features/{auth,empleados,departamentos}`, and `src/app/shared`.
- [x] Authentication flow uses `POST /auth/login`, Bearer token injection by interceptor, and session redirection to `/login`.
- [x] Authorization matrix is specified for `ADMIN` (CRUD) and `USER` (read-only) with UI adaptation rules.
- [x] API integration targets `http://localhost:8080` through a single base URL configuration strategy.
- [x] Data model and UX flows preserve "empleado pertenece a departamento" constraints.
- [x] Test strategy includes backend contract/integration coverage existente y agrega cobertura frontend E2E especifica para auth/roles/CRUD.

**Gate Result**: CONDITIONAL (validar baseline Angular del workspace o registrar ADR de excepción)

## Project Structure

### Documentation (this feature)

```text
specs/008-cypress-e2e-suite/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
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
├── cypress/
│   ├── e2e/
│   ├── fixtures/
│   └── support/
├── src/
│   └── app/
│       ├── core/
│       ├── features/
│       │   ├── auth/
│       │   ├── empleados/
│       │   └── departamentos/
│       └── shared/
├── cypress.config.ts
├── angular.json
└── package.json

specs/008-cypress-e2e-suite/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
└── contracts/
```

**Structure Decision**: Se mantiene la topologia full-stack existente y se agrega/estandariza la estructura Cypress dentro de `frontend/` para separar pruebas E2E (`cypress/e2e`), soporte reusable (`cypress/support`) y datos (`cypress/fixtures`) sin mezclar con unit tests.

## Post-Design Constitution Check

- [x] Frontend y backend permanecen aislados en sus directorios.
- [x] La arquitectura por features no se altera; la suite E2E valida `auth`, `empleados` y `departamentos` desde la UI.
- [x] Se respeta autenticacion por `POST /auth/login` con token Bearer y redireccion de sesion invalida a `/login`.
- [x] La matriz de autorizacion `ADMIN`/`USER` queda cubierta en escenarios positivos y negativos.
- [x] La integracion se mantiene contra `http://localhost:8080` con backend como autoridad final.
- [x] Los flujos E2E preservan la relacion empleado-departamento en creacion y validacion de tabla.
- [x] La estrategia de pruebas conserva cobertura existente backend y agrega cobertura E2E frontend con evidencia visual exigida.

**Gate Result**: CONDITIONAL (cerrar decisión de baseline Angular antes de implementación)

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Workspace Angular 21.x vs baseline 22 LTS | El feature E2E no cambia framework, pero debe respetar gobernanza constitucional | Ignorar la diferencia y mantener PASS produciría un gate inconsistente; se requiere decisión explícita (alineación o ADR) |
