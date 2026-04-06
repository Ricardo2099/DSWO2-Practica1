# Tasks: CRUD de Departamentos Frontend

**Input**: Design documents from `/specs/007-crud-departamentos-frontend/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md

**Tests**: Se incluyen tareas de pruebas unitarias frontend para servicio de departamentos y para artefactos de seguridad modificados (guard/interceptor/autorización), en cumplimiento de la constitución.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar estructura de feature y configuración base para el CRUD de departamentos en frontend.

- [X] T001 Crear estructura de feature departamentos en frontend/src/app/features/departamentos/
- [X] T002 Crear archivo de rutas internas de la feature en frontend/src/app/features/departamentos/departamentos.routes.ts
- [X] T003 [P] Registrar ruta protegida `/departamentos` en frontend/src/app/app.routes.ts
- [X] T004 [P] Confirmar base URL de API en frontend/src/environments/environment.ts
- [X] T005 [P] Confirmar base URL de API en frontend/src/environments/environment.development.ts

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura de modelos, servicio HTTP y autorización que bloquea las historias.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

- [X] T006 Crear modelos de dominio de departamentos en frontend/src/app/features/departamentos/models/departamento.models.ts
- [X] T007 [P] Crear modelos de estado de formulario y listado en frontend/src/app/features/departamentos/models/departamento-state.models.ts
- [X] T008 Implementar servicio API de departamentos en frontend/src/app/features/departamentos/services/departamentos.service.ts
- [X] T009 [P] Implementar mapeo centralizado de errores de departamentos en frontend/src/app/features/departamentos/services/departamentos-error.mapper.ts
- [X] T010 Implementar políticas de permisos de departamentos por rol en frontend/src/app/core/services/authorization.service.ts
- [X] T011 [P] Crear fachada de notificaciones para la feature en frontend/src/app/features/departamentos/services/departamentos-notification.service.ts
- [X] T042 [P] Verificar redirección a /login sin sesión válida y ajustar guard/ruta protegida de departamentos en frontend/src/app/app.routes.ts

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel.

---

## Phase 3: User Story 1 - Consultar departamentos (Priority: P1) 🎯 MVP

**Goal**: Permitir a usuario autenticado consultar listado y detalle de departamentos con paginación en cliente.

**Independent Test**: Iniciar sesión como USER, abrir `/departamentos`, visualizar tabla paginada, cambiar página sin nueva llamada de listado y consultar detalle por clave sin acciones de escritura.

### Implementation for User Story 1

- [X] T012 [P] [US1] Crear componente de página de departamentos en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts
- [X] T013 [P] [US1] Crear plantilla de tabla paginada y estados vacíos en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.html
- [X] T014 [P] [US1] Crear estilos responsive de la vista de consulta en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.scss
- [X] T015 [US1] Implementar carga de `GET /api/v1/departamentos` y paginación en cliente en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts
- [X] T016 [US1] Implementar consulta de detalle con `GET /api/v1/departamentos/{clave}` en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts
- [X] T017 [US1] Ocultar acciones de crear/editar/eliminar para rol USER en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.html

**Checkpoint**: User Story 1 fully functional and independently testable.

---

## Phase 4: User Story 2 - Crear y editar departamentos (Priority: P2)

**Goal**: Permitir a ADMIN crear y editar departamentos con formulario reactivo y manejo de conflicto 409.

**Independent Test**: Iniciar sesión como ADMIN, crear departamento por formulario, editar solo `nombre` con `clave` de solo lectura, y ante 409 recargar datos y confirmar reintento.

### Implementation for User Story 2

- [X] T018 [P] [US2] Crear componente de formulario de departamento en frontend/src/app/features/departamentos/components/departamento-form/departamento-form.component.ts
- [X] T019 [P] [US2] Crear plantilla de formulario con modo crear/editar en frontend/src/app/features/departamentos/components/departamento-form/departamento-form.component.html
- [X] T020 [P] [US2] Crear estilos del formulario de departamento en frontend/src/app/features/departamentos/components/departamento-form/departamento-form.component.scss
- [X] T021 [US2] Implementar validaciones de `clave` y `nombre` por modo en frontend/src/app/features/departamentos/components/departamento-form/departamento-form.component.ts
- [X] T022 [US2] Integrar creación `POST /api/v1/departamentos` y refresco de tabla en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts
- [X] T023 [US2] Integrar actualización `PUT /api/v1/departamentos/{clave}` con `clave` inmutable en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts
- [X] T024 [US2] Implementar manejo de conflicto 409 (recarga + confirmación + reintento) en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts
- [X] T025 [US2] Restringir visualización y ejecución de formulario a rol ADMIN en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.html

**Checkpoint**: User Story 2 fully functional and independently testable.

---

## Phase 5: User Story 3 - Eliminar departamentos con control de rol (Priority: P3)

**Goal**: Permitir a ADMIN eliminar departamentos con feedback claro y bloquear eliminación cuando existan empleados asociados.

**Independent Test**: Iniciar sesión como ADMIN, eliminar departamento sin asociados; intentar eliminar con asociados y validar bloqueo con mensaje; iniciar sesión como USER y confirmar ausencia de acción de eliminar.

### Implementation for User Story 3

- [X] T026 [P] [US3] Agregar columna de acciones por fila en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.html
- [X] T027 [US3] Implementar flujo `DELETE /api/v1/departamentos/{clave}` en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts
- [X] T028 [US3] Implementar mensaje específico de bloqueo por asociados (409) en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts
- [X] T029 [US3] Ajustar paginación tras eliminación para evitar página vacía inválida en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts
- [X] T030 [US3] Restringir botón de eliminar a rol ADMIN en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.html

**Checkpoint**: User Story 3 fully functional and independently testable.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre documental, consistencia UX y validación manual final.

- [X] T031 [P] Actualizar contrato OpenAPI de departamentos y enlazarlo en specs/007-crud-departamentos-frontend/contracts/
- [X] T032 [P] Actualizar contrato frontend-backend de departamentos en specs/007-crud-departamentos-frontend/contracts/departamentos-frontend-api-contract.md
- [X] T033 Mejorar consistencia de mensajes snackbar y estados de carga/error en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts
- [X] T034 [P] Actualizar guía de ejecución y validación manual en specs/007-crud-departamentos-frontend/quickstart.md
- [X] T035 [P] Crear pruebas unitarias de departamentos.service (listar, detalle, crear, actualizar, eliminar y errores) en frontend/src/app/features/departamentos/services/departamentos.service.spec.ts
- [X] T036 [P] Crear pruebas unitarias de autorización/guard para acceso a /departamentos y redirección a /login en frontend/src/app/core/services/authorization.service.spec.ts y frontend/src/app/core/guards/auth.guard.spec.ts
- [X] T037 [P] Crear pruebas unitarias de interceptor para token ausente/inválido y manejo de 401 en frontend/src/app/core/interceptors/auth.interceptor.spec.ts
- [X] T038 Validar escenario negativo sin token y registrar evidencia en specs/007-crud-departamentos-frontend/quickstart.md
- [X] T039 Validar escenario negativo con token inválido y registrar evidencia en specs/007-crud-departamentos-frontend/quickstart.md
- [X] T040 Validar escenario negativo con rol insuficiente USER y registrar evidencia en specs/007-crud-departamentos-frontend/quickstart.md
- [ ] T041 Ejecutar validación manual end-to-end y registrar evidencia consolidada en specs/007-crud-departamentos-frontend/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: No dependencies - starts immediately.
- **Phase 2 (Foundational)**: Depends on Phase 1 - blocks all user stories.
- **Phase 3-5 (User Stories)**: Depend on Phase 2 completion.
- **Phase 6 (Polish)**: Depends on completion de todas las user stories (US1, US2, US3).

### User Story Dependencies

- **US1 (P1)**: Starts after Foundational - no dependency on other stories.
- **US2 (P2)**: Starts after Foundational - reusa vista/listado de US1 pero mantiene prueba independiente de creación/edición.
- **US3 (P3)**: Starts after Foundational - reusa tabla de US1 para acción de eliminación.

### Within Each User Story

- Modelos/estado antes de integración visual.
- Servicio y manejo de errores antes de acciones de formulario/tabla.
- Reglas de autorización visibles antes de cierre de historia.

### Parallel Opportunities

- Setup: T003, T004 y T005 en paralelo.
- Foundational: T007, T009, T011 y T042 en paralelo.
- US1: T012, T013 y T014 en paralelo.
- US2: T018, T019 y T020 en paralelo.
- US3: T026 puede iniciar en paralelo con preparación de lógica de T027.
- Polish: T031, T032, T034, T035, T036 y T037 en paralelo.

---

## Parallel Example: User Story 1

```bash
Task: "Crear componente de página de departamentos en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts"
Task: "Crear plantilla de tabla paginada y estados vacíos en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.html"
Task: "Crear estilos responsive de la vista de consulta en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.scss"
```

## Parallel Example: User Story 2

```bash
Task: "Crear componente de formulario de departamento en frontend/src/app/features/departamentos/components/departamento-form/departamento-form.component.ts"
Task: "Crear plantilla de formulario con modo crear/editar en frontend/src/app/features/departamentos/components/departamento-form/departamento-form.component.html"
Task: "Crear estilos del formulario de departamento en frontend/src/app/features/departamentos/components/departamento-form/departamento-form.component.scss"
```

## Parallel Example: User Story 3

```bash
Task: "Agregar columna de acciones por fila en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.html"
Task: "Implementar flujo DELETE /api/v1/departamentos/{clave} en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.ts"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar consulta, detalle y paginación en cliente con rol USER.

### Incremental Delivery

1. Setup + Foundational.
2. Entregar US1 (consulta/detalle/paginación).
3. Entregar US2 (crear/editar con manejo 409).
4. Entregar US3 (eliminación con bloqueo por asociados y permisos).
5. Cerrar con Polish documental y validación E2E.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego división por historias:
   - Dev A: US1 (consulta y paginación)
   - Dev B: US2 (formulario y edición)
   - Dev C: US3 (eliminación y permisos)
3. Integración y cierre en Phase 6.

---

## Notes

- Todas las tareas cumplen formato checklist con ID único.
- Las tareas de historias incluyen etiqueta `[USx]` para trazabilidad.
- `[P]` indica tareas en archivos diferentes sin dependencia directa entre sí.
- Cada historia mantiene criterio de prueba independiente para demos incrementales.
