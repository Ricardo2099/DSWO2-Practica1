# Quickstart: CRUD de Empleados

## Prerrequisitos
- Java 17
- Maven 3.9+
- Docker + Docker Compose

## 1) Levantar PostgreSQL con Docker
```bash
docker compose up -d db
```

## 2) Configurar variables de entorno
Definir (ejemplo):
- `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/empleados_db`
- `SPRING_DATASOURCE_USERNAME=empleados_user`
- `SPRING_DATASOURCE_PASSWORD=empleados_pass`

## 3) Ejecutar aplicación
```bash
cd backend
mvn spring-boot:run
```

## 4) Verificar API y Swagger
- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 5) Probar autenticación básica
Usar encabezado `Authorization: Basic ...` en cada endpoint.

## 6) Flujo mínimo de validación
1. `POST /api/v1/empleados` con `nombre`, `direccion`, `telefono` válidos (la `clave` la genera el sistema).
2. `GET /api/v1/empleados/{clave}` para confirmar persistencia.
3. `PUT /api/v1/empleados/{clave}` cambiando `nombre`, `direccion` o `telefono`.
4. `DELETE /api/v1/empleados/{clave}` y luego `GET` para validar `404`.

## 7) Ejecutar pruebas
```bash
cd backend
mvn -B test
```

## 8) Ejecutar pruebas de integración con Testcontainers
Asegurar Docker activo y luego:
```bash
cd backend
mvn -B -Dtest='*IntegrationTest' test
```

## 9) Verificación de cobertura mínima
```bash
cd backend
mvn -B verify
```

## 10) Resultado de validación integral (ejecutado)
- Flujo CRUD validado por pruebas de contrato de creación, consulta, actualización y eliminación.
- Seguridad negativa validada: solicitudes sin credenciales o inválidas retornan `401`.
- Cobertura validada en `verify`: regla JaCoCo de 85% cumplida.
- Rendimiento validado para listado (`GET /api/v1/empleados`) con prueba automatizada de p95 `< 300 ms`.
- Si Docker no está disponible localmente, las pruebas Testcontainers se marcan como `skipped` y no bloquean la ejecución.
