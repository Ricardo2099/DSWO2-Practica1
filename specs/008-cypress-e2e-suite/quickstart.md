# Quickstart: Suite E2E con Cypress

## Prerrequisitos
- Docker y Docker Compose disponibles.
- Node.js 20+ y npm.
- Credenciales validas para roles `ADMIN` y `USER`.

## 1) Levantar backend
```bash
docker-compose up -d
```

## 2) Levantar frontend
```bash
cd frontend
npm install
ng serve
```

## 3) Configurar variables de entorno de Cypress
En la misma terminal donde ejecutaras Cypress:

```bash
set CYPRESS_ADMIN_EMAIL=admin@example.com
set CYPRESS_ADMIN_PASSWORD=admin123
set CYPRESS_USER_EMAIL=user@example.com
set CYPRESS_USER_PASSWORD=user123
set CYPRESS_API_URL=http://localhost:8080
```

## 4) Ejecutar Cypress en modo interactivo
```bash
cd frontend
npx cypress open
```

## 5) Ejecutar suite completa en headless
```bash
cd frontend
npx cypress run
```

## 6) Especificaciones esperadas
- `cypress/e2e/auth.cy.ts`
- `cypress/e2e/departamentos.cy.ts`
- `cypress/e2e/empleados.cy.ts`

## 7) Validaciones clave por spec
1. `auth.cy.ts`
- Login exitoso ADMIN.
- Login fallido por credenciales invalidas.
- Persistencia de sesion.
- Redireccion a login sin token o con token invalido.

2. `departamentos.cy.ts`
- Crear departamento como ADMIN.
- Ver listado y verificar aparicion en tabla.
- Screenshot hito tras crear departamento.

3. `empleados.cy.ts`
- Crear empleado con departamento asignado como ADMIN.
- Ver listado y verificar aparicion.
- Eliminar empleado y verificar desaparicion.
- Screenshots hitos tras crear y eliminar empleado.

4. Autorizacion de rol
- USER solo lectura (sin acciones de escritura habilitadas).
- Intento forzado de escritura devuelve 403 visible.

## 8) Evidencia generada
- Videos por spec: `frontend/cypress/videos/`.
- Screenshots en fallo: `frontend/cypress/screenshots/`.
- Screenshots manuales en hitos:
  - despues de login
  - despues de crear departamento
  - despues de crear empleado
  - despues de eliminar empleado

## 9) Troubleshooting rapido
- Si falla login API: validar backend activo y credenciales de entorno.
- Si falla por CORS/red: confirmar que frontend corre en `http://localhost:4200` y backend en `http://localhost:8080`.
- Si aparecen flaky tests: revisar uso de `data-testid`, tiempos de espera implicitos y limpieza de datos por test.
