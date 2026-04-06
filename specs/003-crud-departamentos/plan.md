# Implementation Plan: CRUD de Departamentos

**Branch**: `003-crud-departamentos` | **Date**: 2026-03-17 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/003-crud-departamentos/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar un CRUD de departamentos con `clave` provista por usuario como PK de negocio y `nombre` con máximo 50 caracteres, incorporando relación uno-a-muchos con empleados (cada empleado pertenece a un solo departamento). La solución usará Spring Boot 3 + Java 17, PostgreSQL con Flyway para migraciones, seguridad basada en rol administrador y documentación OpenAPI/Swagger para endpoints CRUD básicos.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.2.8, Spring Web, Spring Data JPA, Spring Security, Bean Validation, Flyway, springdoc-openapi  
**Storage**: PostgreSQL 15+  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, Spring Security Test, Testcontainers (PostgreSQL)  
**Target Platform**: Backend REST en contenedores Linux (Docker)
**Project Type**: web-service backend (monolito REST)  
**Performance Goals**: p95 < 2s para lecturas y p95 < 3s para escrituras del CRUD de departamentos  
**Constraints**: `nombre` max 50; `clave` única e inmutable provista por usuario; solo admins; bloqueo de borrado con empleados asociados; sin reasignación automática de empleados existentes  
**Scale/Scope**: 1 nueva entidad principal (`Departamento`) + ajuste de relación con `Empleado` + 5 endpoints CRUD

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Research Gate

- ✅ **I. Spring Boot 3 + Java 17 Baseline**: la implementación permanece en Java 17 + Spring Boot 3.2.8.
- ✅ **II. Basic Authentication & Transport Security**: los endpoints de departamentos se integran al esquema de seguridad existente y quedan restringidos a administradores.
- ✅ **III. PostgreSQL Source of Truth**: cambios de esquema en PostgreSQL mediante migraciones Flyway versionadas.
- ✅ **IV. Dockerized Environments & Operations**: ejecución y validación en entorno Docker Compose existente.
- ✅ **V. API Documentation & Test Discipline**: contratos OpenAPI + pruebas unitarias/integración/contrato para el nuevo módulo.

**Gate Result**: PASS

## Project Structure

### Documentation (this feature)

```text
specs/003-crud-departamentos/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/example/empleados/
│   ├── config/
│   ├── controller/
│   ├── domain/
│   ├── dto/
│   ├── exception/
│   ├── mapper/
│   ├── repository/
│   └── service/
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/
├── src/test/java/com/example/empleados/
│   ├── contract/
│   ├── integration/
│   ├── service/
│   └── exception/
└── pom.xml

specs/003-crud-departamentos/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
└── contracts/

docker-compose.yml
```

**Structure Decision**: Se mantiene la estructura monolítica existente en `backend/`, incorporando el módulo de departamentos por capas (controller/service/repository/domain/dto) y extendiendo el dominio de empleados para la relación obligatoria con departamento en nuevas altas/actualizaciones.

## Post-Design Constitution Check

- ✅ **I. Stack baseline**: diseño sin cambios de stack ni de versión de runtime.
- ✅ **II. Security**: operaciones de departamentos acotadas a administradores conforme a la clarificación de alcance.
- ✅ **III. Data governance**: modelo define FK de empleados hacia departamentos y política `restrict` en eliminación.
- ✅ **IV. Docker parity**: quickstart basado en `docker-compose` y perfil local existente.
- ✅ **V. Documentation/testing**: contrato OpenAPI para `/api/v1/departamentos` y estrategia de pruebas definida.

**Gate Result**: PASS

## Complexity Tracking

No se identifican violaciones a la constitución para esta feature.
