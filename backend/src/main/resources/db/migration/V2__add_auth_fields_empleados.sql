ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS correo VARCHAR(255);

UPDATE empleados
SET correo = LOWER(clave) || '@empleados.local'
WHERE correo IS NULL;

ALTER TABLE empleados
    ALTER COLUMN correo SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_empleados_correo_lower
    ON empleados (LOWER(correo));

ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS habilitado BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS rol VARCHAR(20) NOT NULL DEFAULT 'USER';
