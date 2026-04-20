# Tasks: Punto De Entrada HTTP Unico

**Input**: Documentos de diseno desde /specs/009-reverse-proxy-stack/
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/, quickstart.md

**Tests**: Esta feature solicita validacion operativa y funcional explicita, por lo tanto se incluyen tareas de prueba por historia.

**Organization**: Tasks agrupadas por historia de usuario para implementacion y validacion independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar estructura base del proxy y artefactos de infraestructura.

- [x] T001 Crear directorio de configuracion del proxy en reverse-proxy/
- [x] T002 Crear archivo base de configuracion Nginx con upstreams en reverse-proxy/nginx.conf
- [x] T003 Crear archivo de override para debug local de base de datos en docker-compose.debug-db.yml
- [x] T004 [P] Agregar seccion de uso de compose override en .env.example
- [x] T005 [P] Agregar seccion de arquitectura de red y entrada unica en README.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura transversal obligatoria antes de cualquier historia.

**Critical**: Ninguna historia inicia hasta completar esta fase.

- [x] T006 Definir red interna dedicada y asociarla a servicios en docker-compose.yml
- [x] T007 Corregir variable de entorno de datasource en docker-compose.yml
- [x] T008 Definir servicio reverse-proxy y publicacion exclusiva de puerto 80 en docker-compose.yml
- [x] T009 Configurar healthcheck de api y reverse-proxy en docker-compose.yml
- [x] T010 Remover exposicion publica de api y frontend en docker-compose.yml
- [x] T011 Remover exposicion publica de db en compose base y dejarla solo para override en docker-compose.yml
- [x] T012 [P] Ajustar base URL de frontend a rutas relativas en frontend/src/environments/environment.ts
- [x] T013 [P] Ajustar base URL de frontend a rutas relativas en frontend/src/environments/environment.development.ts
- [x] T014 Ajustar el interceptor para soportar origen unificado sin hardcode de host en frontend/src/app/core/interceptors/auth.interceptor.ts
- [x] T015 Ajustar pruebas unitarias del interceptor para rutas relativas en frontend/src/app/core/interceptors/auth.interceptor.spec.ts

**Checkpoint**: Base de red, proxy, salud y consumo relativo lista; las historias ya pueden desarrollarse.

---

## Phase 3: User Story 1 - Acceso Unificado A La Aplicacion (Priority: P1) MVP

**Goal**: Exponer frontend y rutas API/documentacion desde un unico endpoint HTTP.

**Independent Test**: Con stack arriba, GET / y GET /swagger-ui responden 200 desde el proxy y rutas API se enrutan al backend.

### Tests for User Story 1

- [x] T016 [P] [US1] Crear script de verificacion de endpoint raiz por proxy en specs/009-reverse-proxy-stack/scripts/test-root.ps1
- [x] T017 [P] [US1] Crear script de verificacion de swagger por proxy en specs/009-reverse-proxy-stack/scripts/test-swagger.ps1
- [x] T018 [US1] Agregar escenario de fallback SPA para ruta profunda en specs/009-reverse-proxy-stack/scripts/test-spa-fallback.ps1

### Implementation for User Story 1

- [x] T019 [US1] Definir reglas de enrutamiento por prefijo API con prioridad alta en reverse-proxy/nginx.conf
- [x] T020 [US1] Definir enrutamiento frontend y fallback a index para rutas no API en reverse-proxy/nginx.conf
- [x] T021 [US1] Montar configuracion de Nginx en servicio reverse-proxy en docker-compose.yml
- [x] T022 [US1] Configurar depends_on con condicion de salud para reverse-proxy sobre api/frontend en docker-compose.yml
- [x] T023 [US1] Registrar comandos de validacion HTTP 200 para raiz y swagger en specs/009-reverse-proxy-stack/quickstart.md

**Checkpoint**: US1 funcional y verificable en forma independiente.

---

## Phase 4: User Story 2 - Autenticacion Bearer A Traves Del Proxy (Priority: P2)

**Goal**: Mantener login y consumo de endpoints protegidos con token Bearer a traves del proxy.

**Independent Test**: POST /auth/login devuelve 200 y GET /api/v1/empleados con Authorization Bearer devuelve 200 via proxy.

### Tests for User Story 2

- [x] T024 [P] [US2] Crear script de login por proxy y captura de token en specs/009-reverse-proxy-stack/scripts/test-auth-login.ps1
- [x] T025 [P] [US2] Crear script de consumo autenticado de empleados via proxy en specs/009-reverse-proxy-stack/scripts/test-empleados-auth.ps1
- [x] T026 [US2] Crear script de caso negativo sin token para endpoint protegido en specs/009-reverse-proxy-stack/scripts/test-auth-negative.ps1

### Implementation for User Story 2

- [x] T027 [US2] Preservar Authorization y headers de forwarding en ubicaciones API del proxy en reverse-proxy/nginx.conf
- [x] T028 [US2] Asegurar propagacion Host, X-Real-IP, X-Forwarded-For y X-Forwarded-Proto en reverse-proxy/nginx.conf
- [x] T029 [US2] Actualizar comandos de quickstart para login y request autenticada con codigo esperado 200 en specs/009-reverse-proxy-stack/quickstart.md
- [x] T030 [US2] Documentar comprobacion de respuesta 401 para token ausente/invalido en specs/009-reverse-proxy-stack/quickstart.md

**Checkpoint**: US2 funcional y verificable en forma independiente.

---

## Phase 5: User Story 3 - Operacion Interna Del Stack Sin Exposicion Innecesaria (Priority: P3)

**Goal**: Dejar solo el proxy publicado y mantener servicios internos en red dedicada.

**Independent Test**: docker compose ps muestra solo reverse-proxy con puertos publicados; db/api/frontend solo red interna.

### Tests for User Story 3

- [x] T031 [P] [US3] Crear script de validacion de puertos publicados por servicio en specs/009-reverse-proxy-stack/scripts/test-exposed-ports.ps1
- [x] T032 [P] [US3] Crear script de validacion de red interna comun entre servicios en specs/009-reverse-proxy-stack/scripts/test-internal-network.ps1
- [x] T033 [US3] Crear script de validacion de override de debug DB en specs/009-reverse-proxy-stack/scripts/test-db-debug-override.ps1

### Implementation for User Story 3

- [x] T034 [US3] Definir servicio db sin publicacion de puertos en compose base en docker-compose.yml
- [x] T035 [US3] Definir publicacion opcional de puerto db solo en override de debug en docker-compose.debug-db.yml
- [x] T036 [US3] Actualizar quickstart con pasos de validacion de red y exposicion de puertos en specs/009-reverse-proxy-stack/quickstart.md
- [x] T037 [US3] Documentar riesgo operativo y recomendacion de no exponer DB en produccion en specs/009-reverse-proxy-stack/quickstart.md

**Checkpoint**: US3 funcional y verificable en forma independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad, documentacion final y ejecucion integral.

- [x] T038 [P] Consolidar guia de arquitectura final del stack con diagrama textual en specs/009-reverse-proxy-stack/plan.md
- [x] T039 [P] Consolidar contrato de enrutamiento y validacion final en specs/009-reverse-proxy-stack/contracts/reverse-proxy-routing-contract.md
- [x] T040 Ejecutar build limpio y registrar evidencias de estado healthy en specs/009-reverse-proxy-stack/quickstart.md
- [x] T041 Ejecutar bateria completa de pruebas HTTP y registrar codigos obtenidos en specs/009-reverse-proxy-stack/quickstart.md
- [x] T042 Validar ausencia de errores de CORS en flujo login y llamadas autenticadas y registrar resultado en specs/009-reverse-proxy-stack/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- Phase 1 Setup: sin dependencias.
- Phase 2 Foundational: depende de Phase 1 y bloquea todas las historias.
- Phase 3 US1: depende de Phase 2.
- Phase 4 US2: depende de Phase 2; recomendado despues de US1 para reutilizar routing estable.
- Phase 5 US3: depende de Phase 2; puede ejecutarse en paralelo con US1/US2.
- Phase 6 Polish: depende de US1, US2 y US3 completadas.

### User Story Dependencies

- US1 (P1): inicia tras Foundational y no depende de otras historias.
- US2 (P2): inicia tras Foundational y depende funcionalmente del enrutamiento API de US1 para validacion completa.
- US3 (P3): inicia tras Foundational y no depende de US1/US2 para validar exposicion/red.

### Within Each User Story

- Primero tareas de prueba de la historia.
- Luego configuracion de proxy/compose o ajustes de codigo.
- Finalmente actualizacion de quickstart y evidencia.

### Parallel Opportunities

- Setup en paralelo: T004 y T005.
- Foundational en paralelo: T012 y T013.
- US1 tests en paralelo: T016 y T017.
- US2 tests en paralelo: T024 y T025.
- US3 tests en paralelo: T031 y T032.
- Polish en paralelo: T038 y T039.

---

## Parallel Example: User Story 1

- Task: T016 [US1] Verificacion de GET / en specs/009-reverse-proxy-stack/scripts/test-root.ps1
- Task: T017 [US1] Verificacion de GET /swagger-ui en specs/009-reverse-proxy-stack/scripts/test-swagger.ps1

---

## Parallel Example: User Story 2

- Task: T024 [US2] Login y token en specs/009-reverse-proxy-stack/scripts/test-auth-login.ps1
- Task: T025 [US2] Endpoint autenticado en specs/009-reverse-proxy-stack/scripts/test-empleados-auth.ps1

---

## Parallel Example: User Story 3

- Task: T031 [US3] Puertos publicados por servicio en specs/009-reverse-proxy-stack/scripts/test-exposed-ports.ps1
- Task: T032 [US3] Red interna compartida en specs/009-reverse-proxy-stack/scripts/test-internal-network.ps1

---

## Implementation Strategy

### MVP First (US1)

1. Completar Setup.
2. Completar Foundational.
3. Completar US1.
4. Validar US1 de forma independiente con scripts de raiz, swagger y fallback SPA.

### Incremental Delivery

1. Setup + Foundational.
2. Entregar US1 (entrada unica y routing).
3. Entregar US2 (login y llamadas autenticadas).
4. Entregar US3 (aislamiento interno y exposicion minima).
5. Cerrar con Polish y evidencias finales.

### Parallel Team Strategy

1. Infra team: compose, red y proxy base (T006-T011, T019-T023).
2. Front team: base URL/interceptor/specs frontend (T012-T015).
3. QA/Ops team: scripts de validacion y reporte final (T016-T018, T024-T026, T031-T033, T040-T042).

---

## Notes

- Todas las tareas siguen formato estricto de checklist con ID secuencial y ruta de archivo.
- Las tareas marcadas con [P] no comparten archivo ni bloqueo directo.
- Cada historia mantiene criterio de prueba independiente.
- El alcance respeta endpoint unico, Bearer intacto, headers de proxy y red interna dedicada.
