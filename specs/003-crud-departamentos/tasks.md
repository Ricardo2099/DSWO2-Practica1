# Tasks: CRUD de Departamentos

**Input**: Design documents from `/specs/003-crud-departamentos/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Se incluyen tareas de pruebas de contrato, integración y servicio para validar reglas críticas de negocio, seguridad y relación referencial.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar base documental y técnica mínima para implementar departamentos en el backend actual

- [X] T001 Revisar y alinear el alcance técnico de la feature en specs/003-crud-departamentos/plan.md
- [X] T002 [P] Verificar contrato inicial de endpoints CRUD en specs/003-crud-departamentos/contracts/departamentos.openapi.yaml
- [X] T003 [P] Ajustar guía de arranque y validación de la feature en specs/003-crud-departamentos/quickstart.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura común obligatoria antes de implementar historias de usuario

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T004 Crear migración de tabla departamentos y FK en empleados en backend/src/main/resources/db/migration/V7__create_departamentos_and_fk_empleado.sql
- [X] T005 [P] Crear entidad Departamento con restricciones de clave y nombre en backend/src/main/java/com/example/empleados/domain/Departamento.java
- [X] T006 Actualizar entidad Empleado con relación ManyToOne hacia Departamento en backend/src/main/java/com/example/empleados/domain/Empleado.java
- [X] T007 [P] Crear repositorio JPA de departamentos en backend/src/main/java/com/example/empleados/repository/DepartamentoRepository.java
- [X] T008 [P] Crear DTO de alta de departamento en backend/src/main/java/com/example/empleados/dto/DepartamentoCreateRequest.java
- [X] T009 [P] Crear DTO de actualización de departamento en backend/src/main/java/com/example/empleados/dto/DepartamentoUpdateRequest.java
- [X] T010 [P] Crear DTO de respuesta de departamento en backend/src/main/java/com/example/empleados/dto/DepartamentoResponse.java
- [X] T011 Crear excepción de departamento no encontrado en backend/src/main/java/com/example/empleados/exception/DepartamentoNotFoundException.java
- [X] T012 Crear excepción de departamento en uso por empleados en backend/src/main/java/com/example/empleados/exception/DepartamentoInUseException.java
- [X] T013 Extender manejo global de errores para nuevas excepciones de departamento en backend/src/main/java/com/example/empleados/exception/GlobalExceptionHandler.java
- [X] T014 Configurar autorización para `/api/v1/departamentos/**` solo admin en backend/src/main/java/com/example/empleados/config/SecurityConfig.java
- [X] T015 Registrar documentación OpenAPI de departamentos y seguridad en backend/src/main/java/com/example/empleados/config/OpenApiConfig.java

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Registrar departamentos (Priority: P1) 🎯 MVP

**Goal**: Permitir crear departamentos con `clave` provista por usuario y `nombre` válido (max 50), con control de duplicados

**Independent Test**: Hacer `POST /api/v1/departamentos` con datos válidos y confirmar `201`; probar clave duplicada y nombre inválido para obtener errores esperados

### Tests for User Story 1

- [X] T016 [P] [US1] Crear prueba de contrato para POST de departamentos en backend/src/test/java/com/example/empleados/contract/DepartamentoCreateContractTest.java
- [X] T017 [P] [US1] Crear prueba de integración para alta de departamento contra PostgreSQL en backend/src/test/java/com/example/empleados/integration/DepartamentoCreateIntegrationTest.java
- [X] T018 [P] [US1] Crear prueba unitaria de reglas de creación y duplicidad en backend/src/test/java/com/example/empleados/service/DepartamentoServiceTest.java

### Implementation for User Story 1

- [X] T019 [P] [US1] Crear mapper de Departamento entidad/dto en backend/src/main/java/com/example/empleados/mapper/DepartamentoMapper.java
- [X] T020 [US1] Implementar lógica de creación con validación de duplicidad en backend/src/main/java/com/example/empleados/service/DepartamentoService.java
- [X] T021 [US1] Implementar endpoint POST `/api/v1/departamentos` en backend/src/main/java/com/example/empleados/controller/DepartamentoController.java
- [X] T022 [US1] Alinear respuestas de validación/duplicidad con Problem Details en backend/src/main/java/com/example/empleados/exception/GlobalExceptionHandler.java

**Checkpoint**: User Story 1 fully functional and testable independently

---

## Phase 4: User Story 2 - Consultar y actualizar departamentos (Priority: P2)

**Goal**: Permitir listar, obtener por clave y actualizar el nombre de departamentos existentes

**Independent Test**: Listar departamentos, consultar uno por clave y actualizar su nombre; validar `404` para clave inexistente

### Tests for User Story 2

- [X] T023 [P] [US2] Crear pruebas de contrato para GET lista y GET por clave en backend/src/test/java/com/example/empleados/contract/DepartamentoQueryContractTest.java
- [X] T024 [P] [US2] Crear pruebas de contrato para PUT de departamento y 404 en backend/src/test/java/com/example/empleados/contract/DepartamentoUpdateContractTest.java
- [X] T025 [P] [US2] Crear pruebas de integración de consulta y actualización en backend/src/test/java/com/example/empleados/integration/DepartamentoQueryUpdateIntegrationTest.java

### Implementation for User Story 2

- [X] T026 [US2] Implementar lógica de listado y búsqueda por clave en backend/src/main/java/com/example/empleados/service/DepartamentoService.java
- [X] T027 [US2] Implementar lógica de actualización de nombre con validaciones en backend/src/main/java/com/example/empleados/service/DepartamentoService.java
- [X] T028 [US2] Exponer endpoints GET `/api/v1/departamentos` y GET `/api/v1/departamentos/{clave}` en backend/src/main/java/com/example/empleados/controller/DepartamentoController.java
- [X] T029 [US2] Exponer endpoint PUT `/api/v1/departamentos/{clave}` en backend/src/main/java/com/example/empleados/controller/DepartamentoController.java
- [X] T030 [US2] Validar inmutabilidad de `clave` en actualización dentro de backend/src/main/java/com/example/empleados/service/DepartamentoService.java

**Checkpoint**: User Stories 1 and 2 are independently functional

---

## Phase 5: User Story 3 - Mantener la relación con empleados (Priority: P3)

**Goal**: Garantizar la relación departamento-empleado y bloquear eliminación de departamentos con empleados asociados

**Independent Test**: Asociar empleado a departamento válido, rechazar asociación a departamento inexistente y bloquear DELETE de departamento con empleados

### Tests for User Story 3

- [X] T031 [P] [US3] Crear prueba de contrato para DELETE restringido por relación en backend/src/test/java/com/example/empleados/contract/DepartamentoDeleteRelationContractTest.java
- [X] T032 [P] [US3] Crear prueba de contrato de validación de asociación a departamento inexistente en backend/src/test/java/com/example/empleados/contract/EmpleadoDepartamentoRelationContractTest.java
- [X] T033 [P] [US3] Crear prueba de integración de restricción referencial departamento-empleado en backend/src/test/java/com/example/empleados/integration/DepartamentoRelationIntegrationTest.java

### Implementation for User Story 3

- [X] T034 [US3] Implementar regla de borrado restringido cuando existen empleados asociados en backend/src/main/java/com/example/empleados/service/DepartamentoService.java
- [X] T035 [US3] Exponer endpoint DELETE `/api/v1/departamentos/{clave}` con restricción de relación en backend/src/main/java/com/example/empleados/controller/DepartamentoController.java
- [X] T036 [US3] Actualizar lógica de creación/actualización de empleado para validar departamento existente en backend/src/main/java/com/example/empleados/service/EmpleadoService.java
- [X] T037 [US3] Incorporar `departamentoClave` en request/response de empleado en backend/src/main/java/com/example/empleados/dto/EmpleadoCreateRequest.java
- [X] T038 [US3] Incorporar `departamentoClave` en DTO de actualización de empleado en backend/src/main/java/com/example/empleados/dto/EmpleadoUpdateRequest.java
- [X] T039 [US3] Incorporar `departamentoClave` en DTO de salida de empleado en backend/src/main/java/com/example/empleados/dto/EmpleadoResponse.java
- [X] T040 [US3] Ajustar mapeo empleado-departamento en backend/src/main/java/com/example/empleados/mapper/EmpleadoMapper.java

**Checkpoint**: All user stories are independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad y consistencia transversal de la feature

- [X] T041 [P] Actualizar contrato OpenAPI final de departamentos según implementación en specs/003-crud-departamentos/contracts/departamentos.openapi.yaml
- [X] T042 [P] Alinear documentación de verificación y pasos finales en specs/003-crud-departamentos/quickstart.md
- [X] T043 [P] Añadir/ajustar pruebas negativas de seguridad para endpoints de departamentos en backend/src/test/java/com/example/empleados/contract/SecurityNegativeContractTest.java
- [X] T044 Ejecutar suite de pruebas y registrar resultado de validación en specs/003-crud-departamentos/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
- **Polish (Phase 6)**: Depends on user stories completion

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2)
- **User Story 2 (P2)**: Can start after Foundational (Phase 2), reusing `DepartamentoService`/`DepartamentoController`
- **User Story 3 (P3)**: Can start after Foundational (Phase 2), and depends on base operations de departamento ya disponibles

### Within Each User Story

- Tests before implementation
- DTOs/mappers before service logic
- Services before controllers
- Error handling and contract alignment before checkpoint

### Parallel Opportunities

- Setup en paralelo: T002, T003
- Foundational en paralelo: T005, T007, T008, T009, T010
- US1 en paralelo: T016, T017, T018, T019
- US2 en paralelo: T023, T024, T025
- US3 en paralelo: T031, T032, T033
- Polish en paralelo: T041, T042, T043

---

## Parallel Example: User Story 1

```bash
Task: "Crear prueba de contrato para POST de departamentos en backend/src/test/java/com/example/empleados/contract/DepartamentoCreateContractTest.java"
Task: "Crear prueba de integración para alta de departamento contra PostgreSQL en backend/src/test/java/com/example/empleados/integration/DepartamentoCreateIntegrationTest.java"
Task: "Crear mapper de Departamento entidad/dto en backend/src/main/java/com/example/empleados/mapper/DepartamentoMapper.java"
```

---

## Parallel Example: User Story 2

```bash
Task: "Crear pruebas de contrato para GET lista y GET por clave en backend/src/test/java/com/example/empleados/contract/DepartamentoQueryContractTest.java"
Task: "Crear pruebas de contrato para PUT de departamento y 404 en backend/src/test/java/com/example/empleados/contract/DepartamentoUpdateContractTest.java"
Task: "Crear pruebas de integración de consulta y actualización en backend/src/test/java/com/example/empleados/integration/DepartamentoQueryUpdateIntegrationTest.java"
```

---

## Parallel Example: User Story 3

```bash
Task: "Crear prueba de contrato para DELETE restringido por relación en backend/src/test/java/com/example/empleados/contract/DepartamentoDeleteRelationContractTest.java"
Task: "Crear prueba de contrato de validación de asociación a departamento inexistente en backend/src/test/java/com/example/empleados/contract/EmpleadoDepartamentoRelationContractTest.java"
Task: "Crear prueba de integración de restricción referencial departamento-empleado en backend/src/test/java/com/example/empleados/integration/DepartamentoRelationIntegrationTest.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1 (Setup)
2. Complete Phase 2 (Foundational)
3. Complete Phase 3 (US1)
4. Validate US1 independently via POST + validación de errores

### Incremental Delivery

1. Setup + Foundational
2. Deliver US1 (MVP)
3. Deliver US2
4. Deliver US3
5. Execute Polish and final validation

### Parallel Team Strategy

1. Team completes Setup + Foundational
2. After foundation:
   - Dev A: US1
   - Dev B: US2
   - Dev C: US3
3. Integrate and close with Phase 6

---

## Notes

- Todas las tareas cumplen formato checklist obligatorio con ID secuencial y rutas de archivo.
- Las tareas marcadas con `[P]` están definidas para ejecución paralela sin conflicto directo de dependencias.
- Cada historia incluye criterio de prueba independiente y checkpoint de salida.
