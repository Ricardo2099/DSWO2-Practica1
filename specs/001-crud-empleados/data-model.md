# Data Model: CRUD de Empleados

## Entity: Empleado

### Description
Representa un empleado administrado por el sistema.

### Fields
- `clave` (string, PK compuesta lógica)
  - Reglas: generada automáticamente por sistema, formato obligatorio `EMP-<autonumero>`, longitud máxima 50, única, inmutable.
- `clave_prefijo` (string, componente de PK lógica)
  - Reglas: valor fijo `EMP`.
- `clave_autonumero` (integer, componente de PK lógica)
  - Reglas: secuencial, incremental, no reutilizable.
- `nombre` (string)
  - Reglas: obligatorio, no vacío, longitud máxima 100.
- `direccion` (string)
  - Reglas: obligatorio, no vacío, longitud máxima 100.
- `telefono` (string)
  - Reglas: obligatorio, no vacío, longitud máxima 100.
- `created_at` (timestamp)
  - Reglas: generado por sistema, no editable.
- `updated_at` (timestamp)
  - Reglas: actualizado por sistema en cada modificación.

### Relationships
- No aplica en esta versión (entidad independiente).

## Validation Rules (Business + Persistence)
- `clave` debe ser única en todo el sistema y cumplir patrón `EMP-[0-9]+`.
- El cliente no envía `clave` en creación; el sistema la genera automáticamente.
- `nombre`, `direccion` y `telefono` no pueden exceder 100 caracteres.
- Operaciones `GET/PUT/DELETE` sobre `clave` inexistente devuelven estado de no encontrado.
- Si la generación de `clave` colisiona, el sistema reintenta y garantiza unicidad sin exponer duplicados.

## State Transitions
- `CREATED`: empleado persistido por primera vez.
- `UPDATED`: se modifica al menos uno de los campos editables (`nombre`, `direccion`, `telefono`).
- `DELETED`: registro eliminado lógicamente del dominio (físicamente removido de tabla en esta versión).
