CREATE TABLE IF NOT EXISTS evento_seguridad (
    id BIGSERIAL PRIMARY KEY,
    clave_empleado VARCHAR(32),
    tipo VARCHAR(32) NOT NULL,
    ocurrido_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    detalle TEXT,
    CONSTRAINT fk_evento_empleado
        FOREIGN KEY (clave_empleado) REFERENCES empleados(clave)
        ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_evento_seguridad_tipo_fecha
    ON evento_seguridad (tipo, ocurrido_en DESC);
