# Feature Specification: Autenticacion de Empleados por Correo

**Feature Branch**: `002-empleados-email-auth`  
**Created**: 2026-03-11  
**Status**: Draft  
**Input**: User description: "genera la autenticacion con correo contraseña de empleados"

## Clarifications

### Session 2026-03-11

- Q: Que politica de contrasena debe exigir el sistema? → A: Minimo 8 caracteres, al menos 1 mayuscula, 1 minuscula y 1 numero.

### Session 2026-03-12

- Q: Como se desbloquea una cuenta tras multiples intentos fallidos? → A: Desbloqueo automatico a los 15 minutos y opcion de desbloqueo manual por administrador.
- Q: Cual es el umbral de intentos fallidos antes del bloqueo? → A: 10 intentos fallidos consecutivos.

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

### User Story 1 - Iniciar sesion con correo y contrasena (Priority: P1)

Como empleado, quiero iniciar sesion con mi correo y contrasena para acceder de forma segura a las funciones del sistema.

**Why this priority**: Sin autenticacion no existe control de acceso y no se puede distinguir entre usuarios autorizados y no autorizados.

**Independent Test**: Puede probarse creando una cuenta de empleado habilitada, iniciando sesion con credenciales validas y comprobando que el acceso autenticado queda activo.

**Acceptance Scenarios**:

1. **Given** que existe un empleado activo con correo y contrasena vigentes, **When** envia credenciales correctas, **Then** el sistema concede acceso autenticado.
2. **Given** que el correo no existe o la contrasena es incorrecta, **When** el empleado intenta iniciar sesion, **Then** el sistema rechaza el acceso con mensaje generico de credenciales invalidas.
3. **Given** que el empleado no completa correo o contrasena, **When** intenta iniciar sesion, **Then** el sistema rechaza la solicitud e indica campos obligatorios.

---

### User Story 2 - Proteger acciones solo para autenticados (Priority: P2)

Como empleado autenticado, quiero que solo usuarios con sesion valida accedan a las operaciones restringidas para mantener la seguridad de la informacion.

**Why this priority**: Evita accesos no autorizados a funciones de negocio una vez habilitado el inicio de sesion.

**Independent Test**: Puede probarse intentando entrar a una operacion restringida con sesion valida y sin sesion para verificar que solo el primer caso es permitido.

**Acceptance Scenarios**:

1. **Given** que un empleado tiene una sesion valida, **When** accede a una funcion restringida, **Then** el sistema permite la operacion.
2. **Given** que una solicitud no incluye autenticacion valida, **When** intenta acceder a una funcion restringida, **Then** el sistema la rechaza por falta de autorizacion.

---

### User Story 3 - Cerrar sesion de forma segura (Priority: P3)

Como empleado autenticado, quiero cerrar sesion para finalizar mi acceso y reducir riesgos cuando termino de usar el sistema.

**Why this priority**: Completa el ciclo basico de autenticacion y reduce exposicion por sesiones abiertas.

**Independent Test**: Puede probarse iniciando sesion, cerrandola y verificando que una accion restringida posterior ya no sea permitida.

**Acceptance Scenarios**:

1. **Given** que el empleado mantiene una sesion valida, **When** ejecuta cierre de sesion, **Then** el sistema invalida su acceso actual.
2. **Given** que la sesion ya fue cerrada o expiro, **When** el empleado intenta reutilizar el acceso previo, **Then** el sistema lo rechaza y solicita autenticacion nueva.

---

### Edge Cases

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right edge cases.
-->

- Multiples intentos fallidos consecutivos que alcanzan exactamente el umbral de bloqueo.
- Inicio de sesion con diferencias de mayusculas/minusculas en el correo.
- Empleado deshabilitado intentando autenticarse.
- Cierre de sesion repetido sobre una sesion ya invalidada.
- Sesion expirada durante una operacion iniciada por el usuario.
- Desbloqueo manual de una cuenta que ya fue desbloqueada automaticamente.

## Requirements *(mandatory)*

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right functional requirements.
-->

### Functional Requirements

- **FR-001**: El sistema DEBE permitir a empleados autenticarse con correo electronico y contrasena.
- **FR-002**: El sistema DEBE validar que el correo tenga formato valido antes de procesar la autenticacion.
- **FR-003**: El sistema DEBE tratar el correo como identificador unico de acceso por empleado.
- **FR-004**: El sistema DEBE rechazar credenciales invalidas con una respuesta generica que no revele si fallo el correo o la contrasena.
- **FR-005**: El sistema DEBE exigir autenticacion valida para toda funcion marcada como restringida.
- **FR-006**: El sistema DEBE permitir cierre de sesion y anular el acceso de la sesion cerrada.
- **FR-007**: El sistema DEBE expirar automaticamente sesiones inactivas despues de 30 minutos.
- **FR-008**: El sistema DEBE registrar eventos de seguridad de inicio de sesion exitoso, fallido y cierre de sesion, incluyendo marca de tiempo y empleado relacionado cuando exista.
- **FR-009**: El sistema DEBE bloquear temporalmente la autenticacion de una cuenta por 15 minutos despues de 10 intentos fallidos consecutivos.
- **FR-010**: El sistema DEBE impedir autenticacion de empleados deshabilitados.
- **FR-011**: El sistema DEBE permitir que un empleado autenticado mantenga una sola sesion activa al mismo tiempo; al iniciar una nueva sesion, la anterior queda invalidada.
- **FR-012**: El sistema DEBE devolver errores de autenticacion con estructura uniforme que incluya `type`, `title`, `status`, `detail` e `instance`.
- **FR-013**: El sistema DEBE exigir contrasenas de minimo 8 caracteres con al menos 1 letra mayuscula, 1 letra minuscula y 1 numero.
- **FR-014**: El sistema DEBE permitir que un administrador desbloquee manualmente una cuenta bloqueada antes de que termine el periodo automatico de 15 minutos.

### Key Entities *(include if feature involves data)*

- **Empleado**: Persona del sistema con identidad de negocio, correo unico para acceso, estado de habilitacion y datos basicos.
- **Credencial de Empleado**: Conjunto de datos de autenticacion asociados al empleado, incluyendo contrasena resguardada que cumple la politica minima de complejidad, estado de bloqueo temporal, contador de intentos fallidos y metadatos de desbloqueo.
- **Sesion de Acceso**: Estado temporal que representa un acceso autenticado vigente, con hora de inicio, hora de expiracion por inactividad y estado de validez.
- **Evento de Seguridad**: Registro auditable de actividades de autenticacion (exito, fallo, cierre) con marca temporal y contexto.

## Assumptions

- Solo empleados ya registrados en el sistema pueden autenticarse; esta feature no incluye auto-registro.
- La recuperacion o cambio de contrasena queda fuera de alcance para esta iteracion.
- Las funciones restringidas existentes en el sistema consumiran el estado de autenticacion definido por esta feature.
- La politica de bloqueo temporal y expiracion propuesta cubre el riesgo inicial esperado para el entorno academico de la practica.

## Dependencies

- Existencia de registros de empleados con correo unico y estado de habilitacion.
- Disponibilidad de canales de operacion restringida que utilicen control de acceso basado en sesion autenticada.
- Consistencia con el manejo de errores ya definido por el backend para respuestas de negocio.

## Success Criteria *(mandatory)*

<!--
  ACTION REQUIRED: Define measurable success criteria.
  These must be technology-agnostic and measurable.
-->

### Measurable Outcomes

- **SC-001**: Al menos el 95% de los intentos de inicio de sesion con credenciales validas completan acceso en menos de 3 segundos.
- **SC-002**: El 100% de los intentos con credenciales invalidas son rechazados sin exponer si el error corresponde al correo o a la contrasena.
- **SC-003**: El 100% de los accesos a funciones restringidas sin autenticacion valida son bloqueados.
- **SC-004**: El 100% de los cierres de sesion invalidan el acceso previo en menos de 2 segundos.
- **SC-005**: Al menos el 99% de eventos de autenticacion (exitos, fallos y cierres) quedan registrados con marca de tiempo y contexto minimo requerido.
