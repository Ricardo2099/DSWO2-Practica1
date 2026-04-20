# Contract: Reverse Proxy Routing And Validation

## Public Entry Point
- Base URL: `http://localhost`
- Public exposed port: `80`

## Upstream Services
- `api` upstream: `http://api:8080`
- `frontend` upstream: `http://frontend:80`

## Routing Contract (priority order)
1. `/api/*` -> `api`
2. `/auth/*` -> `api`
3. `/swagger-ui/*` -> `api`
4. `/v3/api-docs/*` -> `api`
5. `/` and any non-API path -> `frontend` (SPA fallback to `index.html`)

## Header Forwarding Contract
For every proxied request, proxy must forward:
- `Host`
- `X-Real-IP`
- `X-Forwarded-For`
- `X-Forwarded-Proto`

Additionally:
- If client sends `Authorization: Bearer <token>`, proxy must pass it unchanged to `api` upstream.

## Security/Auth Contract
- `POST /auth/login` is publicly reachable through proxy and must preserve backend response semantics.
- Protected endpoints under `/api/*` remain enforced by backend authorization rules.
- Proxy must not implement role decisions; backend remains final authority.

## Health/Readiness Contract
Before functional auth tests run:
- Compose healthcheck for `reverse-proxy` = healthy.
- Compose healthcheck for `api` = healthy.
- `GET /` through proxy returns HTTP 200.
- `GET /swagger-ui` through proxy returns HTTP 200.

## Functional Validation Contract (expected HTTP status)
- `GET /` -> `200`
- `POST /auth/login` -> `200`
- `GET /api/v1/empleados` with valid Bearer token -> `200`
- `GET /swagger-ui` -> `200`
- `GET /api/v1/empleados` without token -> `401`
- `GET /empleados/123` (frontend deep route) -> `200`

## Network Exposure Contract
- Base compose publishes host ports only for `reverse-proxy`.
- `api`, `frontend`, and `db` are internal-only in base compose.
- DB host exposure is allowed only via explicit local debug override compose file.

## Operational Validation Contract
- Build mode: `docker compose build --no-cache`.
- Readiness gate before auth tests:
	- `empleados-api` health = `healthy`
	- `empleados-reverse-proxy` health = `healthy`
- CORS operational check: preflight `OPTIONS /api/v1/empleados` returns HTTP `200` in local validation.
