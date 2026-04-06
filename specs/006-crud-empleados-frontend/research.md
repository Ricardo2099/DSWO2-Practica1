# Phase 0 Research: CRUD de Empleados Frontend

## Decision 1: Estrategia de paginacion del listado
- **Decision**: Implementar paginacion server-side en `GET /api/v1/empleados` con parametros `page` y `size`.
- **Rationale**: Escala mejor para catalogos de empleados medianos/grandes y reduce carga en cliente.
- **Alternatives considered**:
  - Sin paginacion: simple, pero degrada rendimiento al crecer datos.
  - Paginacion client-side: requiere cargar todos los datos y rompe objetivo de escalabilidad.

## Decision 2: Contrato de respuesta paginada
- **Decision**: Consumir un objeto paginado con `content`, `totalElements`, `totalPages`, `number` y `size`.
- **Rationale**: Alinea frontend con contratos tipicos de Spring y simplifica mapeo en Material Paginator.
- **Alternatives considered**:
  - Arreglo simple con headers: menos tipado y mayor acoplamiento a infraestructura HTTP.
  - Contrato custom minimo `{items,total}`: menos metadatos para experiencia de paginacion completa.

## Decision 3: Carga de departamentos para formulario
- **Decision**: Poblar selector desde `GET /api/v1/departamentos` al entrar a vista de empleados.
- **Rationale**: Garantiza datos actualizados y evita drift entre catalogos hardcodeados y backend.
- **Alternatives considered**:
  - Catalogo local estatico: rapido pero inconsistente con cambios reales de negocio.
  - Datos embebidos en listado de empleados: incrementa acoplamiento y payloads innecesarios.

## Decision 4: Matriz de autorizacion de UI
- **Decision**: `ADMIN` puede listar/crear/eliminar y `USER` solo listar; UI oculta/deshabilita acciones no permitidas.
- **Rationale**: Cumple constitucion y mejora experiencia evitando acciones que terminarian en 403.
- **Alternatives considered**:
  - Mostrar todo y manejar solo errores: mala UX y ruido operativo.
  - Bloquear vista completa para USER: contradice requerimiento de solo lectura.

## Decision 5: Manejo de errores de operaciones
- **Decision**: Centralizar feedback de errores y exitos en snackbar desde servicios/componentes de feature.
- **Rationale**: Comportamiento uniforme y menor duplicacion en multiples acciones asincronas.
- **Alternatives considered**:
  - Mensajes inline solamente: no cubren bien fallas globales de red/autorizacion.
  - Alertas nativas del navegador: experiencia inconsistente y poco control visual.
