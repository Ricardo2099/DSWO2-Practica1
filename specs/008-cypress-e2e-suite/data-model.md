# Data Model: Suite E2E de Sistema Completo

## Entity: E2ECredentials

### Description
Credenciales y roles utilizados por Cypress para autenticar sesiones de prueba contra backend real.

### Fields
- `email` (string)
  - Reglas: obligatorio, proveniente de variable de entorno.
- `password` (string)
  - Reglas: obligatorio, no versionado en repositorio.
- `role` (enum: `ADMIN` | `USER`)
  - Reglas: define comandos custom y expectativas de autorizacion.

### Validation Rules
- Deben existir credenciales validas para al menos un `ADMIN` y un `USER`.
- Si faltan variables, la suite falla con mensaje explicito de configuracion.

## Entity: AuthSessionState

### Description
Estado de sesion generado por login API y reutilizado durante cada test.

### Fields
- `token` (string)
- `role` (enum: `ADMIN` | `USER`)
- `expiresAt` (string | null)

### State Transitions
- `UNAUTHENTICATED -> AUTHENTICATED` tras `POST /auth/login` exitoso.
- `AUTHENTICATED -> UNAUTHENTICATED` ante logout explicito o respuesta 401/token invalido.

## Entity: E2EDepartamentoSeed

### Description
Dato de prueba para alta de departamento en escenarios ADMIN.

### Fields
- `clave` (string)
  - Reglas: unica por test (sufijo temporal).
- `nombre` (string)
  - Reglas: obligatorio.
- `createdByTestId` (string)
  - Reglas: permite trazabilidad y limpieza.

## Entity: E2EEmpleadoSeed

### Description
Dato de prueba para alta de empleado vinculado a departamento.

### Fields
- `nombres` (string)
- `apellidoPaterno` (string)
- `apellidoMaterno` (string)
- `email` (string)
  - Reglas: unico por test.
- `departamentoClave` (string)
  - Reglas: debe existir previamente.
- `createdByTestId` (string)

### Relationship Rules
- Todo `E2EEmpleadoSeed` DEBE referenciar un `E2EDepartamentoSeed` existente.
- La limpieza de empleado DEBE ocurrir antes de eliminar su departamento asociado.

## Entity: E2EScenarioEvidence

### Description
Evidencia visual y metadata por escenario para auditoria funcional.

### Fields
- `specName` (string)
- `testName` (string)
- `videoPath` (string)
- `failureScreenshots` (string[])
- `milestoneScreenshots` (string[])
- `status` (enum: `passed` | `failed`)

### Validation Rules
- Cada corrida genera video por spec.
- Deben existir screenshots manuales en hitos de negocio definidos.
- Si un test falla, Cypress debe adjuntar screenshot automatico.
