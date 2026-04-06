# Data Model: CRUD de Departamentos

## Entity: Departamento

### Description
Representa una unidad organizacional a la que pertenecen empleados.

### Fields
- `clave` (string, PK)
  - Reglas: provista por usuario, obligatoria, única, inmutable, longitud máxima 50.
- `nombre` (string)
  - Reglas: obligatorio, no vacío, longitud máxima 50.
- `created_at` (timestamp)
  - Reglas: generado por sistema, no editable.
- `updated_at` (timestamp)
  - Reglas: actualizado por sistema en cada modificación.

### Relationships
- `Departamento (1) -> (N) Empleado`.
- Un departamento puede tener cero o más empleados.

## Entity: Empleado (impactado)

### Description
Entidad existente del sistema que incorporará la referencia a departamento.

### New/Changed Fields
- `departamento_clave` (string, FK -> `departamento.clave`)
  - Reglas: para nuevas altas y actualizaciones debe referenciar un departamento existente.
  - Reglas de migración: empleados existentes pueden permanecer temporalmente sin asignación automática hasta regularización manual.

### Relationships
- `Empleado (N) -> (1) Departamento`.
- Un empleado pertenece a un solo departamento cuando está asignado.

## Validation Rules (Business + Persistence)
- No se permite crear dos departamentos con la misma `clave`.
- `nombre` de departamento no puede exceder 50 caracteres.
- No se puede eliminar un departamento con empleados asociados (restrict).
- Si se intenta asociar un empleado a un departamento inexistente, la operación falla por integridad de referencia.
- La `clave` de departamento no se modifica una vez creado el registro.

## State Transitions

### Departamento
- `CREATED`: departamento persistido por primera vez.
- `UPDATED`: se modifica `nombre`.
- `DELETION_BLOCKED`: intento de borrado con empleados asociados.
- `DELETED`: eliminación efectiva cuando no hay empleados vinculados.

### Empleado (respecto a departamento)
- `UNASSIGNED_LEGACY`: empleado existente previo a la feature sin asignación automática.
- `ASSIGNED`: empleado vinculado a un departamento válido.
- `REASSIGNED`: empleado movido de un departamento a otro válido.
