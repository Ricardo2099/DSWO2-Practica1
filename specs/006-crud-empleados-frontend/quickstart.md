# Quickstart: CRUD de Empleados Frontend

## Prerrequisitos
- Node.js 20+
- npm 10+
- Angular CLI 22+
- Backend levantado en `http://localhost:8080`

## 1) Levantar backend
Desde raiz del repositorio:

```bash
docker compose up -d db backend
```

Verificar salud:
- `http://localhost:8080/actuator/health`

## 2) Crear o preparar frontend independiente

```bash
cd frontend
npm install
```

## 3) Ejecutar frontend en desarrollo

```bash
npm run start
```

Aplicacion en:
- `http://localhost:4200`

## 4) Validar flujo de autenticacion
1. Iniciar sesion desde `/login`.
2. Verificar que se guarda token y rol en `localStorage`.
3. Confirmar redireccion a rutas protegidas con sesion valida.

## 5) Validar listado paginado de empleados
1. Navegar a `/empleados`.
2. Confirmar llamada `GET /api/v1/empleados?page=0&size=10`.
3. Cambiar pagina/tamano y verificar actualizacion de tabla.

## 6) Validar creacion de empleado (rol ADMIN)
1. Abrir formulario de alta en `/empleados`.
2. Confirmar carga de departamentos por `GET /api/v1/departamentos`.
3. Enviar payload con `nombre`, `direccion`, `telefono`, `correo`, `departamentoClave`.
4. Verificar snackbar de exito y refresco de tabla.

## 7) Validar eliminacion de empleado (rol ADMIN)
1. Ejecutar accion eliminar en una fila.
2. Verificar llamada `DELETE /api/v1/empleados/{clave}`.
3. Confirmar snackbar y retiro del registro en la tabla.

## 8) Validar permisos de rol USER
1. Iniciar sesion con usuario USER.
2. Verificar visibilidad de tabla.
3. Confirmar que botones de crear y eliminar no se muestran o quedan deshabilitados.

## 9) Ejecutar pruebas frontend

```bash
npm run test
```

Cobertura minima esperada:
- Servicio de empleados
- Componente de empleados
- Reglas de visibilidad por rol
- Manejo de errores/snackbar

## 10) Validacion final de feature
1. Ejecutar pruebas unitarias: `npm run test -- --watch=false`.
2. Verificar en Network que todas las llamadas usan `http://localhost:8080`.
3. Confirmar que `USER` no puede crear ni eliminar desde UI.
4. Confirmar que `ADMIN` puede crear y eliminar con feedback por snackbar.

## 11) Evidencia de seguridad negativa (SR-006)
Registrar resultados para:
- Solicitud sin token -> esperado `401`.
- Solicitud con token invalido -> esperado `401` y redireccion a `/login`.
- Solicitud con rol insuficiente (`USER` en DELETE) -> esperado `403`.

Plantilla de evidencia:

```text
Fecha:
Usuario/rol probado:
Escenario:
Resultado HTTP observado:
Comportamiento UI observado:
```

## 12) Protocolo de medicion SC-001 y SC-006
- Dataset: minimo 100 empleados precargados en backend.
- Tamaños de pagina: 5, 10 y 20.
- Repeticiones por caso: 20 solicitudes por combinacion (page,size).
- Evidencia: capturas de Network + tabla de tiempos por solicitud.

Tabla de registro sugerida:

```text
Caso | page | size | latencia(ms) | exito
P1   | 0    | 10   |              |
P2   | 1    | 10   |              |
...
```

Calculo de validacion:
- SC-001 cumple si el percentil 95 de latencia de listado es < 2000 ms.
- SC-006 cumple si 100% de cambios de pagina/tamano retornan datos consistentes sin recarga manual.
