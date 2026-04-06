# Feature Specification: CRUD de Departamentos Frontend

**Feature Branch**: `007-crud-departamentos-frontend`  
**Created**: 2026-03-25  
**Status**: Draft  
**Input**: User description: "/speckit.specify Para Frontend. Generar CRUD completo para departamentos basado en la constitución del proyecto"

## Clarifications

### Session 2026-03-25

- Q: En modo edición, ¿qué campos pueden modificarse? → A: Solo `nombre`; `clave` es de solo lectura.
- Q: ¿Cómo se maneja la eliminación cuando un departamento tiene empleados asociados? → A: Se bloquea la eliminación y se muestra mensaje claro.
- Q: ¿Cómo se maneja conflicto de concurrencia en actualización (`PUT` con 409)? → A: Recargar datos y pedir confirmación para reintentar.
- Q: ¿Qué estrategia de paginación se usará en el listado? → A: Paginación en cliente.

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

### User Story 1 - Consultar departamentos (Priority: P1)

Como usuario autenticado, quiero consultar el listado y el detalle de departamentos para conocer la estructura organizacional vigente.

**Why this priority**: La consulta habilita valor inmediato para todos los roles y es la base para crear, editar o eliminar con contexto.

**Independent Test**: Puede validarse iniciando sesión como USER, navegando a la vista de departamentos, cargando la tabla y consultando un registro por clave sin exponer acciones de escritura.

**Acceptance Scenarios**:

1. **Given** que existe una sesión válida, **When** el usuario abre la vista de departamentos, **Then** el sistema lista departamentos consumiendo `GET /api/v1/departamentos`.
2. **Given** que el usuario selecciona un departamento, **When** solicita su detalle, **Then** el sistema obtiene y muestra la información con `GET /api/v1/departamentos/{clave}`.
3. **Given** que falla la API de consulta, **When** el usuario intenta cargar información, **Then** el sistema muestra un mensaje visible y conserva la sesión activa.
4. **Given** que hay múltiples departamentos en la lista, **When** el usuario navega entre páginas, **Then** el sistema pagina resultados en cliente sin nueva llamada de listado por cambio de página.

---

### User Story 2 - Crear y editar departamentos (Priority: P2)

Como usuario ADMIN, quiero crear y editar departamentos desde un formulario para mantener actualizada la información organizacional.

**Why this priority**: La escritura es clave para que el catálogo no quede obsoleto y soporta operación diaria del negocio.

**Independent Test**: Puede validarse iniciando sesión como ADMIN, creando un departamento con `POST /api/v1/departamentos` y editándolo con `PUT /api/v1/departamentos/{clave}` desde el mismo flujo de formulario.

**Acceptance Scenarios**:

1. **Given** que el usuario tiene rol ADMIN, **When** completa el formulario en modo creación, **Then** el sistema registra el departamento y actualiza la tabla.
2. **Given** que el usuario tiene rol ADMIN y selecciona editar, **When** confirma cambios, **Then** el sistema actualiza el registro y refleja el resultado en la vista.
3. **Given** que el formulario tiene datos inválidos o falla la API, **When** el usuario intenta guardar, **Then** el sistema notifica el error y mantiene datos para corrección.
4. **Given** que el usuario está en modo edición, **When** visualiza el campo clave, **Then** el sistema lo muestra como solo lectura y solo permite modificar nombre.
5. **Given** que la actualización devuelve conflicto de concurrencia (409), **When** el usuario intenta guardar cambios, **Then** el sistema recarga datos actuales del departamento, informa el conflicto y solicita confirmación antes de reintentar.

---

### User Story 3 - Eliminar departamentos con control de rol (Priority: P3)

Como usuario ADMIN, quiero eliminar departamentos desde la tabla para depurar registros obsoletos, mientras USER conserva acceso solo de lectura.

**Why this priority**: Completa el CRUD solicitado y consolida reglas de autorización por rol en interfaz.

**Independent Test**: Puede validarse iniciando sesión como ADMIN para eliminar un registro con `DELETE /api/v1/departamentos/{clave}` y como USER para comprobar que la acción no está disponible.

**Acceptance Scenarios**:

1. **Given** que el usuario tiene rol ADMIN, **When** ejecuta eliminar sobre una fila, **Then** el sistema elimina el departamento y refresca la tabla.
2. **Given** que el usuario tiene rol USER, **When** accede a la vista de departamentos, **Then** no visualiza ni puede ejecutar acciones de crear, editar o eliminar.
3. **Given** que la API rechaza la eliminación, **When** el usuario ADMIN intenta eliminar, **Then** el sistema informa el error con notificación visible.
4. **Given** que un departamento tiene empleados asociados, **When** un ADMIN intenta eliminarlo, **Then** el sistema bloquea la eliminación y muestra un mensaje claro indicando la causa.

---

### Edge Cases

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right edge cases.
-->

- La tabla de departamentos se carga vacía y no existen filas disponibles para edición/eliminación.
- La API retorna error en listar, obtener, crear, actualizar o eliminar.
- El usuario pierde sesión durante una operación de escritura.
- El usuario cambia entre modo crear y editar con formulario parcialmente completado.
- Un usuario USER intenta escribir mediante manipulación de cliente o navegación directa.
- El usuario ADMIN intenta eliminar un departamento con empleados asociados y la operación debe bloquearse con feedback explícito.
- La actualización de un departamento falla con 409 por modificación concurrente y requiere recarga con reintento confirmado por usuario.
- La paginación en cliente debe mantener coherencia visual y de navegación tras crear, editar o eliminar registros.

## Requirements *(mandatory)*

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right functional requirements.
-->

### Functional Requirements

- **FR-001**: El sistema DEBE consumir `GET /api/v1/departamentos` para listar departamentos en tabla.
- **FR-002**: El sistema DEBE consumir `GET /api/v1/departamentos/{clave}` para obtener el detalle de un departamento.
- **FR-003**: El sistema DEBE consumir `POST /api/v1/departamentos` para crear departamentos desde formulario.
- **FR-004**: El sistema DEBE consumir `PUT /api/v1/departamentos/{clave}` para actualizar departamentos existentes.
- **FR-005**: El sistema DEBE consumir `DELETE /api/v1/departamentos/{clave}` para eliminar departamentos desde acciones por fila, bloqueando la eliminación cuando existan empleados asociados y mostrando un mensaje claro de bloqueo.
- **FR-006**: El frontend DEBE usar una base URL única configurada en `http://localhost:8080`.
- **FR-007**: El sistema DEBE centralizar acceso a API de departamentos en un servicio con métodos `getDepartamentos()`, `getDepartamento(clave)`, `createDepartamento()`, `updateDepartamento(clave)` y `deleteDepartamento(clave)`.
- **FR-008**: El sistema DEBE ofrecer un componente de departamentos con tabla de lista, formulario de crear/editar y botones de editar/eliminar.
- **FR-009**: El componente DEBE manejar estados explícitos de formulario para crear y editar; en edición, `clave` DEBE permanecer inmutable (solo lectura) y solo `nombre` DEBE ser editable.
- **FR-010**: El sistema DEBE mostrar mensajes de éxito y error mediante snackbar para operaciones de lectura y escritura, incluyendo conflictos de concurrencia (409) en actualización.
- **FR-011**: El sistema DEBE mantener comportamiento funcional equivalente en desktop y móvil para consulta y acciones habilitadas por rol.
- **FR-012**: El sistema DEBE actualizar la tabla de departamentos tras operaciones exitosas sin recarga manual completa.
- **FR-013**: Ante respuesta 409 en `PUT /api/v1/departamentos/{clave}`, el sistema DEBE recargar el estado actual del departamento y solicitar confirmación explícita del usuario para reintentar la actualización.
- **FR-014**: El listado DEBE implementar paginación en cliente sobre los resultados obtenidos por `GET /api/v1/departamentos`.

### Security & Authorization Requirements *(mandatory for authenticated flows)*

- **SR-001**: El acceso a la vista de departamentos DEBE requerir sesión autenticada activa.
- **SR-002**: La matriz de roles DEBE definirse como `ADMIN` (listar, crear, editar, eliminar) y `USER` (listar y obtener detalle).
- **SR-003**: Las rutas privadas DEBEN redirigir a login cuando no exista sesión válida.
- **SR-004**: La interfaz DEBE ocultar o deshabilitar acciones de crear, editar y eliminar para `USER`.
- **SR-005**: El backend DEBE mantenerse como autoridad final de seguridad aunque la UI adapte visibilidad.
- **SR-006**: Se DEBEN cubrir escenarios negativos de solicitud sin token, token inválido y rol insuficiente.

### Key Entities *(include if feature involves data)*

- **Departamento**: Registro organizacional con clave única y nombre, gestionado desde tabla y formulario en UI; la `clave` es identificador inmutable después de la creación y puede tener empleados asociados que bloquean su eliminación.
- **Sesión de Usuario**: Estado autenticado con rol (`ADMIN` o `USER`) que define permisos de acciones visibles y ejecutables.

## Assumptions

- El backend expone y mantiene operativos los endpoints de departamentos indicados en alcance.
- El flujo de autenticación y rol activo ya está integrado en el frontend del proyecto.
- Las respuestas de API son suficientes para poblar tabla y formulario sin transformación extraordinaria.

## Dependencies

- Disponibilidad de backend en `http://localhost:8080`.
- Contrato de endpoints de departamentos consistente con constitución y seguridad del proyecto.
- Disponibilidad de rutas protegidas y mecanismo de inyección de token en frontend.

## Success Criteria *(mandatory)*

<!--
  ACTION REQUIRED: Define measurable success criteria.
  These must be technology-agnostic and measurable.
-->

### Measurable Outcomes

- **SC-001**: El 95% de cargas de listado de departamentos completan en menos de 2 segundos en condiciones normales de red local.
- **SC-002**: El 100% de operaciones exitosas de creación y actualización se reflejan en la tabla sin recarga manual completa.
- **SC-003**: El 100% de eliminaciones exitosas remueven el registro visible de la tabla activa.
- **SC-004**: El 100% de usuarios con rol `USER` no visualizan controles de crear, editar ni eliminar.
- **SC-005**: Al menos el 90% de operaciones fallidas muestran un mensaje de error comprensible para usuario final.
