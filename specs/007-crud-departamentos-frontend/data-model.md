# Data Model: CRUD de Departamentos Frontend

## Entity: Departamento

### Description
Representa un departamento mostrado y gestionado desde la UI de catálogo.

### Fields
- `clave` (string)
  - Reglas: obligatorio en creación, único en backend, solo lectura en edición.
- `nombre` (string)
  - Reglas: obligatorio, editable en creación y edición, con validaciones de formato/longitud según contrato backend.

### Relationships
- Puede tener cero o más empleados asociados (relación mantenida por backend).
- Cuando existen asociados, la eliminación debe bloquearse con mensaje explícito.

## Entity: SesionUsuario

### Description
Estado de autenticación consumido por guard/interceptor/UI para habilitar navegación y acciones.

### Fields
- `token` (string)
  - Reglas: obligatorio para consumir endpoints protegidos.
- `rol` (enum: `ADMIN` | `USER`)
  - Reglas: define permisos visibles/ejecutables en la vista de departamentos.

## View Model: DepartamentoFormState

### Description
Estado local del formulario para diferenciar creación, edición, conflicto y recuperación.

### Fields
- `mode` (enum: `create` | `edit`)
- `currentClave` (string | null)
- `isSubmitting` (boolean)
- `hasConflict` (boolean)
- `pendingRetry` (boolean)

### Validation Rules
- En `create`: `clave` y `nombre` requeridos.
- En `edit`: `clave` no editable, `nombre` requerido.
- En conflicto `409`: `hasConflict=true`, recarga de datos y confirmación explícita antes de reintentar.

## View Model: DepartamentoListState

### Description
Estado de tabla para consulta y navegación paginada en cliente.

### Fields
- `items` (Departamento[])
- `pageIndex` (number)
- `pageSize` (number)
- `total` (number)
- `isLoading` (boolean)
- `errorMessage` (string | null)

### State Transitions
- `IDLE -> LOADING -> READY`
- `READY -> SUBMITTING_CREATE|SUBMITTING_UPDATE|SUBMITTING_DELETE`
- `SUBMITTING_* -> READY` (éxito con refresco)
- `SUBMITTING_UPDATE -> CONFLICT` (409)
- `CONFLICT -> READY` (tras recarga + confirmación + reintento)
- Cualquier estado `-> ERROR` ante fallas no recuperables, manteniendo feedback por snackbar.
