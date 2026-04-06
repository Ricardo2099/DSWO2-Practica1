INSERT INTO empleados (clave, nombre, direccion, telefono, correo, habilitado, rol, created_at, updated_at)
VALUES (
    'EMP-ADMIN',
    'Administrador',
    'N/A',
    'N/A',
    'admin@empleados.local',
    TRUE,
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (clave) DO NOTHING;

INSERT INTO credencial_empleado (clave_empleado, contrasena_hash, intentos_fallidos, bloqueado_hasta, updated_at)
VALUES (
    'EMP-ADMIN',
    '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiW2zW7x1YgnSUQvBYwygJyI072QtdG',
    0,
    NULL,
    CURRENT_TIMESTAMP
)
ON CONFLICT (clave_empleado) DO NOTHING;
