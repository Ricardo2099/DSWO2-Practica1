# Quickstart: Reverse Proxy Stack

## 1) Build limpio y arranque

```powershell
docker compose down -v --remove-orphans
docker compose build --no-cache
docker compose up -d
```

## 2) Verificar estado de contenedores y health

```powershell
docker compose ps
docker inspect --format='{{.State.Health.Status}}' empleados-api
docker inspect --format='{{.State.Health.Status}}' empleados-reverse-proxy
```

Esperado:
- `empleados-api` en `healthy`
- `empleados-reverse-proxy` en `healthy`

## 3) Ejecutar scripts de validacion funcional

```powershell
./specs/009-reverse-proxy-stack/scripts/test-root.ps1
./specs/009-reverse-proxy-stack/scripts/test-swagger.ps1
./specs/009-reverse-proxy-stack/scripts/test-spa-fallback.ps1
$token = ./specs/009-reverse-proxy-stack/scripts/test-auth-login.ps1
./specs/009-reverse-proxy-stack/scripts/test-empleados-auth.ps1 -Token $token
./specs/009-reverse-proxy-stack/scripts/test-auth-negative.ps1
./specs/009-reverse-proxy-stack/scripts/test-exposed-ports.ps1
./specs/009-reverse-proxy-stack/scripts/test-internal-network.ps1
./specs/009-reverse-proxy-stack/scripts/test-db-debug-override.ps1
```

## 4) Probar preflight CORS (control operativo)

```powershell
$response = Invoke-WebRequest -Uri "http://localhost/api/v1/empleados" -Method Options -Headers @{ Origin = "http://localhost"; "Access-Control-Request-Method" = "GET" } -UseBasicParsing
$response.StatusCode
```

Esperado:
- HTTP `200`.

## 5) Comandos HTTP directos de referencia

```powershell
curl -i http://localhost/
curl -i http://localhost/swagger-ui
curl -i -X POST http://localhost/auth/login -H "Content-Type: application/json" -d '{"correo":"admin@empleados.local","contrasena":"password"}'
```

Esperado:
- `GET /` -> `200`
- `GET /swagger-ui` -> `200`
- `POST /auth/login` -> `200`

## 6) Verificar endpoint protegido con token

```powershell
$login = Invoke-RestMethod -Method Post -Uri "http://localhost/auth/login" -ContentType "application/json" -Body '{"correo":"admin@empleados.local","contrasena":"password"}'
$token = $login.token
curl -i http://localhost/api/v1/empleados -H "Authorization: Bearer $token"
```

Esperado:
- HTTP `200`

## 7) Verificar que solo el proxy publica puertos

```powershell
docker compose ps
```

Esperado:
- Solo `reverse-proxy` publica puertos al host.

## 8) (Opcional) habilitar debug local de DB

```powershell
docker compose -f docker-compose.yml -f docker-compose.debug-db.yml up -d
```

Esperado:
- DB publica puerto solo cuando se usa override.

## Resultado de validacion ejecutada (2026-04-09)

- Estado de contenedores:
	- `empleados-api`: healthy
	- `empleados-reverse-proxy`: healthy
- Pruebas scriptadas:
	- `GET /` -> 200 (PASS)
	- `GET /swagger-ui` -> 200 (PASS)
	- `GET /empleados/123` (fallback SPA) -> 200 (PASS)
	- `POST /auth/login` -> 200 (PASS)
	- `GET /api/v1/empleados` con Bearer -> 200 (PASS)
	- `GET /api/v1/empleados` sin token -> 401 (PASS)
	- Verificacion de puertos publicados -> solo reverse-proxy (PASS)
	- Verificacion de red interna -> todos los servicios en `dswo2-practica1_internal-net` (PASS)
	- Verificacion de override DB -> expone `5432` solo con override (PASS)
- Preflight CORS operativo sobre `/api/v1/empleados` -> 200 (PASS)

## Nota sobre credenciales locales para pruebas

Si `POST /auth/login` retorna 401 en un entorno local previamente alterado, se puede normalizar la credencial admin para pruebas:

```powershell
docker exec empleados-postgres psql -U empleados_user -d empleados_db -c "CREATE EXTENSION IF NOT EXISTS pgcrypto;"
docker exec empleados-postgres psql -U empleados_user -d empleados_db -c "update credencial_empleado set contrasena_hash = crypt('password', gen_salt('bf', 10)), intentos_fallidos = 0, bloqueado_hasta = null where clave_empleado='EMP-ADMIN';"
```
