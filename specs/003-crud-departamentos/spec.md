# Feature Specification: CRUD de Departamentos

**Feature Branch**: `003-crud-departamentos`  
**Created**: 2026-03-12  
**Status**: Draft  
**Input**: User description: "Crea un CRUD para departamentos dentro del proyecto actual de Spring Boot. La entidad debe llamarse Departamento y tener los campos clave como llave primaria y nombre con un máximo de 50 caracteres. El departamento debe estar relacionado con los empleados, de forma que un departamento pueda tener varios empleados y cada empleado pertenezca a un departamento. Agrega la relación correspondiente en las entidades para que se cree la clave foránea en la base de datos. Genera la entidad, repository, service y controller con los endpoints CRUD básicos. Asegúrate de que los endpoints queden disponibles en Swagger y que la tabla se cree correctamente en PostgreSQL usando la configuración actual del proyecto."

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

### User Story 1 - Registrar departamentos (Priority: P1)

Como usuario administrador, quiero registrar departamentos para organizar a los empleados por área funcional.

**Why this priority**: Sin departamentos disponibles no puede establecerse la relación organizacional requerida con los empleados.

**Independent Test**: Puede probarse creando un departamento válido y verificando que quede disponible para consulta y asociación con empleados.

**Acceptance Scenarios**:

1. **Given** que se envía una `clave` única y un `nombre` válido, **When** se registra un departamento, **Then** el sistema guarda el departamento y confirma su creación.
2. **Given** que el `nombre` supera 50 caracteres, **When** se intenta registrar el departamento, **Then** el sistema rechaza la operación con un mensaje de validación claro.
3. **Given** que ya existe un departamento con la misma `clave`, **When** se intenta registrar otro con esa `clave`, **Then** el sistema rechaza la creación para evitar duplicados.

---

### User Story 2 - Consultar y actualizar departamentos (Priority: P2)

Como usuario administrador, quiero consultar y actualizar departamentos para mantener vigente la estructura organizacional.

**Why this priority**: Una vez creados los departamentos, es necesario poder revisarlos y corregir su información sin afectar el resto del sistema.

**Independent Test**: Puede probarse consultando el listado, recuperando un departamento por `clave` y modificando su `nombre` con un valor válido.

**Acceptance Scenarios**:

1. **Given** que existen departamentos registrados, **When** solicito el listado o una consulta por `clave`, **Then** el sistema devuelve la información correspondiente del departamento.
2. **Given** que un departamento existe, **When** actualizo su `nombre` con un valor válido, **Then** el sistema guarda el cambio y devuelve la información actualizada.
3. **Given** que la `clave` consultada o actualizada no existe, **When** se realiza la operación, **Then** el sistema responde que el recurso no fue encontrado.

---

### User Story 3 - Mantener la relación con empleados (Priority: P3)

Como usuario administrador, quiero que cada empleado pertenezca a un departamento y que cada departamento pueda agrupar varios empleados para reflejar la estructura real de la organización.

**Why this priority**: Esta relación completa el valor del CRUD de departamentos porque permite usarlo dentro del modelo actual de empleados.

**Independent Test**: Puede probarse asociando empleados a un departamento existente, verificando que la relación se conserve y que no se permita eliminar departamentos que aún tengan empleados vinculados.

**Acceptance Scenarios**:

1. **Given** que existe un departamento y existen empleados, **When** se establece la pertenencia de empleados a ese departamento, **Then** el sistema conserva la relación de uno a muchos entre departamento y empleados.
2. **Given** que un empleado requiere pertenecer a un departamento, **When** se intenta asociarlo a un departamento inexistente, **Then** el sistema rechaza la operación por referencia inválida.
3. **Given** que un departamento tiene empleados asociados, **When** se intenta eliminarlo, **Then** el sistema bloquea la eliminación e informa que existen dependencias activas.

---

### Edge Cases

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right edge cases.
-->

- Intento de crear o actualizar un departamento con `nombre` vacío o compuesto solo por espacios.
- Intento de registrar un departamento con `clave` ya existente.
- Consulta, actualización o eliminación de una `clave` de departamento inexistente.
- Eliminación de un departamento que aún tiene uno o más empleados asociados.
- Asociación de un empleado a un departamento no registrado.

## Requirements *(mandatory)*

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right functional requirements.
-->

### Functional Requirements

- **FR-001**: El sistema DEBE permitir crear departamentos con los campos obligatorios `clave` y `nombre`.
- **FR-002**: El sistema DEBE recibir la `clave` de departamento como dato provisto por el usuario al momento de crear el registro.
- **FR-003**: El sistema DEBE limitar `nombre` de departamento a un máximo de 50 caracteres.
- **FR-004**: El sistema DEBE rechazar la creación o actualización de departamentos cuando `nombre` exceda 50 caracteres o no contenga un valor válido.
- **FR-005**: El sistema DEBE permitir consultar un departamento por `clave`.
- **FR-006**: El sistema DEBE permitir listar los departamentos registrados.
- **FR-007**: El sistema DEBE permitir actualizar el `nombre` de un departamento existente.
- **FR-008**: El sistema DEBE permitir eliminar un departamento solo cuando no tenga empleados asociados.
- **FR-009**: El sistema DEBE mantener la relación de negocio en la que un departamento puede tener varios empleados y cada empleado pertenece a un único departamento.
- **FR-010**: El sistema DEBE impedir que un empleado quede asociado a un departamento inexistente.
- **FR-011**: El sistema DEBE conservar la relación entre departamentos y empleados para que pueda consultarse y validarse de forma consistente.
- **FR-012**: El sistema DEBE exponer únicamente operaciones CRUD básicas para departamentos: crear, listar, obtener por clave, actualizar y eliminar.
- **FR-013**: El sistema DEBE responder con errores uniformes cuando un departamento no exista o cuando una operación viole restricciones de relación o validación.
- **FR-014**: El sistema DEBE restringir las operaciones de departamentos a usuarios administradores.
- **FR-015**: El sistema DEBE mantener sin cambios automáticos a los empleados existentes durante la introducción de departamentos, dejando su asignación inicial como una actividad manual posterior.

### Key Entities *(include if feature involves data)*

- **Departamento**: Representa una unidad organizacional identificada por `clave` y descrita por `nombre`, a la que pueden pertenecer múltiples empleados.
- **Empleado**: Representa un colaborador existente del sistema que debe estar vinculado a exactamente un departamento.

## Assumptions

- La `clave` de departamento será provista por el usuario y validada como valor único de negocio.
- El alcance funcional se centra en el CRUD de departamentos y en la integridad de su relación con empleados, no en ampliar los flujos de negocio ajenos a esa relación.
- La eliminación de departamentos con empleados asociados debe bloquearse para preservar integridad y trazabilidad.
- La introducción del módulo de departamentos no hará migraciones automáticas para asignar departamento a empleados existentes.

## Clarifications

### Session 2026-03-17

- Q: ¿Quién define la `clave` del departamento? → A: La provee el usuario al crear.
- Q: ¿Cómo manejar empleados existentes sin departamento? → A: Sin cambios automáticos; asignación manual posterior.
- Q: ¿Qué política de borrado aplica con empleados asociados? → A: Restrict; no se elimina el departamento.
- Q: ¿Cuál es el alcance de endpoints para departamentos? → A: CRUD básico únicamente.
- Q: ¿Quién puede operar departamentos? → A: Solo administradores.

## Success Criteria *(mandatory)*

<!--
  ACTION REQUIRED: Define measurable success criteria.
  These must be technology-agnostic and measurable.
-->

### Measurable Outcomes

- **SC-001**: El 100% de los intentos de creación con `clave` única y `nombre` válido registran un departamento correctamente en menos de 3 segundos.
- **SC-002**: El 100% de los intentos con `nombre` mayor a 50 caracteres son rechazados con un mensaje de validación entendible.
- **SC-003**: Al menos el 95% de las consultas de departamentos existentes devuelven el resultado correcto en menos de 2 segundos.
- **SC-004**: El 100% de los empleados asociados a departamentos conservan una referencia válida a un departamento existente.
- **SC-005**: El 100% de los intentos de eliminar departamentos con empleados asociados son bloqueados con una respuesta clara sobre la dependencia existente.
