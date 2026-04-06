# Phase 0 Research: CRUD de Departamentos Frontend

## Decision 1: Estrategia de integración API
- **Decision**: Usar un servicio único de departamentos en frontend (`getDepartamentos`, `getDepartamento`, `createDepartamento`, `updateDepartamento`, `deleteDepartamento`) con base URL centralizada `http://localhost:8080`.
- **Rationale**: Evita duplicación de llamadas HTTP y asegura consistencia de headers, errores y rutas.
- **Alternatives considered**:
  - Llamadas HTTP directas desde componentes: aumenta acoplamiento y dificulta pruebas.
  - Múltiples servicios fragmentados por operación: sobre-diseño para alcance actual.

## Decision 2: Edición con clave inmutable
- **Decision**: En modo edición solo se permite modificar `nombre`; `clave` queda en solo lectura.
- **Rationale**: La clave identifica el recurso en la URL y mantenerla inmutable reduce conflictos de identidad.
- **Alternatives considered**:
  - Permitir editar `clave`: complica integridad y sincronización de referencias.
  - Bloquear toda edición: no cumple el alcance de mantenimiento del catálogo.

## Decision 3: Política de eliminación con asociaciones
- **Decision**: Bloquear eliminación de departamentos con empleados asociados y mostrar mensaje claro al usuario.
- **Rationale**: Preserva consistencia con la relación de negocio empleado-departamento.
- **Alternatives considered**:
  - Eliminación en cascada: riesgo alto de pérdida de datos.
  - Eliminación silenciosa fallida: mala UX y diagnósticos pobres.

## Decision 4: Manejo de conflicto de concurrencia (409)
- **Decision**: Ante `PUT` con respuesta 409, recargar datos vigentes del departamento, informar conflicto y pedir confirmación para reintentar.
- **Rationale**: Evita sobreescrituras silenciosas y permite al usuario decidir con contexto actualizado.
- **Alternatives considered**:
  - Mostrar error genérico y cerrar formulario: rompe flujo y aumenta fricción.
  - Ignorar conflicto y forzar guardado: compromete consistencia de datos.

## Decision 5: Paginación del listado
- **Decision**: Implementar paginación en cliente sobre el resultado de `GET /api/v1/departamentos`.
- **Rationale**: Balancea simplicidad de implementación y rendimiento esperado para el tamaño de catálogo inicial.
- **Alternatives considered**:
  - Sin paginación: mala usabilidad al crecer el listado.
  - Paginación en servidor: requiere ampliar contrato backend fuera del alcance inmediato.

## Decision 6: Estrategia de pruebas
- **Decision**: Cubrir frontend con pruebas unitarias de servicio y componente de departamentos; validar reglas de rol y manejo de errores (401/403/409).
- **Rationale**: Asegura el comportamiento crítico del flujo sin depender de pruebas manuales.
- **Alternatives considered**:
  - Solo pruebas manuales: baja repetibilidad y alto riesgo de regresión.
  - E2E únicamente: diagnóstico más costoso para fallos de lógica local.
