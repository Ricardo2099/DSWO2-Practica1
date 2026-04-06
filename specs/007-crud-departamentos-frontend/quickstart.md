# Quickstart: CRUD de Departamentos Frontend

## Prerrequisitos
- Node.js 20+
- npm 10+
- Backend ejecutandose en `http://localhost:8080`

## 1) Ejecutar frontend
```bash
cd frontend
npm install
npm run start
```

## 2) Validar flujo principal (ADMIN)
1. Iniciar sesion como `ADMIN`.
2. Abrir `/departamentos`.
3. Crear departamento con `clave` y `nombre`.
4. Editar un departamento y verificar que `clave` esta en solo lectura.
5. Eliminar un departamento sin empleados asociados.

## 3) Validar flujo de consulta (USER)
1. Iniciar sesion como `USER`.
2. Abrir `/departamentos`.
3. Verificar listado y detalle por clave.
4. Verificar que no existen acciones de crear/editar/eliminar.

## 4) Validar escenarios negativos de seguridad
1. Sin token: abrir `/departamentos` y confirmar redireccion a `/login`.
2. Token invalido: forzar token invalido en `localStorage`, consumir API y confirmar limpieza de sesion + redireccion.
3. Rol insuficiente: como `USER`, intentar acciones de escritura y confirmar rechazo visual/backend.

## 5) Validar conflictos de negocio
1. Forzar `DELETE` de departamento con empleados asociados y confirmar mensaje de bloqueo.
2. Forzar conflicto `409` en `PUT` y confirmar recarga de datos + prompt de reintento.

## 6) Validar SC-001 (medicion de rendimiento)
1. Cargar `/departamentos` 10 veces con backend local estable.
2. Medir tiempo desde inicio de carga hasta render de tabla.
3. Registrar promedio y porcentaje de ejecuciones < 2s.

## 7) Registro de evidencia
- Completar aqui resultados de T038, T039, T040, T041 y medicion SC-001.
- Fecha: 2026-03-25
- Entorno: Frontend Angular local (`npm run test -- --watch=false`, `npm run build`).
- Resultado sin token: Validado por `auth.guard.spec.ts` (redirige a `/login`) y caso sin Authorization en `auth.interceptor.spec.ts`.
- Resultado token invalido: Validado por `auth.interceptor.spec.ts` (respuesta 401 limpia sesion y navega a `/login`).
- Resultado rol insuficiente: Validado por `auth.interceptor.spec.ts` (403 sin limpiar sesion) y por visibilidad de controles en componentes por rol.
- Resultado E2E: Pendiente de validacion manual con backend en ejecucion y recorrido completo de UI.
- Promedio SC-001: Pendiente de medicion manual en ambiente integrado.
