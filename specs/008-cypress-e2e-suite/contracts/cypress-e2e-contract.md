# Contrato E2E Frontend-Backend: Suite Cypress

## Base URLs
- Frontend: `http://localhost:4200`
- Backend API: `http://localhost:8080`

## Alcance funcional cubierto
- Autenticacion (`/auth/login`) con roles `ADMIN` y `USER`.
- CRUD principal de departamentos (alta y verificacion en listado).
- CRUD principal de empleados (alta con departamento, verificacion en listado, baja y verificacion de desaparicion).
- Autorizacion por rol (UI read-only para USER y rechazo 403 al forzar escritura).
- Escenarios negativos de sesion (sin token y token invalido).

## Matriz de escenarios por spec
1. `auth.cy.ts`
- Login UI exitoso (ADMIN).
- Login UI fallido.
- Persistencia de sesion.
- Redireccion sin token.
- Logout/limpieza por token invalido.

2. `departamentos.cy.ts`
- Login por comando `loginAdmin()`.
- Crear departamento unico.
- Verificar aparicion en tabla.
- Captura manual post-creacion.

3. `empleados.cy.ts`
- Login por comando `loginAdmin()`.
- Crear empleado asociado a departamento.
- Verificar aparicion en tabla.
- Eliminar empleado y verificar desaparicion.
- Capturas manuales post-creacion y post-eliminacion.

4. Autorizacion USER (incluida en `auth.cy.ts` o spec dedicada)
- Login por comando `loginUser()`.
- Confirmar acciones de escritura no disponibles en UI.
- Forzar solicitud de escritura y validar 403 visible.

## Comandos custom obligatorios
- `loginAdmin()`
- `loginUser()`

### Reglas de implementacion de comandos
- Deben autenticar contra API real (`POST /auth/login`).
- Deben persistir token y rol en `localStorage` para sesion de test.
- Deben fallar con mensaje claro si faltan variables de entorno.

## Datos de prueba
- Cada test crea datos unicos (p.ej. sufijo timestamp).
- Todo recurso creado en test debe limpiarse dentro del mismo test.
- No se permite dependencia entre tests.

## Politica de evidencia visual
- Video: habilitado en todas las corridas.
- Screenshot automatico: en fallo.
- Screenshot manual obligatorio en hitos:
  - despues de login
  - despues de crear departamento
  - despues de crear empleado
  - despues de eliminar empleado

## Criterios de aceptacion de ejecucion
- Corrida completa finaliza con los tres specs entregables.
- Se generan artifacts de evidencia en carpetas de Cypress.
- No hay dependencia de orden entre specs para pasar.
