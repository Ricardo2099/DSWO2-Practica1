# Implementation Plan: Autenticacion de Empleados por Correo

**Branch**: `002-empleados-email-auth` | **Date**: 2026-03-12 | **Spec**: `specs/002-empleados-email-auth/spec.md`
**Input**: Feature specification from `/specs/002-empleados-email-auth/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Se migra el mecanismo actual de HTTP Basic con usuario en memoria a autenticacion de empleados por correo y contrasena contra PostgreSQL. La sesion se representa como token opaco UUID almacenado en DB (`sesion_acceso`) con expiracion de 30 minutos, revocacion en logout y regla de una sola sesion activa por empleado.

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.2.8, Spring Security, Spring Data JPA, Flyway  
**Storage**: PostgreSQL  
**Testing**: Spring Boot Test, MockMvc, Testcontainers  
**Target Platform**: Linux server in Docker
**Project Type**: backend web-service  
**Performance Goals**: login valido < 3s, logout efectivo < 2s  
**Constraints**: bloqueo tras 10 intentos por 15 min, sesion expira a 30 min, una sesion activa por empleado  
**Scale/Scope**: sistema academico interno para gestion de empleados

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- PASS: Java 17 + Spring Boot 3.2.x
- PASS: PostgreSQL + Flyway para cambios de esquema
- PASS: Docker compose y Actuator health
- PASS: estrategia de token documentada como UUID opaco persistido

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
# [REMOVE IF UNUSED] Option 1: Single project (DEFAULT)
src/
├── models/
├── services/
├── cli/
└── lib/

tests/
├── contract/
├── integration/
└── unit/

# [REMOVE IF UNUSED] Option 2: Web application (when "frontend" + "backend" detected)
backend/
├── src/
│   ├── models/
│   ├── services/
│   └── api/
└── tests/

frontend/
├── src/
│   ├── components/
│   ├── pages/
│   └── services/
└── tests/

# [REMOVE IF UNUSED] Option 3: Mobile + API (when "iOS/Android" detected)
api/
└── [same as backend above]

ios/ or android/
└── [platform-specific structure: feature modules, UI flows, platform tests]
```

**Structure Decision**: Se conserva la estructura backend existente en `backend/src/main/java/com/example/empleados` y `backend/src/test/java/com/example/empleados`, agregando nuevas capas de auth en `config`, `controller`, `domain`, `repository` y `service`.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |
