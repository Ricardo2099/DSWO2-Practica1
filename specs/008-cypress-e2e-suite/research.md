# Phase 0 Research: Suite E2E de Sistema Completo

## Decision 1: Estrategia de autenticacion para pruebas E2E
- **Decision**: Usar autenticacion por API real (`POST /auth/login`) en comandos custom (`loginAdmin`, `loginUser`) y mantener una prueba dedicada de login por UI.
- **Rationale**: Reduce flakiness en el set-up de escenarios largos y mantiene cobertura de experiencia real de autenticacion.
- **Alternatives considered**:
  - Login solo por UI en todos los tests: mayor costo y fragilidad por cambios visuales.
  - Token estatico pregenerado: invalida cobertura real del backend y expira con facilidad.

## Decision 2: Independencia y limpieza de datos de prueba
- **Decision**: Generar datos unicos por test (sufijo temporal/uuid) y limpiar recursos creados dentro del mismo test.
- **Rationale**: Evita colisiones entre corridas, elimina dependencia de estado previo y mejora repetibilidad.
- **Alternatives considered**:
  - Datos fijos compartidos: alto riesgo de conflictos por duplicados.
  - Dependencia de datos precargados: baja portabilidad y resultados no deterministas.

## Decision 3: Validacion de autorizacion por rol
- **Decision**: Verificar doble capa para `USER`: bloqueo/ocultacion en UI y rechazo visible 403 al forzar accion no permitida.
- **Rationale**: Asegura UX consistente y confirma al backend como autoridad final de seguridad.
- **Alternatives considered**:
  - Validar solo UI: no demuestra enforcement real de backend.
  - Validar solo backend: pierde garantia de comportamiento esperado en interfaz.

## Decision 4: Politica de credenciales E2E
- **Decision**: Inyectar credenciales mediante variables de entorno Cypress (`CYPRESS_ADMIN_EMAIL`, `CYPRESS_ADMIN_PASSWORD`, `CYPRESS_USER_EMAIL`, `CYPRESS_USER_PASSWORD`) con fallback local documentado.
- **Rationale**: Protege secretos en repositorio y habilita ejecucion local/CI con minima friccion.
- **Alternatives considered**:
  - Hardcode en fixtures/comandos: riesgo de exposicion y rotacion costosa.
  - Archivo versionado con secretos: incumple practica de seguridad.

## Decision 5: Estrategia de evidencia visual
- **Decision**: Mantener video de cada corrida, screenshots automaticos en fallo y screenshots manuales en hitos de negocio (post-login, crear departamento, crear empleado, eliminar empleado).
- **Rationale**: Balancea trazabilidad funcional y costo de almacenamiento/tiempo.
- **Alternatives considered**:
  - Captura en cada paso: sobredimensiona evidencia y ralentiza ejecucion.
  - Solo evidencia en fallo: insuficiente para demostracion funcional positiva.

## Decision 6: Selectores y robustez de UI tests
- **Decision**: Estandarizar uso de `data-testid` para elementos de accion/tabla/formulario en los flujos cubiertos.
- **Rationale**: Reduce acoplamiento a cambios cosmeticos y mejora mantenibilidad de la suite.
- **Alternatives considered**:
  - Selectores por texto/clases CSS: alta sensibilidad a cambios de copy/estilos.
  - XPath complejo: baja legibilidad y mantenimiento costoso.

## Decision 7: Alcance de contratos de la feature
- **Decision**: Documentar contrato E2E funcional (matriz de escenarios, precondiciones, evidencia y criterios de salida) y esquema de configuracion de entorno Cypress.
- **Rationale**: Hace verificable la ejecucion extremo a extremo para equipos de desarrollo y QA.
- **Alternatives considered**:
  - Contrato informal en README: trazabilidad limitada.
  - Solo OpenAPI backend: no cubre comportamiento E2E de frontend.
