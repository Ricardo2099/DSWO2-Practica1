CREATE TABLE IF NOT EXISTS credencial_empleado (
    clave_empleado VARCHAR(32) PRIMARY KEY,
    contrasena_hash VARCHAR(255) NOT NULL,
    intentos_fallidos INT NOT NULL DEFAULT 0,
    bloqueado_hasta TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_credencial_empleado
        FOREIGN KEY (clave_empleado) REFERENCES empleados(clave)
        ON DELETE CASCADE
);
