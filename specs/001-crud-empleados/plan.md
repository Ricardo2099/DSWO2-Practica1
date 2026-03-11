# Implementation Plan: CRUD de Empleados

**Branch**: `001-crud-empleados` | **Date**: 2026-02-26 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-crud-empleados/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar un CRUD de empleados con `clave` autogenerada como PK compuesta lógica en formato `EMP-<autonumero>` y validación de longitud máxima de 100 caracteres para `nombre`, `direccion` y `telefono`, sobre Spring Boot 3 + Java 17, persistencia PostgreSQL y documentación OpenAPI/Swagger. La implementación seguirá arquitectura por capas (controller/service/repository), control de errores consistente y pruebas unitarias, de integración y de contrato.

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.2+, Spring Web, Spring Data JPA, Spring Security (Basic Auth), Flyway, springdoc-openapi, Bean Validation  
**Storage**: PostgreSQL 15+  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, Testcontainers (PostgreSQL)  
**Target Platform**: Linux containers (Docker) en entorno backend
**Project Type**: web-service backend (monolito REST)  
**Performance Goals**: p95 < 2s en consultas por `clave`; p95 < 3s en operaciones de escritura para dataset de hasta 10k empleados  
**Constraints**: `nombre`, `direccion`, `telefono` máximo 100 caracteres; `clave` autogenerada, única, inmutable y con patrón `EMP-[0-9]+`; Swagger obligatorio; Basic Auth obligatorio  
**Scale/Scope**: CRUD interno para administración de empleados (1 entidad principal, 5 endpoints REST)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Research Gate

- ✅ **I. Spring Boot 3 + Java 17 Baseline**: stack fijado en Spring Boot 3.2+ y Java 17.
- ✅ **II. Basic Authentication & Transport Security**: se planifica autenticación Basic para todos los endpoints; TLS tratado en capa de despliegue.
- ✅ **III. PostgreSQL Source of Truth**: única persistencia será PostgreSQL, con migraciones Flyway.
- ✅ **IV. Dockerized Environments & Operations**: diseño contempla ejecución en contenedores y paridad de entorno.
- ✅ **V. API Documentation & Test Discipline**: contratos OpenAPI y estrategia de pruebas (contrato + integración + unitarias) definidos.

**Gate Result**: PASS

## Project Structure

### Documentation (this feature)

```text
specs/001-crud-empleados/
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
├── src/main/java/com/example/empleados/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── mapper/
│   ├── repository/
│   ├── service/
│   └── exception/
├── src/main/resources/
│   ├── db/migration/
│   └── application.yml
├── src/test/java/com/example/empleados/
│   ├── unit/
│   ├── integration/
│   └── contract/
├── Dockerfile
└── pom.xml

docker-compose.yml
```

**Structure Decision**: Se selecciona una estructura de backend único basada en convenciones Spring Boot, con separación por capas y pruebas por tipo (unit/integration/contract).

## Post-Design Constitution Check

- ✅ **I. Stack baseline**: diseño mantiene Java 17 + Spring Boot 3.2+.
- ✅ **II. Security**: contratos requieren Basic Auth para todas las operaciones `/api/v1/empleados`.
- ✅ **III. Data governance**: modelo y contratos usan PostgreSQL + restricciones explícitas de longitud/PK.
- ✅ **IV. Docker parity**: quickstart y contratos se orientan a ejecución docker-compose.
- ✅ **V. Documentation/testing**: se define contrato OpenAPI y pruebas de contrato/integración como criterio de salida.

**Gate Result**: PASS

## Complexity Tracking

No se identifican violaciones a la constitución para esta feature.
