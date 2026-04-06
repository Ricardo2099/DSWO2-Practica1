CREATE TABLE IF NOT EXISTS sesion_acceso (
    token VARCHAR(36) PRIMARY KEY,
    clave_empleado VARCHAR(32) NOT NULL,
    iniciada_en TIMESTAMP NOT NULL,
    expira_en TIMESTAMP NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_sesion_empleado
        FOREIGN KEY (clave_empleado) REFERENCES empleados(clave)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_sesion_empleado_activa
    ON sesion_acceso (clave_empleado, activa);
