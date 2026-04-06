# Feature Specification: CRUD de Empleados Frontend

**Feature Branch**: `006-crud-empleados-frontend`  
**Created**: 2026-03-24  
**Status**: Draft  
**Input**: User description: "/speckit.specify Generar funcionalidad CRUD para empleados basada en la constitución del proyecto"

## Clarifications

### Session 2026-03-24

- Q: Que payload debe usar createEmpleado? -> A: Enviar nombre, direccion, telefono, correo y departamentoClave.
- Q: De donde debe cargarse el selector de departamentos? -> A: Desde API con GET /api/v1/departamentos.
- Q: Que estrategia de paginacion debe usar el listado? -> A: Paginacion server-side con parametros page y size.
- Q: Que formato de respuesta debe usar el listado paginado? -> A: Objeto con content, totalElements, totalPages, number y size.

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

### User Story 1 - Ver listado de empleados (Priority: P1)

Como usuario autenticado, quiero ver el listado de empleados para consultar la informacion operativa actual.

**Why this priority**: La consulta es la base del flujo y habilita valor inmediato para todos los roles, incluyendo usuarios de solo lectura.

**Independent Test**: Puede validarse iniciando sesion como USER, abriendo la vista de empleados y comprobando que la tabla carga los registros sin exponer acciones de escritura.

**Acceptance Scenarios**:

1. **Given** que existe una sesion valida, **When** el usuario navega a la vista de empleados, **Then** el sistema muestra una tabla con los empleados obtenidos desde el servicio.
2. **Given** que el servicio de empleados falla, **When** el usuario intenta cargar el listado, **Then** el sistema informa el error de forma visible y no bloquea la sesion.

---

### User Story 2 - Crear empleados con departamento (Priority: P2)

Como usuario ADMIN, quiero crear empleados y asociarlos a un departamento para mantener actualizada la estructura organizacional.

**Why this priority**: La creacion agrega nuevos registros de negocio y depende del listado de departamentos para conservar integridad de datos.

**Independent Test**: Puede validarse iniciando sesion como ADMIN, completando el formulario de alta con un departamento existente y verificando que el nuevo empleado aparece en la tabla.

**Acceptance Scenarios**:

1. **Given** que el usuario tiene rol ADMIN y existen departamentos disponibles, **When** completa y envia el formulario de alta, **Then** el sistema crea el empleado y actualiza la lista.
2. **Given** que el formulario tiene datos invalidos o falla la creacion, **When** el usuario intenta guardar, **Then** el sistema muestra un mensaje de error y mantiene los datos para correccion.

---

### User Story 3 - Eliminar empleados segun permisos (Priority: P3)

Como usuario ADMIN, quiero eliminar empleados desde la lista para retirar registros que ya no son vigentes, mientras los usuarios USER conservan acceso solo de consulta.

**Why this priority**: Completa el alcance CRUD solicitado y consolida el control de autorizacion por rol.

**Independent Test**: Puede validarse iniciando sesion como ADMIN para eliminar un empleado y como USER para comprobar que la accion no esta disponible.

**Acceptance Scenarios**:

1. **Given** que el usuario tiene rol ADMIN, **When** ejecuta la accion de eliminar en una fila, **Then** el sistema elimina el registro y refresca la tabla.
2. **Given** que el usuario tiene rol USER, **When** accede a la vista de empleados, **Then** el sistema oculta o deshabilita acciones de crear y eliminar.

---

### Edge Cases

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right edge cases.
-->

- La lista de empleados se carga vacia y no existen filas para acciones.
- La API devuelve error en listado, creacion o eliminacion.
- El usuario pierde sesion mientras interactua con la vista.
- No hay departamentos disponibles al abrir el formulario de creacion.
- El usuario USER intenta ejecutar acciones de escritura mediante navegacion directa o manipulacion de cliente.

## Requirements *(mandatory)*

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right functional requirements.
-->

### Functional Requirements

- **FR-001**: El sistema DEBE obtener el listado de empleados desde `GET /api/v1/empleados` y mostrarlo en formato tabular.
- **FR-002**: El sistema DEBE permitir crear empleados mediante `POST /api/v1/empleados` desde un formulario de captura.
- **FR-003**: El sistema DEBE permitir eliminar empleados mediante `DELETE /api/v1/empleados/{clave}` desde acciones por fila.
- **FR-004**: El formulario de alta DEBE permitir seleccionar un departamento para asociarlo al empleado.
- **FR-005**: El sistema DEBE operar contra una unica base de URL de API configurada como `http://localhost:8080`.
- **FR-006**: El sistema DEBE mostrar mensajes de exito o error de operaciones al usuario mediante notificaciones visibles y consistentes.
- **FR-007**: El sistema DEBE restringir creacion y eliminacion exclusivamente a usuarios con rol ADMIN.
- **FR-008**: El sistema DEBE permitir a usuarios con rol USER acceso unicamente de lectura al listado.
- **FR-009**: El sistema DEBE resolver el estado de habilitación de acciones mediante un servicio central de autorización de UI.
- **FR-010**: El sistema DEBE centralizar el acceso a datos de empleados en un servicio con operaciones de listar, crear y eliminar.
- **FR-011**: El sistema DEBE mantener la vista de empleados operativa en desktop y movil sin perdida de funcionalidad principal.
- **FR-012**: El alta de empleado DEBE enviar exactamente los campos `nombre`, `direccion`, `telefono`, `correo` y `departamentoClave`.
- **FR-013**: El selector de departamentos DEBE cargarse consumiendo `GET /api/v1/departamentos`.
- **FR-014**: El listado de empleados DEBE usar paginacion server-side consumiendo parametros `page` y `size`.
- **FR-015**: La respuesta de listado DEBE mapearse desde un objeto paginado con `content`, `totalElements`, `totalPages`, `number` y `size`.
- **FR-016**: La creación de empleado DEBE esperar respuesta 201 con el objeto creado para actualizar la vista sin solicitud adicional.

### Security & Authorization Requirements *(mandatory for authenticated flows)*

- **SR-001**: El acceso a la vista de empleados DEBE requerir sesion autenticada activa.
- **SR-002**: La matriz de roles DEBE quedar definida como: `ADMIN` (listar, crear, eliminar) y `USER` (listar solamente).
- **SR-003**: Las rutas privadas DEBEN redirigir a login si no existe sesion valida.
- **SR-004**: La interfaz DEBE ocultar o deshabilitar acciones no permitidas para el rol activo.
- **SR-005**: El backend DEBE mantenerse como autoridad final de seguridad aunque la UI oculte acciones.
- **SR-006**: Se DEBEN cubrir escenarios negativos de solicitud sin token, token invalido y rol insuficiente.

### Key Entities *(include if feature involves data)*

- **Empleado**: Registro de persona en la organizacion con clave unica y atributos operativos de gestion.
- **Departamento**: Unidad organizacional seleccionable al crear empleados.
- **Sesion de Usuario**: Estado autenticado con rol asociado (`ADMIN` o `USER`) que define permisos efectivos en UI.

## Assumptions

- El backend expone y mantiene operativos los endpoints definidos en el alcance.
- La sesion autenticada y el rol ya existen en el flujo de acceso del proyecto.
- El catalogo de departamentos se obtiene desde servicios ya disponibles en la aplicacion.

## Dependencies

- Disponibilidad del backend en `http://localhost:8080`.
- Contratos de API de empleados y departamentos consistentes con la constitucion.
- Rutas de autenticacion y control de acceso ya integradas en el frontend.
- Disponibilidad operativa de `GET /api/v1/departamentos` para poblar el formulario de alta.
- Disponibilidad de parametros de paginacion (`page`, `size`) en el endpoint de listado de empleados.
- Disponibilidad del formato paginado en `GET /api/v1/empleados` con `content`, `totalElements`, `totalPages`, `number` y `size`.

## Success Criteria *(mandatory)*

<!--
  ACTION REQUIRED: Define measurable success criteria.
  These must be technology-agnostic and measurable.
-->

### Measurable Outcomes

- **SC-001**: El 95% de cargas de listado de empleados completan en menos de 2 segundos en condiciones normales de red local.
- **SC-002**: El 100% de intentos de creacion exitosos reflejan el nuevo empleado en la tabla sin recarga manual.
- **SC-003**: El 100% de intentos de eliminacion exitosos remueven el registro de la vista activa.
- **SC-004**: El 100% de usuarios USER no visualizan acciones de crear ni eliminar en la interfaz.
- **SC-005**: Al menos el 90% de operaciones fallidas muestran un mensaje de error comprensible para el usuario final.
- **SC-006**: El 100% de cambios de pagina y tamano en el listado se reflejan con respuesta consistente del backend sin recargar manualmente la vista.
