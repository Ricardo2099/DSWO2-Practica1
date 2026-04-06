# Feature Specification: Suite E2E de Sistema Completo

**Feature Branch**: `008-cypress-e2e-suite`  
**Created**: 2026-03-26  
**Status**: Draft  
**Input**: User description: "Genera la especificación e implementación de pruebas E2E con Cypress para validar el sistema completo desde el frontend Angular consumiendo el backend real."

## Clarifications

### Session 2026-03-26

- Q: ¿Qué estrategia de autenticación debe usar la suite E2E para minimizar inestabilidad sin perder validación real? → A: Autenticación por API en comandos custom y una prueba dedicada de login por UI.
- Q: ¿Qué estrategia de datos de prueba debe usar la suite para asegurar independencia y evitar colisiones? → A: Generar datos únicos por test y limpiar lo creado dentro del mismo test.
- Q: ¿Cómo validar autorización para USER sin sacrificar seguridad real? → A: Bloquear en UI y además validar error 403 visible al forzar acción.
- Q: ¿Cómo gestionar credenciales de pruebas sin exponer secretos en repositorio? → A: Usar variables de entorno con fallback local documentado.
- Q: ¿Qué estrategia de evidencia visual ofrece mejor balance entre trazabilidad y costo de ejecución? → A: Screenshot automático en fallos, screenshots manuales en hitos clave y video siempre.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Acceso seguro y sesión confiable (Priority: P1)

Como usuario del sistema, quiero iniciar sesión y conservar una sesión válida para acceder a la aplicación sin fricción y con control de seguridad.

**Why this priority**: Sin autenticación confiable no se puede validar ni operar el resto de los flujos del sistema.

**Independent Test**: Puede validarse de forma independiente verificando inicio de sesión exitoso/fallido, persistencia de sesión y redirección al login cuando no existe credencial válida.

**Acceptance Scenarios**:

1. **Given** que un usuario con credenciales válidas accede a login, **When** envía sus datos, **Then** entra al sistema con una sesión activa y visible en la UI.
2. **Given** que un usuario ingresa credenciales incorrectas, **When** intenta autenticarse, **Then** permanece en login y ve un mensaje de error claro.
3. **Given** que existe una sesión activa, **When** el usuario recarga o vuelve a abrir la aplicación, **Then** mantiene acceso sin repetir login.
4. **Given** que no existe token o el token es inválido, **When** el usuario intenta abrir una vista protegida, **Then** es redirigido a login y la sesión local se limpia.

---

### User Story 2 - Gestión administrativa de departamentos y empleados (Priority: P2)

Como usuario con rol administrativo, quiero gestionar departamentos y empleados desde la interfaz para mantener información organizacional consistente.

**Why this priority**: El valor principal de operación está en validar que el flujo completo de alta, consulta y baja funciona extremo a extremo con datos reales.

**Independent Test**: Puede validarse de forma independiente ejecutando altas y bajas como ADMIN y comprobando que las tablas reflejan los cambios sin depender de otros escenarios.

**Acceptance Scenarios**:

1. **Given** que un ADMIN está autenticado, **When** crea un departamento, **Then** el nuevo registro aparece en la tabla de departamentos.
2. **Given** que un ADMIN está autenticado y existe al menos un departamento, **When** crea un empleado asignando departamento, **Then** el empleado aparece en la tabla de empleados con su relación.
3. **Given** que un ADMIN visualiza empleados, **When** elimina un empleado existente, **Then** el registro desaparece de la tabla.

---

### User Story 3 - Respeto estricto de permisos por rol (Priority: P3)

Como usuario con rol de solo lectura, quiero ver información sin poder modificarla para respetar los límites de autorización definidos por negocio.

**Why this priority**: Reduce riesgo de operaciones indebidas y valida que la autorización funciona tanto en interfaz como en respuestas del sistema.

**Independent Test**: Puede validarse iniciando sesión como USER y comprobando visualización de listas junto con bloqueo visible de acciones de creación/edición/eliminación.

**Acceptance Scenarios**:

1. **Given** que un USER está autenticado, **When** navega por empleados y departamentos, **Then** puede consultar listas pero no ve acciones de escritura habilitadas.
2. **Given** que un USER intenta forzar una acción sin permisos, **When** el sistema procesa la solicitud, **Then** se informa un error de autorización visible.
3. **Given** que un ADMIN está autenticado, **When** navega por las mismas vistas, **Then** dispone de acciones completas de gestión.

---

### User Story 4 - Evidencia visual de funcionamiento (Priority: P3)

Como responsable de calidad, quiero evidencia visual automática de la ejecución para demostrar el comportamiento real del sistema en escenarios críticos.

**Why this priority**: La evidencia respalda auditoría funcional y facilita diagnóstico de regresiones.

**Independent Test**: Puede validarse ejecutando la suite y comprobando que cada corrida genera material visual, incluyendo capturas en pasos clave de negocio.

**Acceptance Scenarios**:

1. **Given** que se ejecuta una corrida E2E completa, **When** finaliza, **Then** se genera evidencia visual de todos los escenarios ejecutados.
2. **Given** que se completan pasos críticos (login, alta/baja), **When** el flujo llega a cada hito, **Then** existe captura explícita del estado de pantalla.

### Edge Cases

- El sistema devuelve error al autenticar y no debe quedar sesión parcial.
- Un usuario intenta abrir rutas protegidas sin token válido.
- Un token expira o se vuelve inválido durante la navegación.
- Un usuario con rol USER intenta ejecutar acciones de escritura por navegación directa o manipulación del cliente.
- La creación de empleado falla por datos incompletos o relación inválida de departamento y debe mostrarse mensaje claro.
- La lista de empleados o departamentos está vacía y la UI debe mantener estado legible.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El proyecto DEBE contar con una suite E2E ejecutable desde el frontend que valide flujos reales de la aplicación contra backend activo.
- **FR-002**: La suite DEBE incluir pruebas separadas para autenticación, empleados y departamentos.
- **FR-003**: La suite DEBE permitir autenticación reutilizable para rol administrativo y rol de solo lectura usando autenticación por API real y conservar una prueba dedicada de login por UI.
- **FR-004**: El flujo de autenticación DEBE cubrir inicio de sesión exitoso, credenciales inválidas, persistencia de sesión y redirección por ausencia de sesión.
- **FR-005**: El flujo de departamentos para ADMIN DEBE cubrir creación y validación de aparición en listado.
- **FR-006**: El flujo de empleados para ADMIN DEBE cubrir creación con departamento asociado, validación en listado, eliminación y validación de desaparición.
- **FR-007**: El flujo de autorización DEBE validar que USER solo consulta y no puede ejecutar acciones de escritura.
- **FR-008**: La suite DEBE validar escenarios negativos de seguridad: sin token, token inválido y operación sin permisos.
- **FR-009**: La ejecución DEBE generar evidencia visual con captura automática en fallos, capturas manuales en login exitoso, alta de departamento, alta de empleado y eliminación de empleado, y video de ejecución en cada corrida.
- **FR-010**: Los escenarios DEBEN ser independientes entre sí, evitando dependencias secuenciales obligatorias.
- **FR-011**: Los datos de prueba DEBEN gestionarse de forma repetible generando identificadores únicos por test y limpiando los registros creados dentro del mismo test para evitar resultados no deterministas entre ejecuciones.
- **FR-012**: La interacción con la interfaz DEBE apoyarse en selectores estables orientados a pruebas para reducir fragilidad ante cambios visuales.
- **FR-013**: La entrega DEBE incluir instrucciones de ejecución extremo a extremo para levantar backend, frontend y ejecución de pruebas en entorno local.

### Security & Authorization Requirements *(mandatory for authenticated flows)*

- **SR-001**: El punto de entrada principal de autenticación para pruebas E2E DEBE usar API real de login para establecer sesión con token y almacenar estado autenticado para reutilización en navegación.
- **SR-002**: El ciclo de vida del token DEBE contemplar almacenamiento inicial, uso en solicitudes protegidas y limpieza al detectar invalidez.
- **SR-003**: La matriz de permisos DEBE definirse como `ADMIN` con operaciones completas de gestión y `USER` con solo lectura.
- **SR-008**: Las credenciales de pruebas E2E DEBEN inyectarse mediante variables de entorno y no almacenarse como secretos versionados; solo se permite fallback local documentado en `frontend/README.md` con formato explícito de variables requeridas y ejemplo sin secretos reales.
- **SR-004**: Cualquier acceso a vistas protegidas sin sesión válida DEBE redirigir al login.
- **SR-005**: Las acciones no permitidas para el rol activo DEBEN mostrarse bloqueadas en UI y, si se intentan forzar, DEBEN reflejar rechazo visible.
- **SR-007**: Las pruebas E2E DEBEN validar explícitamente para rol USER tanto el bloqueo visual de acciones restringidas como la respuesta 403 visible al forzar la operación.
- **SR-006**: El sistema DEBE tratar al backend como autoridad final de autorización, validando respuestas de rechazo cuando corresponda.

### Key Entities *(include if feature involves data)*

- **Usuario autenticado**: Persona que accede al sistema con rol (`ADMIN` o `USER`) y permisos derivados para operar o solo consultar.
- **Sesión**: Estado autenticado representado por una credencial válida usada para acceder a vistas protegidas.
- **Departamento**: Unidad organizacional visible en listados y utilizable para asociación de empleados.
- **Empleado**: Registro de persona vinculada obligatoriamente a un departamento dentro del dominio.
- **Evidencia de ejecución**: Resultado visual generado durante pruebas para documentar el comportamiento observado en flujos críticos.

## Assumptions

- El backend y frontend se ejecutan localmente y son accesibles durante la corrida de pruebas.
- Existen credenciales válidas para al menos un usuario ADMIN y un usuario USER en el entorno de prueba.
- Las vistas de empleados, departamentos y login están operativas y accesibles con sesión según rol.
- La base de datos del entorno de pruebas permite crear y eliminar datos usados por los escenarios.

## Dependencies

- Disponibilidad del backend del sistema en entorno local.
- Disponibilidad del frontend del sistema en entorno local.
- Existencia de datos mínimos para relaciones de negocio o capacidad de crearlos durante la prueba.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de escenarios críticos definidos (autenticación, autorización, CRUD principal y negativos de seguridad) ejecutan y reportan resultado verificable en una corrida completa.
- **SC-002**: El 100% de corridas genera evidencia visual automática y trazable para revisión funcional.
- **SC-003**: Al menos el 95% de ejecuciones repetidas de la suite en el mismo entorno finalizan sin fallos intermitentes.
- **SC-004**: El 100% de validaciones de rol confirman que USER no ejecuta acciones de escritura y ADMIN sí puede completarlas.
- **SC-005**: El tiempo total de ejecución de la suite completa se mantiene por debajo de 10 minutos en entorno local estándar.
