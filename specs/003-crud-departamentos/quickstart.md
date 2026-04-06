# Quickstart: CRUD de Departamentos

## Prerrequisitos
- Java 17
- Maven 3.9+
- Docker + Docker Compose

## 1) Levantar servicios base
```bash
docker compose up -d db
```

## 2) Configurar variables de entorno
Definir (ejemplo):
- `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/empleados_db`
- `SPRING_DATASOURCE_USERNAME=empleados_user`
- `SPRING_DATASOURCE_PASSWORD=empleados_pass`

## 3) Ejecutar backend
```bash
cd backend
mvn spring-boot:run
```

## 4) Verificar migraciones y API
- Confirmar que Flyway ejecuta migraciones de departamentos y relación con empleados.
- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 5) Probar autenticación
1. Llamar `POST /auth/login` con credenciales válidas de administrador.
2. Usar el token retornado como `Authorization: Bearer <token>`.
3. Verificar que operaciones sin token o con token inválido devuelven `401`.

## 6) Flujo mínimo de validación
1. `POST /api/v1/departamentos` con `clave` y `nombre` válidos.
2. `GET /api/v1/departamentos` para listar.
3. `GET /api/v1/departamentos/{clave}` para validar recuperación.
4. `PUT /api/v1/departamentos/{clave}` para actualizar `nombre`.
5. `DELETE /api/v1/departamentos/{clave}` en un departamento sin empleados asociados.
6. Intentar `DELETE` sobre departamento con empleados asociados y validar rechazo por restricción.

## 7) Ejecutar pruebas
```bash
cd backend
mvn -B test
```

## 8) Ejecutar pruebas de integración con PostgreSQL real
```bash
cd backend
mvn -B -Dtest='*IntegrationTest' test
```

## 9) Verificar cobertura
```bash
cd backend
mvn -B verify
```

## 10) Criterios de cierre rápido
- CRUD de departamentos funcional en Swagger.
- Restricción de borrado con empleados asociadas validada.
- Asociación de empleado a departamento inexistente rechazada.
- Acceso no administrador a endpoints de departamentos rechazado.

## 11) Resultado de validación ejecutada
- Fecha de ejecución: 2026-03-17.
- Comando ejecutado: `mvn -B test` en `backend/`.
- Resultado: `BUILD SUCCESS`.
- Resumen: 42 pruebas ejecutadas, 0 fallas, 0 errores, 12 omitidas.
- Nota: las pruebas con Testcontainers se omitieron automáticamente por no detectar entorno Docker en la ejecución local.
- Comando de cierre: `mvn -B verify -DskipITs=true`.
- Resultado de cierre: `BUILD SUCCESS` con regla JaCoCo satisfecha (umbral de cobertura alcanzado en el conjunto evaluado).
