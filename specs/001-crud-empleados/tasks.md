# Tasks: CRUD de Empleados

**Input**: Design documents from `/specs/001-crud-empleados/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Se incluyen tareas explícitas de pruebas de contrato, integración, seguridad negativa y rendimiento para cumplir la constitución del proyecto.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Inicializar proyecto backend y entorno base para Spring Boot + PostgreSQL + Docker

- [X] T001 Crear esqueleto del backend Spring Boot en backend/pom.xml
- [X] T002 Configurar dependencias base (web, jpa, security, flyway, openapi, validation) en backend/pom.xml
- [X] T003 [P] Crear configuración principal de aplicación en backend/src/main/resources/application.yml
- [X] T004 [P] Crear Dockerfile de aplicación en backend/Dockerfile
- [X] T005 Crear orquestación local con PostgreSQL en docker-compose.yml

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura común obligatoria antes de cualquier historia

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T006 Crear migración inicial de tabla empleados y secuencia autonumérica en backend/src/main/resources/db/migration/V1__create_empleados_table.sql
- [X] T007 [P] Implementar entidad Empleado con restricciones de longitud en backend/src/main/java/com/example/empleados/domain/Empleado.java
- [X] T008 [P] Implementar repositorio JPA de Empleado en backend/src/main/java/com/example/empleados/repository/EmpleadoRepository.java
- [X] T009 [P] Crear DTO de alta sin campo `clave` y response con `clave` en backend/src/main/java/com/example/empleados/dto/EmpleadoCreateRequest.java
- [X] T010 [P] Crear DTOs de actualización y respuesta en backend/src/main/java/com/example/empleados/dto/EmpleadoUpdateRequest.java
- [X] T011 Implementar manejo global de errores Problem Details en backend/src/main/java/com/example/empleados/exception/GlobalExceptionHandler.java
- [X] T012 Implementar configuración de seguridad Basic Auth en backend/src/main/java/com/example/empleados/config/SecurityConfig.java
- [X] T013 Configurar usuario(s) de autenticación básica en backend/src/main/resources/application.yml
- [X] T014 Publicar configuración de OpenAPI/Swagger en backend/src/main/java/com/example/empleados/config/OpenApiConfig.java
- [X] T015 Implementar generador de `clave` con patrón `EMP-<autonumero>` en backend/src/main/java/com/example/empleados/service/ClaveEmpleadoGenerator.java
- [X] T016 Configurar endpoint de salud `/actuator/health` en backend/src/main/resources/application.yml
- [X] T017 Configurar logging JSON a STDOUT/STDERR en backend/src/main/resources/application.yml
- [X] T018 Crear archivo de paridad de entorno en .env.example

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Registrar empleados (Priority: P1) 🎯 MVP

**Goal**: Permitir alta de empleados con `clave` autogenerada `EMP-<autonumero>` y validaciones de campos

**Independent Test**: Crear empleado válido y verificar que devuelve `clave` autogenerada; validar rechazos por campos >100 caracteres

### Tests for User Story 1

- [X] T019 [P] [US1] Crear pruebas de contrato de POST (validación y clave autogenerada) en backend/src/test/java/com/example/empleados/contract/EmpleadoCreateContractTest.java
- [X] T020 [P] [US1] Crear prueba de integración de alta contra PostgreSQL en backend/src/test/java/com/example/empleados/integration/EmpleadoCreateIntegrationTest.java

### Implementation for User Story 1

- [X] T021 [P] [US1] Implementar mapper de creación a entidad en backend/src/main/java/com/example/empleados/mapper/EmpleadoMapper.java
- [X] T022 [US1] Integrar generación de `clave` automática en backend/src/main/java/com/example/empleados/service/EmpleadoService.java
- [X] T023 [US1] Exponer endpoint POST /api/v1/empleados (sin `clave` en request) en backend/src/main/java/com/example/empleados/controller/EmpleadoController.java
- [X] T024 [US1] Definir excepción de colisión de secuencia de `clave` en backend/src/main/java/com/example/empleados/exception/ClaveGeneracionException.java
- [X] T025 [US1] Documentar endpoint de creación según contrato en backend/src/main/java/com/example/empleados/controller/EmpleadoController.java

**Checkpoint**: User Story 1 funcional y verificable de forma independiente

---

## Phase 4: User Story 2 - Consultar empleados (Priority: P2)

**Goal**: Permitir consulta individual por `clave` y listado completo

**Independent Test**: Consultar por `clave` existente/inexistente y listar registros cargados

### Tests for User Story 2

- [X] T026 [P] [US2] Crear pruebas de contrato para GET detalle/lista y 404 en backend/src/test/java/com/example/empleados/contract/EmpleadoQueryContractTest.java
- [X] T027 [P] [US2] Crear prueba de integración de consulta por `clave` en backend/src/test/java/com/example/empleados/integration/EmpleadoQueryIntegrationTest.java

### Implementation for User Story 2

- [X] T028 [US2] Implementar lógica de consulta por clave y listado en backend/src/main/java/com/example/empleados/service/EmpleadoService.java
- [X] T029 [US2] Exponer endpoints GET /api/v1/empleados y GET /api/v1/empleados/{clave} en backend/src/main/java/com/example/empleados/controller/EmpleadoController.java
- [X] T030 [US2] Definir excepción de no encontrado en backend/src/main/java/com/example/empleados/exception/EmpleadoNotFoundException.java
- [X] T031 [US2] Alinear respuestas 404 con Problem Details en backend/src/main/java/com/example/empleados/exception/GlobalExceptionHandler.java

**Checkpoint**: User Stories 1 y 2 funcionales de forma independiente

---

## Phase 5: User Story 3 - Actualizar y eliminar empleados (Priority: P3)

**Goal**: Permitir actualización de campos editables y eliminación por `clave`

**Independent Test**: Actualizar un empleado existente y luego eliminarlo verificando 404 posterior

### Tests for User Story 3

- [X] T032 [P] [US3] Crear pruebas de contrato para PUT/DELETE e inmutabilidad de `clave` en backend/src/test/java/com/example/empleados/contract/EmpleadoUpdateDeleteContractTest.java
- [X] T033 [P] [US3] Crear prueba de integración de actualización/eliminación en backend/src/test/java/com/example/empleados/integration/EmpleadoUpdateDeleteIntegrationTest.java

### Implementation for User Story 3

- [X] T034 [US3] Implementar actualización de `nombre`, `direccion`, `telefono` en backend/src/main/java/com/example/empleados/service/EmpleadoService.java
- [X] T035 [US3] Implementar eliminación por `clave` en backend/src/main/java/com/example/empleados/service/EmpleadoService.java
- [X] T036 [US3] Exponer endpoints PUT y DELETE /api/v1/empleados/{clave} en backend/src/main/java/com/example/empleados/controller/EmpleadoController.java
- [X] T037 [US3] Asegurar inmutabilidad de `clave` en actualización dentro de backend/src/main/java/com/example/empleados/service/EmpleadoService.java

**Checkpoint**: Todas las historias de usuario funcionales e independientes

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad transversal para la feature completa

- [X] T038 [P] Implementar pruebas negativas de Basic Auth (sin credenciales/credenciales inválidas) en backend/src/test/java/com/example/empleados/contract/SecurityNegativeContractTest.java
- [X] T039 [P] Agregar verificación de cobertura mínima 85% en backend/pom.xml
- [X] T040 [P] Ejecutar prueba de rendimiento para SC-001/SC-003 y documentar p95 en specs/001-crud-empleados/quickstart.md
- [X] T041 [P] Sincronizar implementación final con contrato en specs/001-crud-empleados/contracts/empleados.openapi.yaml
- [X] T042 [P] Actualizar guía de ejecución con endpoints finales en specs/001-crud-empleados/quickstart.md
- [X] T043 Ajustar configuración de entorno y credenciales ejemplo en backend/src/main/resources/application.yml
- [X] T044 Ejecutar validación integral de flujo CRUD y documentar resultado en specs/001-crud-empleados/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Sin dependencias
- **Foundational (Phase 2)**: Depende de Setup; bloquea todas las historias
- **User Stories (Phase 3+)**: Dependen de Foundational completada
- **Polish (Phase 6)**: Depende de historias completadas

### User Story Dependencies

- **US1 (P1)**: Inicia tras Phase 2
- **US2 (P2)**: Inicia tras Phase 2; reutiliza service/controller de US1 pero sigue siendo verificable por sí sola
- **US3 (P3)**: Inicia tras Phase 2; extiende service/controller existentes

### Within Each User Story

- DTO/mapper antes de reglas de negocio
- Reglas de negocio antes de endpoints
- Endpoints antes de verificación final de historia

### Parallel Opportunities

- T003, T004 pueden ejecutarse en paralelo durante Setup
- T007, T008, T009, T010 pueden ejecutarse en paralelo durante Foundational
- En US1, T019 y T020 pueden ejecutarse en paralelo; T021 puede avanzar con T024
- En US2, T026 y T027 pueden ejecutarse en paralelo
- En US3, T032 y T033 pueden ejecutarse en paralelo
- En Polish, T038, T039 y T040 pueden ejecutarse en paralelo

---

## Parallel Example: User Story 1

```bash
# Trabajo paralelo sugerido para US1
Task: "Implementar mapper de creación a entidad en backend/src/main/java/com/example/empleados/mapper/EmpleadoMapper.java"
Task: "Definir excepción de colisión de secuencia de clave en backend/src/main/java/com/example/empleados/exception/ClaveGeneracionException.java"
```

---

## Parallel Example: User Story 2

```bash
# Trabajo paralelo sugerido para US2
Task: "Definir excepción de no encontrado en backend/src/main/java/com/example/empleados/exception/EmpleadoNotFoundException.java"
Task: "Implementar lógica de consulta por clave y listado en backend/src/main/java/com/example/empleados/service/EmpleadoService.java"
```

---

## Parallel Example: User Story 3

```bash
# Trabajo paralelo sugerido para US3
Task: "Implementar actualización de campos editables en backend/src/main/java/com/example/empleados/service/EmpleadoService.java"
Task: "Implementar eliminación por clave en backend/src/main/java/com/example/empleados/service/EmpleadoService.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup)
2. Completar Phase 2 (Foundational)
3. Completar Phase 3 (US1)
4. Validar alta y reglas de longitud/duplicidad
5. Entregar MVP

### Incremental Delivery

1. Setup + Foundational
2. Agregar US1 y validar
3. Agregar US2 y validar
4. Agregar US3 y validar
5. Ejecutar Polish final

### Parallel Team Strategy

1. Equipo completo en Phase 1 y 2
2. Luego reparto por historia:
   - Dev A: US1
   - Dev B: US2
   - Dev C: US3
3. Integración y cierre en Phase 6

---

## Notes

- Todas las tareas siguen formato checklist obligatorio.
- Las tareas con `[P]` están pensadas para ejecución en paralelo sin conflicto de archivos incompletos.
- Cada fase de historia mantiene criterio de prueba independiente.
- Se preserva alineación con contrato OpenAPI y constitución del proyecto.
