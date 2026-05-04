# DSWO2 Practica 1

## Arquitectura de ejecucion local

El stack se ejecuta en contenedores Docker con un unico punto de entrada HTTP publico:

- `reverse-proxy` (Nginx): publica `http://localhost:80`.
- `frontend` (Angular + Nginx): servicio interno, sin puertos publicados.
- `api` (Spring Boot): servicio interno, sin puertos publicados.
- `db` (PostgreSQL): servicio interno, sin puertos publicados en compose base.

Todos los servicios comparten la red interna `internal-net`.

## Enrutamiento en proxy

- `/api/*` -> `api:8080`
- `/auth/*` -> `api:8080`
- `/swagger-ui` y `/swagger-ui/*` -> `api:8080`
- `/v3/api-docs*` -> `api:8080`
- `/` y rutas no API -> `frontend:80` (fallback SPA)

El proxy preserva headers:

- `Host`
- `X-Real-IP`
- `X-Forwarded-For`
- `X-Forwarded-Proto`

y mantiene `Authorization: Bearer <token>` sin modificaciones.

## Debug local de base de datos (opcional)

El compose base no expone el puerto de `db`. Para debug local se puede habilitar override:

```powershell
docker compose -f docker-compose.yml -f docker-compose.debug-db.yml up -d
```

<!-- CI trigger: cambio sin impacto -->
