# Tasks: Suite E2E de Sistema Completo

**Input**: Design documents from `/specs/008-cypress-e2e-suite/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Esta feature requiere pruebas E2E explicitamente solicitadas; por lo tanto se incluyen tareas de test en cada historia.

**Organization**: Tasks grouped by user story to enable independent implementation and testing.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar Cypress y estructura base en frontend.

- [ ] T001 Instalar Cypress y agregar scripts `cy:open`/`cy:run` en frontend/package.json
- [ ] T002 Crear configuracion base de Cypress con `baseUrl` y e2e setup en frontend/cypress.config.ts
- [ ] T003 [P] Crear estructura inicial de soporte Cypress en frontend/cypress/support/e2e.ts
- [ ] T004 [P] Crear archivo de comandos custom inicial en frontend/cypress/support/commands.ts
- [ ] T005 [P] Crear plantilla de datos de prueba E2E en frontend/cypress/fixtures/e2e-data.json

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura transversal que bloquea todas las historias hasta completarse.

**⚠️ CRITICAL**: Ninguna historia inicia antes de completar esta fase.

- [ ] T006 Implementar validacion de variables de entorno Cypress en frontend/cypress/support/env.ts
- [ ] T007 [P] Implementar helper de autenticacion API real (`/auth/login`) en frontend/cypress/support/api.ts
- [ ] T008 Implementar comando `loginAdmin()` con almacenamiento de token/rol en frontend/cypress/support/commands.ts
- [ ] T009 Implementar comando `loginUser()` con almacenamiento de token/rol en frontend/cypress/support/commands.ts
- [ ] T010 [P] Registrar typings de comandos custom en frontend/cypress/support/index.d.ts
- [ ] T011 Configurar politica global de evidencia (video siempre + screenshot en fallo) en frontend/cypress.config.ts
- [ ] T012 [P] Agregar selectores estables `data-testid` al login en frontend/src/app/features/auth/pages/login-page/login-page.component.html
- [ ] T013 [P] Agregar selectores estables `data-testid` a departamentos en frontend/src/app/features/departamentos/pages/departamentos-page/departamentos-page.component.html
- [ ] T014 [P] Agregar selectores estables `data-testid` a formulario de departamentos en frontend/src/app/features/departamentos/components/departamento-form/departamento-form.component.html
- [ ] T015 [P] Agregar selectores estables `data-testid` a empleados en frontend/src/app/features/empleados/pages/empleados-page/empleados-page.component.html
- [ ] T016 [P] Agregar selectores estables `data-testid` a formulario de empleados en frontend/src/app/features/empleados/components/empleado-form/empleado-form.component.html
- [ ] T017 Implementar factory de datos unicos por test (timestamp/uuid) en frontend/cypress/support/data-factory.ts
- [ ] T018 Implementar helpers de limpieza de datos creados en frontend/cypress/support/cleanup.ts

**Checkpoint**: Fundacion lista; las historias pueden avanzar.

---

## Phase 3: User Story 1 - Acceso seguro y sesion confiable (Priority: P1) 🎯 MVP

**Goal**: Validar autenticacion, persistencia de sesion y redirecciones de seguridad desde la UI.

**Independent Test**: Ejecutar solo frontend/cypress/e2e/auth.cy.ts y comprobar login valido/invalido, persistencia y redireccion sin token.

### Tests for User Story 1

- [ ] T019 [P] [US1] Crear spec base de autenticacion en frontend/cypress/e2e/auth.cy.ts
- [ ] T020 [US1] Implementar caso login UI exitoso ADMIN y captura posterior en frontend/cypress/e2e/auth.cy.ts
- [ ] T021 [US1] Implementar caso login UI fallido por credenciales invalidas en frontend/cypress/e2e/auth.cy.ts
- [ ] T022 [US1] Implementar caso persistencia de sesion tras recarga en frontend/cypress/e2e/auth.cy.ts
- [ ] T023 [US1] Implementar caso redireccion a login sin token en frontend/cypress/e2e/auth.cy.ts
- [ ] T024 [US1] Implementar caso token invalido con limpieza de sesion en frontend/cypress/e2e/auth.cy.ts

**Checkpoint**: US1 funcional y testeable de forma independiente.

---

## Phase 4: User Story 2 - Gestion administrativa de departamentos y empleados (Priority: P2)

**Goal**: Validar como ADMIN la creacion/listado de departamentos y creacion/listado/eliminacion de empleados.

**Independent Test**: Ejecutar frontend/cypress/e2e/departamentos.cy.ts y frontend/cypress/e2e/empleados.cy.ts con login por comando API y comprobar altas/bajas reflejadas en tabla.

### Tests for User Story 2

- [ ] T025 [P] [US2] Crear spec de departamentos ADMIN en frontend/cypress/e2e/departamentos.cy.ts
- [ ] T026 [US2] Implementar crear departamento y validar aparicion en tabla en frontend/cypress/e2e/departamentos.cy.ts
- [ ] T027 [US2] Implementar captura manual despues de crear departamento en frontend/cypress/e2e/departamentos.cy.ts
- [ ] T028 [P] [US2] Crear spec de empleados ADMIN en frontend/cypress/e2e/empleados.cy.ts
- [ ] T029 [US2] Implementar crear empleado con departamento y validar aparicion en frontend/cypress/e2e/empleados.cy.ts
- [ ] T030 [US2] Implementar eliminar empleado y validar desaparicion en frontend/cypress/e2e/empleados.cy.ts
- [ ] T031 [US2] Implementar capturas manuales tras crear y eliminar empleado en frontend/cypress/e2e/empleados.cy.ts
- [ ] T032 [US2] Implementar limpieza de recursos por test en frontend/cypress/e2e/departamentos.cy.ts
- [ ] T033 [US2] Implementar limpieza de recursos por test en frontend/cypress/e2e/empleados.cy.ts

**Checkpoint**: US1 y US2 funcionales e independientes.

---

## Phase 5: User Story 3 - Respeto estricto de permisos por rol (Priority: P3)

**Goal**: Validar que USER es solo lectura (UI bloqueada) y que forzar acciones de escritura muestra 403 visible.

**Independent Test**: Ejecutar escenarios USER en specs existentes y comprobar ausencia/bloqueo de acciones y error 403 al forzar escritura.

### Tests for User Story 3

- [ ] T034 [P] [US3] Implementar escenario USER solo lectura en departamentos en frontend/cypress/e2e/departamentos.cy.ts
- [ ] T035 [P] [US3] Implementar escenario USER solo lectura en empleados en frontend/cypress/e2e/empleados.cy.ts
- [ ] T036 [US3] Implementar escenario forzado de escritura con verificacion 403 visible en frontend/cypress/e2e/auth.cy.ts
- [ ] T037 [US3] Implementar escenario ADMIN con acceso completo visible en frontend/cypress/e2e/departamentos.cy.ts
- [ ] T038 [US3] Implementar escenario ADMIN con acciones CRUD visibles en frontend/cypress/e2e/empleados.cy.ts

**Checkpoint**: US3 validada de forma independiente sobre suite existente.

---

## Phase 6: User Story 4 - Evidencia visual de funcionamiento (Priority: P3)

**Goal**: Garantizar evidencia visual consistente y verificable en todos los escenarios clave.

**Independent Test**: Ejecutar la suite y verificar videos + screenshots de hitos definidos en carpetas de artifacts.

### Tests for User Story 4

- [ ] T039 [P] [US4] Crear helper de nombres estandarizados para screenshots en frontend/cypress/support/screenshots.ts
- [ ] T040 [US4] Integrar helper de screenshots en hitos de auth en frontend/cypress/e2e/auth.cy.ts
- [ ] T041 [US4] Integrar helper de screenshots en hitos de departamentos en frontend/cypress/e2e/departamentos.cy.ts
- [ ] T042 [US4] Integrar helper de screenshots en hitos de empleados en frontend/cypress/e2e/empleados.cy.ts
- [ ] T043 [US4] Ajustar configuracion de artifacts (videos/screenshots) y nombres de corrida en frontend/cypress.config.ts

**Checkpoint**: Evidencia visual completa y consistente.

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad, documentacion y validacion final de ejecucion.

- [ ] T044 [P] Documentar variables de entorno y comandos Cypress en frontend/README.md
- [ ] T045 [P] Actualizar flujo final de ejecucion y troubleshooting en specs/008-cypress-e2e-suite/quickstart.md
- [ ] T046 Ejecutar corrida completa y registrar verificacion de criterios en specs/008-cypress-e2e-suite/quickstart.md
- [ ] T047 Ejecutar 5 corridas consecutivas de la suite y registrar tasa de exito para validar SC-003 en specs/008-cypress-e2e-suite/quickstart.md
- [ ] T048 Medir duracion total por corrida (5 muestras), calcular promedio y p95, y registrar cumplimiento de SC-005 en specs/008-cypress-e2e-suite/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia inmediatamente.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias.
- **Phase 3 (US1)**: depende de Phase 2.
- **Phase 4 (US2)**: depende de Phase 2; recomendado despues de US1 para reutilizar autenticacion estabilizada.
- **Phase 5 (US3)**: depende de Phase 2; no requiere completar US1/US2 para mantener independencia de historia.
- **Phase 6 (US4)**: depende de US1-US3 para integrar evidencia en todos los escenarios.
- **Phase 7 (Polish)**: depende de todas las fases anteriores.

### User Story Dependencies

- **US1 (P1)**: inicia tras Foundational; no depende de otras historias.
- **US2 (P2)**: inicia tras Foundational; usa base de login commands establecida.
- **US3 (P3)**: inicia tras Foundational; no depende de US1/US2 y puede validarse de forma autónoma.
- **US4 (P3)**: inicia cuando US1-US3 ya tienen escenarios implementados.

### Within Each User Story

- Implementar primero estructura base del spec.
- Añadir escenarios principales.
- Añadir limpieza/robustez.
- Cerrar con evidencia visual y verificaciones de salida.

### Parallel Opportunities

- Setup: T003, T004 y T005 en paralelo tras T001-T002.
- Foundational: T012-T016 y T017-T018 en paralelo tras T006-T011.
- US2: T025 y T028 en paralelo; luego T026/T029 y T027/T031 por archivo.
- US3: T034 y T035 en paralelo.
- US4: T040, T041 y T042 en paralelo tras T039.
- Polish: T044 y T045 en paralelo antes de T046/T047/T048.

---

## Parallel Example: User Story 1

```bash
# Preparar base de pruebas de autenticacion:
Task: "T019 [US1] Crear spec base en frontend/cypress/e2e/auth.cy.ts"

# Luego ejecutar en secuencia sobre el mismo archivo:
Task: "T020 [US1] Login UI exitoso + screenshot"
Task: "T021 [US1] Login fallido"
Task: "T022 [US1] Persistencia de sesion"
Task: "T023 [US1] Redireccion sin token"
Task: "T024 [US1] Token invalido con limpieza"
```

## Parallel Example: User Story 2

```bash
# Trabajo paralelo por archivo:
Task: "T025 [US2] Spec departamentos en frontend/cypress/e2e/departamentos.cy.ts"
Task: "T028 [US2] Spec empleados en frontend/cypress/e2e/empleados.cy.ts"

# Luego completar escenarios y evidencia en cada spec:
Task: "T026-T027 [US2] Escenarios departamentos"
Task: "T029-T031 [US2] Escenarios empleados"
```

## Parallel Example: User Story 3

```bash
# Validaciones read-only por modulo en paralelo:
Task: "T034 [US3] USER read-only departamentos"
Task: "T035 [US3] USER read-only empleados"

# Escenarios complementarios:
Task: "T036 [US3] Forzado 403 visible"
Task: "T037-T038 [US3] Confirmacion acceso ADMIN"
```

## Parallel Example: User Story 4

```bash
# Tras crear helper comun:
Task: "T039 [US4] Helper screenshots"

# Integracion paralela por spec:
Task: "T040 [US4] Integracion en auth.cy.ts"
Task: "T041 [US4] Integracion en departamentos.cy.ts"
Task: "T042 [US4] Integracion en empleados.cy.ts"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar `frontend/cypress/e2e/auth.cy.ts` de forma aislada.
5. Demostrar autenticacion y seguridad base.

### Incremental Delivery

1. Foundation completa (Phases 1-2).
2. Entregar US1 (autenticacion).
3. Entregar US2 (CRUD ADMIN).
4. Entregar US3 (autorizacion por rol).
5. Entregar US4 (evidencia visual completa).
6. Cerrar con Phase 7 (documentacion + corrida final).

### Parallel Team Strategy

1. Equipo A: infraestructura Cypress (T001-T018).
2. Equipo B: auth spec (T019-T024).
3. Equipo C: specs CRUD ADMIN (T025-T033).
4. Equipo D: roles y evidencia (T034-T043).
5. Integracion final conjunta en T044-T046.

---

## Notes

- Tareas con `[P]` no comparten archivo y pueden ejecutarse en paralelo.
- Cada tarea incluye ruta de archivo para ejecucion directa por LLM.
- Las historias mantienen independencia funcional y de validacion.
- Se prioriza estabilidad E2E con datos unicos por test y limpieza interna.
- El backend real en `http://localhost:8080` es requisito de validacion.
