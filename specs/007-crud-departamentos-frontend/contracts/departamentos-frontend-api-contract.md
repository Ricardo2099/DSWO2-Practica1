# Contrato Frontend-Backend: Departamentos

## Base URL
- `http://localhost:8080`

## Endpoints consumidos por frontend
- `GET /api/v1/departamentos`
- `GET /api/v1/departamentos/{clave}`
- `POST /api/v1/departamentos`
- `PUT /api/v1/departamentos/{clave}`
- `DELETE /api/v1/departamentos/{clave}`

## Reglas funcionales de consumo
- El listado se carga una vez y la paginacion se aplica en cliente.
- En edicion, `clave` se muestra en solo lectura; solo `nombre` es editable.
- Si `PUT` retorna `409`, frontend recarga detalle y solicita confirmacion para reintentar.
- Si `DELETE` retorna `409`, frontend muestra mensaje de bloqueo por empleados asociados.

## Matriz de rol en interfaz
- `ADMIN`: ver, crear, editar, eliminar.
- `USER`: ver listado y detalle; sin controles de escritura.

## Manejo de errores esperado
- `401`: limpiar sesion y redirigir a `/login`.
- `403`: mostrar mensaje de permisos insuficientes.
- `404`: mostrar mensaje de recurso no encontrado en detalle.
- `409`: mostrar mensaje contextual (conflicto o bloqueo por asociados).

## Fuente OpenAPI
- Ver [departamentos.openapi.yaml](./departamentos.openapi.yaml).
