# Tasks: CRUD de Empleados Frontend

**Input**: Design documents from `/specs/006-crud-empleados-frontend/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/, quickstart.md

**Tests**: Se incluyen tareas de pruebas unitarias frontend para servicios, interceptor, permisos por rol y componente de empleados, en cumplimiento de la constitución.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Inicializar frontend independiente y estructura base para la feature de empleados.

- [X] T001 Crear proyecto Angular independiente en frontend/ con configuracion inicial en frontend/angular.json
- [X] T002 Configurar Angular Material para la aplicacion en frontend/src/app/app.config.ts
- [X] T003 [P] Definir base URL de API en frontend/src/environments/environment.ts
- [X] T004 [P] Definir base URL de API de desarrollo en frontend/src/environments/environment.development.ts
- [X] T005 Crear ruta protegida `/empleados` en frontend/src/app/app.routes.ts
- [X] T006 [P] Crear estructura de feature empleados en frontend/src/app/features/empleados/

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura comun de datos, roles y servicios HTTP necesaria antes de cualquier historia.

**CRITICAL**: No user story work can begin until this phase is complete.

- [X] T007 Crear interfaces de dominio de empleados en frontend/src/app/features/empleados/models/empleado.models.ts
- [X] T008 [P] Crear interfaces de paginacion (`content`, `totalElements`, `totalPages`, `number`, `size`) en frontend/src/app/features/empleados/models/paged-response.models.ts
- [X] T009 [P] Crear interfaz de opcion de departamento en frontend/src/app/features/departamentos/models/departamento-option.models.ts
- [X] T010 Implementar servicio de empleados (`getEmpleados`, `createEmpleado`, `deleteEmpleado`) en frontend/src/app/features/empleados/services/empleados.service.ts
- [X] T011 [P] Implementar servicio de catalogo de departamentos en frontend/src/app/features/departamentos/services/departamentos.service.ts
- [X] T012 Implementar servicio de permisos por rol (`ADMIN`/`USER`) en frontend/src/app/core/services/authorization.service.ts
- [X] T013 [P] Implementar servicio de notificaciones snackbar en frontend/src/app/shared/services/notification.service.ts
- [X] T014 Integrar interceptor para base URL y bearer token en frontend/src/app/core/interceptors/auth.interceptor.ts

**Checkpoint**: Foundation ready - user story implementation can now begin.

---

## Phase 3: User Story 1 - Ver listado de empleados (Priority: P1) MVP

**Goal**: Mostrar listado paginado server-side para usuarios autenticados con visibilidad de solo lectura para USER.

**Independent Test**: Iniciar sesion como USER, abrir `/empleados`, verificar carga de tabla con `GET /api/v1/empleados?page=0&size=10` y cambio de pagina/tamano sin recarga manual.

### Implementation for User Story 1

- [X] T015 [US1] Crear contenedor de pagina de empleados en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.ts
- [X] T016 [P] [US1] Crear plantilla de pagina con tabla y estados de carga/error en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.html
- [X] T017 [P] [US1] Crear estilos responsive de pagina en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.scss
- [X] T018 [US1] Implementar carga paginada server-side con `page` y `size` en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.ts
- [X] T019 [US1] Integrar MatTable y MatPaginator con contrato paginado en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.html
- [X] T020 [US1] Manejar errores de listado con snackbar en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.ts

**Checkpoint**: User Story 1 should be fully functional and testable independently.

---

## Phase 4: User Story 2 - Crear empleados con departamento (Priority: P2)

**Goal**: Permitir a ADMIN crear empleados con formulario reactivo y seleccion obligatoria de departamento cargado desde API.

**Independent Test**: Iniciar sesion como ADMIN, abrir formulario, cargar departamentos por `GET /api/v1/departamentos`, enviar alta valida y confirmar refresco de tabla.

### Implementation for User Story 2

- [X] T021 [US2] Crear componente de formulario de empleado en frontend/src/app/features/empleados/components/empleado-form/empleado-form.component.ts
- [X] T022 [P] [US2] Implementar plantilla de formulario reactivo con selector de departamento en frontend/src/app/features/empleados/components/empleado-form/empleado-form.component.html
- [X] T023 [P] [US2] Implementar estilos del formulario en frontend/src/app/features/empleados/components/empleado-form/empleado-form.component.scss
- [X] T024 [US2] Agregar validaciones de campos (`nombre`, `direccion`, `telefono`, `correo`, `departamentoClave`) en frontend/src/app/features/empleados/components/empleado-form/empleado-form.component.ts
- [X] T025 [US2] Integrar carga de departamentos desde API en frontend/src/app/features/empleados/components/empleado-form/empleado-form.component.ts
- [X] T026 [US2] Integrar submit de `createEmpleado()` y refresco de listado en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.ts
- [X] T027 [US2] Restringir visibilidad de formulario a rol ADMIN en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.html

**Checkpoint**: User Story 2 should be fully functional and testable independently.

---

## Phase 5: User Story 3 - Eliminar empleados segun permisos (Priority: P3)

**Goal**: Permitir a ADMIN eliminar empleados por fila y mantener a USER en modo solo lectura.

**Independent Test**: Iniciar sesion como ADMIN para eliminar una fila y validar refresco; iniciar sesion como USER y verificar que no aparecen acciones de eliminar.

### Implementation for User Story 3

- [X] T028 [US3] Agregar columna de acciones de fila en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.html
- [X] T029 [US3] Implementar accion `deleteEmpleado(clave)` con snackbar de resultado en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.ts
- [X] T030 [US3] Ajustar refresco de pagina tras eliminacion y manejo de pagina vacia en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.ts
- [X] T031 [US3] Ocultar o deshabilitar accion eliminar para rol USER en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.html

**Checkpoint**: User Story 3 should be fully functional and testable independently.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de documentacion y validacion integral de la feature.

- [X] T032 [P] Actualizar contrato de integracion de frontend en specs/006-crud-empleados-frontend/contracts/empleados-frontend-api-contract.md
- [X] T033 [P] Actualizar pasos de validacion final en specs/006-crud-empleados-frontend/quickstart.md
- [X] T034 Mejorar responsive y estados vacios de tabla/formulario en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.scss
- [ ] T035 Ejecutar validacion manual end-to-end y registrar resultados en specs/006-crud-empleados-frontend/quickstart.md
- [X] T036 [P] Crear pruebas unitarias de empleados.service (listar paginado, crear, eliminar, errores) en frontend/src/app/features/empleados/services/empleados.service.spec.ts
- [X] T037 [P] Crear pruebas unitarias de auth.interceptor para Authorization Bearer y manejo de 401 en frontend/src/app/core/interceptors/auth.interceptor.spec.ts
- [X] T038 [P] Crear pruebas unitarias de authorization.service para matriz ADMIN/USER en frontend/src/app/core/services/authorization.service.spec.ts
- [X] T039 Crear pruebas unitarias de empleados-page.component para visibilidad por rol en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.spec.ts
- [X] T040 Crear pruebas unitarias de empleado-form.component para validaciones y submit en frontend/src/app/features/empleados/components/empleado-form/empleado-form.component.spec.ts
- [ ] T041 Validar escenarios negativos sin token, token inválido y rol insuficiente en quickstart y registrar evidencia en specs/006-crud-empleados-frontend/quickstart.md
- [X] T042 [P] Añadir pruebas unitarias de flujo de error 401/403 en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.spec.ts y frontend/src/app/core/interceptors/auth.interceptor.spec.ts
- [X] T043 Definir protocolo de medición de SC-001 y SC-006 (dataset, tamaño de página, repeticiones, evidencia) en specs/006-crud-empleados-frontend/quickstart.md
- [ ] T044 Ejecutar mediciones de SC-001 y SC-006 y registrar resultados en specs/006-crud-empleados-frontend/quickstart.md
---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately.
- **Foundational (Phase 2)**: Depends on Setup completion - blocks all user stories.
- **User Stories (Phase 3+)**: Depend on Foundational completion.
- **Polish (Phase 6)**: Depends on all user stories completion.

### User Story Dependencies

- **User Story 1 (P1)**: Starts after Foundational; no dependency on other stories.
- **User Story 2 (P2)**: Starts after Foundational and reuses listado de US1 para refresco post-create.
- **User Story 3 (P3)**: Starts after Foundational and reuses tabla/listado de US1.

### Within Each User Story

- Models/services ready before UI integration.
- Data loading and role checks before user actions.
- UX feedback (snackbar) integrated before story closure.

### Parallel Opportunities

- Setup: T003, T004, T006 can run in parallel.
- Foundational: T008, T009, T011, T013 can run in parallel.
- US1: T016 and T017 can run in parallel after T015.
- US2: T022 and T023 can run in parallel after T021.
- Phase 6: T032 and T033 can run in parallel.

---

## Parallel Example: User Story 1

```bash
Task: "Crear plantilla de pagina con tabla y estados de carga/error en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.html"
Task: "Crear estilos responsive de pagina en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.scss"
```

## Parallel Example: User Story 2

```bash
Task: "Implementar plantilla de formulario reactivo con selector de departamento en frontend/src/app/features/empleados/components/empleado-form/empleado-form.component.html"
Task: "Implementar estilos del formulario en frontend/src/app/features/empleados/components/empleado-form/empleado-form.component.scss"
```

## Parallel Example: User Story 3

```bash
Task: "Agregar columna de acciones de fila en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.html"
Task: "Implementar accion deleteEmpleado(clave) con snackbar de resultado en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.ts"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup.
2. Complete Phase 2: Foundational.
3. Complete Phase 3: User Story 1.
4. Validate paginacion y lectura para rol USER.

### Incremental Delivery

1. Setup + Foundational.
2. Deliver US1 (listado paginado).
3. Deliver US2 (alta con departamento).
4. Deliver US3 (eliminacion por rol).
5. Final polish and documentation closure.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego trabajo en paralelo:
   - Dev A: listado paginado (US1)
   - Dev B: formulario de alta (US2)
   - Dev C: accion de eliminacion y permisos UI (US3)

---

## Notes

- Cada tarea usa formato checklist obligatorio con ID secuencial.
- Las tareas de historias incluyen etiqueta `[USx]` para trazabilidad.
- Las tareas marcadas con `[P]` no compiten por el mismo archivo y pueden ejecutarse en paralelo.
