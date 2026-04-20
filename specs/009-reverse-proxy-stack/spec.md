# Feature Specification: Punto De Entrada HTTP Unico

**Feature Branch**: `009-reverse-proxy-stack`  
**Created**: 2026-04-09  
**Status**: Draft  
**Input**: User description: "Necesito que implementes un proxy inverso para mi proyecto y lo integres al stack de contenedores."

## Clarifications

### Session 2026-04-09

- Q: ¿Como definimos exactamente "verificar health de contenedores"? → A: Exigir healthcheck en Compose para proxy y backend, y validar ademas `GET /` y `GET /swagger-ui` via proxy.
- Q: ¿Como quieres habilitar la excepcion de debug local para base de datos? → A: DB sin exposicion por defecto; archivo override/perfil local opcional para publicar puerto.
- Q: ¿Como quieres fijar los codigos esperados de las pruebas clave? → A: Definir codigos exactos: `GET /` = 200, `POST /auth/login` = 200, `GET /api/v1/empleados` con token = 200, `GET /swagger-ui` = 200.
- Q: ¿Como quieres manejar rutas frontend como `/empleados/123` cuando se acceden directamente? → A: Fallback a `index.html` para rutas no API, manteniendo prioridad de `/api`, `/auth`, `/swagger-ui`, `/v3/api-docs`.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Acceso Unificado A La Aplicacion (Priority: P1)

Como usuario final, quiero acceder al frontend y a las rutas de API desde un unico endpoint HTTP para usar la aplicacion sin configurar puertos distintos ni origenes cruzados.

**Why this priority**: Es la base funcional del cambio; sin un punto de entrada unificado no se cumple el objetivo principal de integracion.

**Independent Test**: Se valida levantando el stack completo y verificando que `GET /` entrega la interfaz y que las rutas de API bajo el mismo host responden correctamente.

**Acceptance Scenarios**:

1. **Given** el stack en ejecucion, **When** un usuario abre `GET /` en el endpoint publico, **Then** recibe el frontend correctamente.
2. **Given** el stack en ejecucion, **When** un cliente realiza `GET /swagger-ui`, **Then** la documentacion de API se carga desde el mismo endpoint publico.
3. **Given** el stack en ejecucion, **When** un cliente consulta rutas de API bajo `/api` y `/v3/api-docs`, **Then** la solicitud llega al servicio de API sin requerir un host o puerto alternativo.

---

### User Story 2 - Autenticacion Bearer A Traves Del Proxy (Priority: P2)

Como cliente autenticado, quiero iniciar sesion y consumir endpoints protegidos usando Bearer token a traves del mismo endpoint publico para mantener la seguridad y continuidad del flujo funcional.

**Why this priority**: Garantiza que la unificacion de entrada no rompa el flujo de negocio critico de autenticacion y acceso protegido.

**Independent Test**: Se valida con `POST /auth/login` para obtener token y luego `GET /api/v1/empleados` con `Authorization: Bearer <token>` devolviendo respuesta autorizada.

**Acceptance Scenarios**:

1. **Given** credenciales validas, **When** un cliente hace `POST /auth/login` por el endpoint publico, **Then** obtiene respuesta de autenticacion valida.
2. **Given** un token valido, **When** un cliente hace `GET /api/v1/empleados` con header Bearer por el endpoint publico, **Then** recibe respuesta autorizada sin errores de enrutamiento.
3. **Given** token ausente o invalido, **When** un cliente invoca un endpoint protegido via proxy, **Then** recibe la respuesta de no autorizado definida por el backend.

---

### User Story 3 - Operacion Interna Del Stack Sin Exposicion Innecesaria (Priority: P3)

Como operador del entorno, quiero que solo el proxy sea publico y que base de datos, API y frontend permanezcan internos para reducir superficie de ataque y simplificar despliegue.

**Why this priority**: Mejora seguridad operacional y alinea el stack con una arquitectura de acceso controlado.

**Independent Test**: Se valida inspeccionando puertos publicados y confirmando que solo existe publicacion del servicio de entrada HTTP.

**Acceptance Scenarios**:

1. **Given** el stack desplegado, **When** se revisa la publicacion de puertos, **Then** solo existe el puerto HTTP del proxy expuesto al host.
2. **Given** el stack desplegado, **When** un operador revisa conectividad interna entre servicios, **Then** proxy, frontend, API y base de datos se comunican por una red interna dedicada.

### Edge Cases

- Solicitudes a rutas no definidas en reglas de enrutamiento deben devolver una respuesta de no encontrado consistente y sin exponer detalles internos.
- Si la API no esta disponible temporalmente, las solicitudes bajo rutas de API deben fallar de forma controlada sin interrumpir la entrega del frontend.
- Si el frontend no esta disponible temporalmente, `GET /` debe devolver error de upstream claro, preservando trazabilidad operativa.
- Las solicitudes autenticadas con headers adicionales no deben perder el header `Authorization` ni headers de trazabilidad al atravesar el proxy.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST exponer un unico endpoint HTTP publico para acceso de clientes web y consumidores de API.
- **FR-002**: El sistema MUST enrutar las rutas `/api`, `/auth`, `/swagger-ui` y `/v3/api-docs` hacia el servicio de API.
- **FR-003**: El sistema MUST enrutar la ruta raiz `/` y demas rutas de aplicacion web no API hacia el servicio frontend.
- **FR-004**: El sistema MUST preservar y reenviar los headers `Host`, `X-Real-IP`, `X-Forwarded-For` y `X-Forwarded-Proto` en solicitudes proxificadas.
- **FR-005**: El sistema MUST mantener compatibilidad completa con autenticacion Bearer en solicitudes proxificadas a rutas protegidas.
- **FR-006**: El sistema MUST publicar externamente solo el servicio de proxy; API y frontend MUST permanecer accesibles solo dentro de la red interna del stack.
- **FR-007**: El sistema MUST mantener la base de datos como servicio interno por defecto, habilitando exposicion de puerto solo mediante archivo override o perfil local explicito para depuracion.
- **FR-008**: El sistema MUST conectar proxy, frontend, API y base de datos a una red interna dedicada para comunicacion entre servicios.
- **FR-009**: El sistema MUST corregir configuraciones invalidas o mal escritas de variables de entorno relacionadas con la ejecucion del stack.
- **FR-010**: El sistema MUST permitir que el frontend consuma la API por rutas relativas bajo el mismo endpoint publico, evitando dependencias a hosts o puertos hardcodeados.
- **FR-011**: El sistema MUST permitir validacion operativa del stack mediante build limpio, healthcheck en Docker Compose para proxy y backend, y validacion HTTP via proxy de `GET /` y `GET /swagger-ui`.
- **FR-012**: El sistema MUST entregar una guia operativa con comandos exactos para levantar y validar el entorno en local.
- **FR-013**: El sistema MUST validar en pruebas de aceptacion los codigos HTTP exactos de rutas criticas: `GET /` = 200, `POST /auth/login` = 200, `GET /api/v1/empleados` con token valido = 200, `GET /swagger-ui` = 200.
- **FR-014**: El sistema MUST aplicar fallback a `index.html` para rutas de frontend que no sean API, preservando prioridad de enrutamiento para `/api`, `/auth`, `/swagger-ui` y `/v3/api-docs`.

### Security & Authorization Requirements *(mandatory for authenticated flows)*

- El flujo de autenticacion se inicia en `/auth/login` usando el endpoint publico unico; el token Bearer emitido debe conservar su comportamiento actual de expiracion y validez.
- Los clientes deben poder adjuntar token mediante header `Authorization: Bearer <token>` en rutas protegidas bajo `/api`.
- Los accesos sin token o con token invalido a rutas protegidas deben devolver la respuesta de no autorizado definida por el backend.
- La interfaz de usuario debe operar sin dependencia de origen cruzado para autenticacion y consumo de API, reduciendo riesgos de configuraciones CORS inconsistentes.
- La autorizacion de roles (por ejemplo `ADMIN` y `USER`) permanece gobernada por el backend; el proxy no debe modificar decisiones de autorizacion.

### Key Entities *(include if feature involves data)*

- **PuntoDeEntradaHTTP**: Representa el endpoint publico unico; atributos clave: direccion publica, rutas soportadas, estado operativo.
- **ReglaDeEnrutamiento**: Representa la asignacion de prefijos de ruta a servicios internos; atributos clave: prefijo, destino interno, prioridad de evaluacion.
- **SolicitudProxificada**: Representa una solicitud reenviada por el proxy; atributos clave: ruta original, headers reenviados, destino seleccionado, resultado.
- **SesionAutenticada**: Representa una sesion de cliente con token Bearer valido para consumo de endpoints protegidos.

### Assumptions

- El backend ya implementa autenticacion y autorizacion funcional para endpoints protegidos.
- La aplicacion frontend puede operar correctamente cuando API y frontend comparten el mismo origen HTTP.
- El entorno local de despliegue permite build limpio de imagenes y ejecucion de todos los contenedores requeridos.
- La exposicion del puerto de base de datos no forma parte del flujo normal y se considera una excepcion para depuracion local.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de accesos de usuario a frontend y API se realiza a traves de un solo endpoint HTTP publico durante pruebas de aceptacion.
- **SC-002**: Las rutas criticas `GET /`, `POST /auth/login`, `GET /api/v1/empleados` (con token) y `GET /swagger-ui` responden con HTTP 200 en al menos 3 ejecuciones consecutivas de validacion.
- **SC-003**: El tiempo de disponibilidad operativa del stack tras un build limpio no supera 10 minutos en entorno local estandar.
- **SC-004**: No se observan errores de origen cruzado en navegador durante el flujo de login y consumo de endpoints protegidos en pruebas funcionales.
- **SC-005**: Solo un servicio publica puertos al host durante ejecucion estandar del stack.
- **SC-006**: En validacion operativa, proxy y backend reportan estado saludable por healthcheck de Compose antes de ejecutar pruebas funcionales autenticadas.
- **SC-007**: En ejecucion estandar sin override de depuracion, la base de datos no publica puertos al host.
- **SC-008**: Una recarga directa de una ruta frontend profunda (por ejemplo `/empleados/123`) devuelve HTTP 200 y carga la aplicacion sin afectar rutas API.
