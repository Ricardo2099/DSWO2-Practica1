# Contract: Empleados Frontend API Integration

## Base URL
- `http://localhost:8080`

## Security
- Header requerido en endpoints protegidos:
  - `Authorization: Bearer <token>`

## 1) Listar empleados paginados
- **Method**: `GET`
- **Path**: `/api/v1/empleados`
- **Query Params**:
  - `page` (number, required)
  - `size` (number, required)
- **Success Response (200)**:

```json
{
  "content": [
    {
      "clave": "EMP-101",
      "nombre": "Ana Perez",
      "direccion": "Av. Centro 123",
      "telefono": "555123456",
      "correo": "ana@empresa.local",
      "departamentoClave": "IT"
    }
  ],
  "totalElements": 120,
  "totalPages": 12,
  "number": 0,
  "size": 10
}
```

## 2) Crear empleado
- **Method**: `POST`
- **Path**: `/api/v1/empleados`
- **Body (required)**:

```json
{
  "nombre": "Ana Perez",
  "direccion": "Av. Centro 123",
  "telefono": "555123456",
  "correo": "ana@empresa.local",
  "departamentoClave": "IT"
}
```

- **Success Response (201)**: devuelve el objeto empleado creado en el cuerpo.
- **Ejemplo de respuesta 201**:

```json
{
  "clave": "EMP-101",
  "nombre": "Ana Perez",
  "direccion": "Av. Centro 123",
  "telefono": "555123456",
  "correo": "ana@empresa.local",
  "departamentoClave": "IT"
}
```
- **Failure Codes**:
  - `400` validacion
  - `401` no autenticado
  - `403` rol insuficiente
  - `409` conflicto de negocio

## 3) Eliminar empleado
- **Method**: `DELETE`
- **Path**: `/api/v1/empleados/{clave}`
- **Success Response**: `204 No Content`
- **Failure Codes**:
  - `401` no autenticado
  - `403` rol insuficiente
  - `404` no encontrado

## 4) Cargar departamentos para selector
- **Method**: `GET`
- **Path**: `/api/v1/departamentos`
- **Success Response (200)**:

```json
[
  { "clave": "IT", "nombre": "Tecnologia" },
  { "clave": "HR", "nombre": "Recursos Humanos" }
]
```

## Role Matrix (UI behavior)
- `ADMIN`: listar, crear, eliminar.
- `USER`: listar solamente.
- La UI adapta visibilidad, pero la autorizacion final la valida backend.
