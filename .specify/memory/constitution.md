<!--
Sync Impact Report
Version change: 2.0.0 -> 2.1.0
Modified principles:
- I. Full-Stack Baseline (Angular 22 LTS + Spring Boot 3 + Java 17) -> I. Baseline Tecnico y Separacion de Repositorio
- II. Token-Based Authentication & Session Security -> II. Autenticacion Bearer y Ciclo de Sesion
- III. API-First Contract & Data Consistency -> III. Contrato API y Consistencia de Datos
- IV. Feature-Based Frontend Architecture -> IV. Arquitectura Frontend por Features
- V. Quality Gates, Testing, and Operability -> V. Calidad, Pruebas y Operabilidad
Added sections:
- Matriz de Autorizacion y Comportamiento UI
Removed sections: None
Templates requiring updates:
- .specify/templates/plan-template.md ✅ updated
- .specify/templates/spec-template.md ✅ updated
- .specify/templates/tasks-template.md ✅ updated
- .specify/templates/commands/*.md ⚠ pending (directory not present)
- specs/001-crud-empleados/quickstart.md ✅ reviewed (no change required)
- specs/003-crud-departamentos/quickstart.md ✅ reviewed (no change required)
Follow-up TODOs: None
-->

# DSWO2-Practica1 Full-Stack Constitution

## Core Principles

### I. Baseline Tecnico y Separacion de Repositorio
- El frontend DEBE existir como proyecto Angular independiente en `/frontend`; el backend DEBE permanecer en `/backend`.
- El frontend DEBE usar Angular 22 LTS, APIs standalone, TypeScript estricto y Angular Material.
- El backend DEBE compilar con Spring Boot 3.2+ y Java 17 LTS.
- Cualquier desviacion de este baseline DEBE registrarse en un ADR aprobado por dos mantenedores antes de implementarse.
*Rationale: mantener proyectos separados y versiones fijadas evita deriva tecnica y reduce friccion de integracion.*

### II. Autenticacion Bearer y Ciclo de Sesion
- La autenticacion DEBE ejecutarse mediante `POST /auth/login`.
- El frontend DEBE almacenar token y rol (`ADMIN` o `USER`) en `localStorage` y limpiar ambos al cerrar sesion o detectar `401` invalido.
- Un interceptor HTTP DEBE adjuntar `Authorization: Bearer <token>` a solicitudes protegidas.
- Las rutas privadas DEBEN protegerse con `AuthGuard` y redirigir a `/login` cuando no exista sesion valida.
*Rationale: un flujo de sesion centralizado reduce errores de seguridad y de navegacion.*

### III. Contrato API y Consistencia de Datos
- El frontend DEBE consumir la API por `HttpClient` contra `http://localhost:8080` mediante una estrategia unica de base URL configurable.
- Los endpoints de empleados y departamentos DEBEN mantenerse sincronizados con OpenAPI en el mismo cambio que altera comportamiento.
- La relacion "empleado pertenece a departamento" DEBE respetarse en formularios y validaciones de persistencia.
- Las respuestas de error DEBEN mapearse de forma centralizada para evitar parsing ad hoc repetido por feature.
*Rationale: contratos claros y consistencia de datos previenen regresiones entre UI y backend.*

### IV. Arquitectura Frontend por Features
- La estructura `src/app/core`, `src/app/features/{auth,empleados,departamentos}` y `src/app/shared` ES OBLIGATORIA.
- La logica de negocio/integracion DEBE residir en servicios; los componentes DEBEN enfocarse en presentacion e interaccion.
- Los formularios DEBEN ser reactivos.
- Las vistas de datos DEBEN usar tablas de Angular Material y notificaciones uniformes por snackbar.
*Rationale: limites de arquitectura explicitos facilitan mantenimiento y entrega incremental.*

### V. Calidad, Pruebas y Operabilidad
- Todo cambio de backend DEBE pasar pruebas unitarias, de contrato e integracion.
- Todo cambio de frontend DEBE incluir pruebas unitarias para servicios, guard e interceptor cuando dichos artefactos cambien.
- Seguridad negativa DEBE probar escenarios sin token, token invalido y permisos insuficientes por rol.
- La ejecucion local y CI DEBEN ser reproducibles con `docker-compose.yml` y backend accesible en `http://localhost:8080`.
*Rationale: quality gates verificables sostienen estabilidad de extremo a extremo.*

## Technology Stack Guardrails
- **Frontend**: Angular 22 LTS, Angular Material, Reactive Forms, HttpClient, AuthGuard e HTTP interceptor.
- **Backend**: Java 17, Spring Boot 3, Spring Security, Spring Data/JPA y Flyway.
- **Persistencia**: PostgreSQL como fuente de verdad, con migraciones versionadas.
- **Autenticacion**: Bearer token emitido por `/auth/login`; no se permite alternativa para rutas protegidas.
- **Topologia**: `frontend/` y `backend/` se gestionan como proyectos independientes con scripts propios.

## Matriz de Autorizacion y Comportamiento UI
1. **ADMIN**: DEBE tener acceso completo de CRUD en empleados y departamentos.
2. **USER**: DEBE tener acceso de solo lectura; crear, editar y eliminar DEBE ocultarse o deshabilitarse en UI.
3. **Rutas**: `/login` es publica; `/empleados` y `/departamentos` DEBEN requerir sesion.
4. **Visibilidad de acciones**: La UI DEBE adaptarse al rol y no mostrar controles no permitidos.
5. **Autoridad final**: La validacion de seguridad real DEBE ejecutarse en backend aunque la UI oculte acciones.

## Development Workflow & Quality Gates
1. **Constitution Check**: Cada plan DEBE validar separacion frontend/backend, arquitectura por features y reglas de sesion/rol.
2. **Diseno de API**: Todo cambio funcional DEBE reflejarse en contratos y pruebas de integracion/contrato.
3. **Implementacion**: Tareas DEBEN organizarse por historias y cubrir auth, autorizacion y flujo empleado-departamento.
4. **Verificacion**: Antes de merge DEBEN ejecutarse pruebas requeridas y validarse flujo UI por rol.
5. **Revision**: Se requieren dos revisores, incluyendo uno para seguridad/integracion.

## Governance
- Esta constitucion prevalece sobre cualquier otra guia conflictiva dentro del repositorio.
- Las enmiendas siguen Semantic Versioning: PATCH para aclaraciones, MINOR para reglas o secciones nuevas, MAJOR para eliminaciones o inversiones normativas.
- Toda enmienda DEBE actualizar este archivo y las plantillas dependientes en el mismo cambio.
- La conformidad DEBE revisarse en cada PR y en una revision trimestral de arquitectura.
- Incumplimientos DEBEN bloquear merges salvo excepcion documentada, acotada en tiempo y aprobada por dos mantenedores.

**Version**: 2.1.0 | **Ratified**: 2026-02-25 | **Last Amended**: 2026-03-24
