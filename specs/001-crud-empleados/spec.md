# Feature Specification: CRUD de Empleados

**Feature Branch**: `001-crud-empleados`  
**Created**: 2026-02-25  
**Status**: Draft  
**Input**: User description: "crea un crud de empleados con los campos clave, nombre, direccion y telefono. Donde clave sea el PK y nombre, direccion y telefono sea de 100 caracteres" + ajuste: `clave` con prefijo `EMP-` seguido de autonúmero como PK compuesta.

## User Scenarios & Testing *(mandatory)*

<!--
  IMPORTANT: User stories should be PRIORITIZED as user journeys ordered by importance.
  Each user story/journey must be INDEPENDENTLY TESTABLE - meaning if you implement just ONE of them,
  you should still have a viable MVP (Minimum Viable Product) that delivers value.
  
  Assign priorities (P1, P2, P3, etc.) to each story, where P1 is the most critical.
  Think of each story as a standalone slice of functionality that can be:
  - Developed independently
  - Tested independently
  - Deployed independently
  - Demonstrated to users independently
-->

### User Story 1 - Registrar empleados (Priority: P1)

Como usuario administrador, quiero registrar empleados con su información básica para iniciar el control del personal.

**Why this priority**: Sin alta de empleados no existe base de datos útil para las operaciones restantes del CRUD.

**Independent Test**: Puede probarse creando un empleado nuevo y verificando que quede almacenado con `clave`, `nombre`, `direccion` y `telefono` válidos.

**Acceptance Scenarios**:

1. **Given** que se solicita registrar un empleado con datos válidos, **When** se crea el registro, **Then** el sistema genera automáticamente una `clave` en formato `EMP-<autonumero>` y confirma la creación.
2. **Given** que el usuario envía `nombre`, `direccion` o `telefono` con más de 100 caracteres, **When** intenta registrar el empleado, **Then** el sistema rechaza la operación con mensaje de validación claro.
3. **Given** que ocurre una colisión al generar la secuencia de `clave`, **When** el sistema intenta persistir el registro, **Then** el sistema reintenta la generación hasta 3 veces y, si no logra unicidad, responde error de generación controlado.

---

### User Story 2 - Consultar empleados (Priority: P2)

Como usuario administrador, quiero consultar un empleado por su `clave` y listar empleados registrados para revisar información existente.

**Why this priority**: La consulta permite usar y validar la información capturada en la historia P1.

**Independent Test**: Puede probarse recuperando un empleado existente por su `clave` y ejecutando una consulta de listado general.

**Acceptance Scenarios**:

1. **Given** que existe un empleado con una `clave` específica, **When** consulto por esa `clave`, **Then** el sistema devuelve los datos completos del empleado.
2. **Given** que no existe un empleado con la `clave` solicitada, **When** hago la consulta, **Then** el sistema responde que el recurso no fue encontrado.
3. **Given** que existen empleados registrados, **When** solicito el listado, **Then** el sistema devuelve la colección de empleados con los cuatro campos definidos.

---

### User Story 3 - Actualizar y eliminar empleados (Priority: P3)

Como usuario administrador, quiero actualizar y eliminar empleados para mantener la información vigente y limpia.

**Why this priority**: Estas acciones completan el ciclo CRUD una vez que ya existen datos creados y consultables.

**Independent Test**: Puede probarse actualizando los campos editables de un empleado existente y posteriormente eliminándolo por `clave`.

**Acceptance Scenarios**:

1. **Given** que existe un empleado, **When** actualizo `nombre`, `direccion` y/o `telefono` con valores válidos, **Then** el sistema guarda los cambios.
2. **Given** que intento actualizar `nombre`, `direccion` o `telefono` con más de 100 caracteres, **When** envío la actualización, **Then** el sistema rechaza la operación con error de validación.
3. **Given** que existe un empleado con una `clave` válida, **When** solicito su eliminación, **Then** el sistema elimina el registro y confirma la operación.

---

### Edge Cases

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right edge cases.
-->

- Intento de crear o actualizar con `nombre`, `direccion` o `telefono` vacíos.
- Intento de crear enviando `clave` manualmente distinta al patrón `EMP-<autonumero>`.
- Desalineación de la secuencia autonumérica (saltos o reinicios) que provoque colisión de `clave`.
- Eliminación de una `clave` inexistente.
- Consulta de listado cuando no existen empleados registrados.

## Requirements *(mandatory)*

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right functional requirements.
-->

### Functional Requirements

- **FR-001**: El sistema DEBE permitir crear empleados con los campos obligatorios `nombre`, `direccion` y `telefono`.
- **FR-002**: El campo `clave` DEBE ser generado automáticamente por el sistema con formato `EMP-<autonumero>` y cumplir el patrón `^EMP-[0-9]+$`.
- **FR-003**: Los campos `nombre`, `direccion` y `telefono` DEBEN aceptar como máximo 100 caracteres cada uno.
- **FR-004**: El sistema DEBE rechazar operaciones de creación o actualización cuando `nombre`, `direccion` o `telefono` excedan 100 caracteres.
- **FR-005**: El sistema DEBE permitir consultar un empleado por `clave`.
- **FR-006**: El sistema DEBE permitir listar los empleados registrados.
- **FR-007**: El sistema DEBE permitir actualizar los campos `nombre`, `direccion` y `telefono` de un empleado existente identificado por `clave`.
- **FR-008**: El sistema DEBE permitir eliminar un empleado existente por `clave`.
- **FR-009**: El sistema DEBE devolver errores con estructura uniforme que incluya `type`, `title`, `status`, `detail` e `instance` cuando una `clave` no exista en operaciones de consulta, actualización o eliminación.
- **FR-010**: El sistema DEBE persistir la información de empleados para que esté disponible entre sesiones del sistema.
- **FR-011**: El sistema DEBE aplicar política de colisión para generación de `clave` con máximo 3 reintentos y registrar el evento cuando se alcance el límite.
- **FR-012**: El sistema NO DEBE permitir modificar manualmente `clave` en operaciones de actualización.

### Key Entities *(include if feature involves data)*

- **Empleado**: Representa a una persona registrada en el sistema con los atributos `clave` (PK compuesta lógica: prefijo `EMP-` + autonúmero), `nombre` (máximo 100), `direccion` (máximo 100) y `telefono` (máximo 100).

## Assumptions

- El CRUD será usado por usuarios internos con permisos de administración de empleados.
- La `clave` se genera automáticamente y de forma secuencial con patrón `EMP-<autonumero>` (autonúmero con 1 a 10 dígitos).
- La `clave` se trata como dato de negocio estable y no editable una vez creado el empleado.
- El alcance de esta feature no incluye paginación, filtros avanzados ni importación masiva.

## Success Criteria *(mandatory)*

<!--
  ACTION REQUIRED: Define measurable success criteria.
  These must be technology-agnostic and measurable.
-->

### Measurable Outcomes

- **SC-001**: El 100% de los intentos de alta con datos válidos registran un empleado correctamente en menos de 3 segundos.
- **SC-002**: El 100% de los intentos con `nombre`, `direccion` o `telefono` mayores a 100 caracteres son rechazados con un mensaje de validación entendible.
- **SC-003**: Al menos el 95% de las consultas por `clave` existente devuelven el empleado correcto en menos de 2 segundos.
- **SC-004**: El 100% de las operaciones de actualización y eliminación sobre `clave` existente reflejan cambios consistentes al consultar de nuevo el registro.
- **SC-005**: El 100% de los empleados creados reciben `clave` válida con formato `EMP-<autonumero>` sin duplicados.
