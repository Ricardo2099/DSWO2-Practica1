# Phase 0 Research: CRUD de Departamentos

## Decision 1: Identificador de negocio de Departamento
- **Decision**: Usar `clave` como identificador primario de negocio provisto por el usuario en creación; será único, inmutable y no autogenerado.
- **Rationale**: La clave semántica mejora trazabilidad operativa (ej. `IT`, `RRHH`) y fue una clarificación explícita del alcance funcional.
- **Alternatives considered**:
  - Autogeneración (`DEPT-<n>`): simplifica backend, pero contradice la decisión de negocio.
  - UUID: robusto técnicamente, pero menos legible para operación diaria.

## Decision 2: Integridad referencial Departamento-Empleado
- **Decision**: Implementar relación 1:N (Departamento -> Empleados) con clave foránea en empleados y política de borrado `restrict` para departamentos con empleados asociados.
- **Rationale**: Preserva consistencia de datos y evita huérfanos de negocio.
- **Alternatives considered**:
  - `cascade delete`: elimina empleados y genera alto riesgo de pérdida de datos.
  - `set null`: permitiría empleados sin departamento tras borrado, rompiendo la regla de relación objetivo.

## Decision 3: Estrategia para empleados existentes
- **Decision**: No reasignar automáticamente empleados existentes al introducir departamentos; la asignación inicial se realiza manualmente luego del despliegue.
- **Rationale**: Minimiza riesgo en migración y respeta el comportamiento acordado en clarificación.
- **Alternatives considered**:
  - Crear departamento por defecto y asignar masivamente: introduce semántica artificial.
  - Bloquear aplicación hasta completar migración: agrega fricción operativa innecesaria.

## Decision 4: Alcance de API y seguridad
- **Decision**: Exponer solo CRUD básico de departamentos (`POST`, `GET` lista, `GET` por clave, `PUT`, `DELETE`) y restringir su uso a administradores.
- **Rationale**: Cumple alcance definido, reduce complejidad inicial y mantiene consistencia con controles de seguridad del sistema.
- **Alternatives considered**:
  - Agregar filtros avanzados y endpoints de relación en esta fase: aumenta scope sin requerimiento prioritario.
  - Lectura pública: no alineado con la política actual de seguridad.

## Decision 5: Validación y errores de contrato
- **Decision**: Validar `nombre` con `maxLength=50` y no vacío; responder errores con esquema uniforme `application/problem+json` para validación, no encontrado, conflicto y restricciones de negocio.
- **Rationale**: Mejora interoperabilidad con clientes y reutiliza patrón de manejo de errores ya presente.
- **Alternatives considered**:
  - Mensajes ad-hoc por endpoint: inconsistencia y mayor costo de mantenimiento.

## Decision 6: Pruebas y observabilidad de salida
- **Decision**: Añadir pruebas unitarias de servicio, de contrato HTTP y de integración con PostgreSQL (Testcontainers), cubriendo alta, consulta, actualización, borrado restringido y autorización.
- **Rationale**: Cubre reglas de negocio críticas y evita regresiones sobre `Empleado`.
- **Alternatives considered**:
  - Solo pruebas unitarias: no validan contrato HTTP ni persistencia real.
  - Solo integración: más lentas y con diagnóstico menos granular.
