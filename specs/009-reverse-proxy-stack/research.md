# Research: Reverse Proxy En Docker Compose

## Decision 1: Usar Nginx dedicado como unico punto de entrada
- Decision: Agregar un servicio `reverse-proxy` (Nginx) en Compose y publicar solo `80:80` en host.
- Rationale: Centraliza entrada HTTP para frontend y backend, elimina CORS entre apps bajo el mismo origen y reduce superficie expuesta.
- Alternatives considered:
  - Exponer frontend y backend en puertos separados: mantiene CORS y duplica entrada publica.
  - Mover frontend al backend (jar static): rompe separacion actual de proyectos y aumenta acoplamiento.

## Decision 2: Definir reglas de enrutamiento por prefijo con prioridad API
- Decision: Enrutar `/api`, `/auth`, `/swagger-ui`, `/v3/api-docs` a `api:8080`; enrutar `/` y rutas no API a `frontend:80` con fallback SPA.
- Rationale: Preserva contratos existentes del backend y soporta rutas profundas del frontend (`/empleados/123`) sin 404 en recarga.
- Alternatives considered:
  - Redireccionar rutas desconocidas a `/`: altera UX de SPA y cambia semantica HTTP.
  - No fallback: rompe navegacion directa en rutas internas.

## Decision 3: Mantener headers de forwarding estandar y compatibilidad Bearer
- Decision: Configurar `proxy_set_header Host`, `X-Real-IP`, `X-Forwarded-For`, `X-Forwarded-Proto`; no modificar header `Authorization`.
- Rationale: Mantiene trazabilidad, esquema original y autenticacion Bearer end-to-end sin adaptar backend.
- Alternatives considered:
  - Confiar en defaults de Nginx sin headers explicitos: comportamiento menos deterministico entre entornos.
  - Reescribir tokens en proxy: agrega complejidad y riesgo de seguridad.

## Decision 4: Health checks obligatorios para proxy y backend
- Decision: Definir `healthcheck` de Compose para `reverse-proxy` y `api`; validar ademas `GET /` y `GET /swagger-ui` via proxy antes de pruebas autenticadas.
- Rationale: Evita ejecutar pruebas cuando solo el contenedor esta vivo pero la app aun no lista.
- Alternatives considered:
  - Verificar solo `docker compose ps`: puede dar falsos positivos de readiness.
  - Validar solo endpoints HTTP sin healthcheck de Compose: pierde señal interna de orquestacion.

## Decision 5: Mantener base de datos interna por defecto con override local
- Decision: Quitar publicacion de puerto de `db` en Compose base y ofrecer `docker-compose.debug-db.yml` opcional para debug local.
- Rationale: Cumple principio de minimizacion de exposicion y mantiene capacidad de diagnostico cuando se requiere.
- Alternatives considered:
  - DB siempre expuesta: aumenta superficie de ataque innecesariamente.
  - DB nunca expuesta: dificulta troubleshooting local.

## Decision 6: Cambiar frontend a base URL relativa
- Decision: Ajustar estrategia de `apiBaseUrl` para consumir rutas relativas (`/api/...`, `/auth/...`) bajo el mismo origen del proxy.
- Rationale: Elimina dependencia hardcodeada a `http://localhost:8080` y evita CORS en navegador.
- Alternatives considered:
  - Mantener `http://localhost:8080`: rompe objetivo de endpoint unico.
  - Resolver por variable absoluta en runtime: mayor complejidad para local si ya hay origen unificado.

## Decision 7: Corregir variable mal escrita en Compose
- Decision: Corregir `SPRING_DATASOURCE_PASSWORDq` a `SPRING_DATASOURCE_PASSWORD`.
- Rationale: La clave actual impide inyeccion de credenciales esperada por Spring Boot y puede causar fallos de conexion.
- Alternatives considered:
  - Mantener typo y compensar en app: enmascara un defecto de configuracion base.
