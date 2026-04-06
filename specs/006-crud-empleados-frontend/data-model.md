# Data Model: CRUD de Empleados Frontend

## Entity: EmpleadoListItem

### Description
Representa el registro mostrado en la tabla paginada de empleados.

### Fields
- `clave` (string, required)
- `nombre` (string, required)
- `direccion` (string, required)
- `telefono` (string, required)
- `correo` (string, required)
- `departamentoClave` (string, required)

### Validation Rules
- Campos obligatorios para renderizado de tabla.
- `correo` debe mostrarse en formato valido recibido del backend.

## Entity: EmpleadoCreatePayload

### Description
Payload enviado al crear empleado desde formulario reactivo.

### Fields
- `nombre` (string, required)
- `direccion` (string, required)
- `telefono` (string, required)
- `correo` (string, required)
- `departamentoClave` (string, required)

### Validation Rules
- Todos los campos son obligatorios para habilitar submit.
- `departamentoClave` debe corresponder a una opcion cargada del catalogo.

## Entity: DepartamentoOption

### Description
Representa una opcion de departamento para el selector en formulario.

### Fields
- `clave` (string, required)
- `nombre` (string, required)

### Validation Rules
- El selector debe bloquear envio mientras no exista seleccion valida.

## Entity: PagedEmpleadosResponse

### Description
Contrato esperado de listado paginado de empleados.

### Fields
- `content` (EmpleadoListItem[], required)
- `totalElements` (number, required)
- `totalPages` (number, required)
- `number` (number, required)
- `size` (number, required)

### State Transitions
- `idle -> loading -> loaded`: carga de pagina exitosa.
- `loading -> error`: fallo de red/servidor, con mensaje snackbar.

## Entity: SessionRole

### Description
Estado de autorizacion derivado de la sesion autenticada.

### Fields
- `role` (`ADMIN` | `USER`, required)

### State Transitions
- `ADMIN`: habilita acciones de crear y eliminar.
- `USER`: mantiene solo lectura y oculta/deshabilita acciones de escritura.
