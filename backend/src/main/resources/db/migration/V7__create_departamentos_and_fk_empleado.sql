CREATE TABLE IF NOT EXISTS departamentos (
    clave VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_departamentos_nombre ON departamentos (nombre);

ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS departamento_clave VARCHAR(50);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_empleados_departamento'
    ) THEN
        ALTER TABLE empleados
            ADD CONSTRAINT fk_empleados_departamento
            FOREIGN KEY (departamento_clave)
            REFERENCES departamentos (clave)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT;
    END IF;
END
$$;

CREATE INDEX IF NOT EXISTS idx_empleados_departamento_clave
    ON empleados (departamento_clave);
