# Phase 0 Research: CRUD de Empleados

## Decision 1: Estrategia de generación de `clave`
- **Decision**: Generar `clave` automáticamente con formato `EMP-<autonumero>` (patrón `^EMP-[0-9]+$`), almacenada como `VARCHAR(50)` e inmutable tras creación.
- **Rationale**: Mantiene identificadores de negocio legibles, centraliza la generación en backend y evita dependencia de entrada manual del cliente.
- **Alternatives considered**:
  - `BIGSERIAL` como PK expuesta: simplifica DB, pero no cumple el requerimiento de prefijo `EMP-`.
  - `UUID`: robusto técnicamente, pero no cumple formato requerido y reduce legibilidad operativa.

## Decision 2: Validaciones de longitud y obligatoriedad
- **Decision**: Aplicar doble validación en API (`@NotBlank`, `@Size(max=100)`) y en base de datos (`VARCHAR(100) NOT NULL`) para `nombre`, `direccion`, `telefono`.
- **Rationale**: El fail-fast en API mejora UX y las restricciones en DB garantizan integridad ante entradas externas o bypass de capa de aplicación.
- **Alternatives considered**:
  - Validar solo en backend: deja brechas de integridad en persistencia.
  - Validar solo en DB: produce errores tardíos y menos claros para consumidores API.

## Decision 3: Estrategia de actualización de empleado
- **Decision**: Permitir actualización únicamente de `nombre`, `direccion` y `telefono`; `clave` no editable.
- **Rationale**: La PK inmutable simplifica trazabilidad y evita cascadas de actualización en relaciones futuras.
- **Alternatives considered**:
  - Permitir cambiar `clave`: aumenta complejidad de consistencia y riesgos de integridad.

## Decision 4: Contrato HTTP para errores
- **Decision**: Usar `application/problem+json` (RFC 7807) para validaciones, duplicados y no encontrados.
- **Rationale**: Uniformidad de errores facilita integración de clientes y pruebas de contrato.
- **Alternatives considered**:
  - Errores ad-hoc por endpoint: inconsistentes y difíciles de mantener.

## Decision 5: Pruebas mínimas por nivel
- **Decision**: Definir pruebas unitarias de servicio, pruebas de contrato con MockMvc y pruebas de integración con Testcontainers PostgreSQL.
- **Rationale**: Cubre lógica de negocio, forma del contrato HTTP y comportamiento real contra PostgreSQL.
- **Alternatives considered**:
  - Solo unitarias: no validan contrato ni integración real.
  - Solo integración: ciclos lentos y diagnóstico más costoso.

## Decision 6: Documentación de API
- **Decision**: Publicar contrato OpenAPI 3.0 con endpoints CRUD en `/api/v1/empleados` y Swagger UI en `/swagger-ui`.
- **Rationale**: Cumple constitución y habilita validación temprana de consumidores.
- **Alternatives considered**:
  - Documentación manual en README: propensa a desactualización y sin validación estructural.
