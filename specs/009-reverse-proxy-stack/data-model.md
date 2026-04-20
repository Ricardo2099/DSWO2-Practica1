# Data Model: Reverse Proxy Stack

## Entity: ReverseProxyService
- Description: Servicio Nginx expuesto al host que recibe todo el trafico HTTP de entrada.
- Fields:
  - serviceName (string, required, fixed=`reverse-proxy`)
  - publicPort (number, required, fixed=80)
  - configMountPath (string, required)
  - healthcheckPath (string, required, default=`/`)
  - status (enum: `created|starting|healthy|unhealthy|stopped`)
- Relationships:
  - routesTo -> ApiService
  - routesTo -> FrontendService
  - connectedTo -> InternalNetwork

## Entity: ProxyRoutingRule
- Description: Regla declarativa de enrutamiento por prefijo/ubicacion en Nginx.
- Fields:
  - ruleId (string, required)
  - pathPattern (string, required; one of `/api`, `/auth`, `/swagger-ui`, `/v3/api-docs`, `/`)
  - upstreamTarget (string, required; `api:8080` or `frontend:80`)
  - priority (integer, required; menor valor implica mayor prioridad)
  - spaFallback (boolean, required; true solo en regla frontend)
- Validation Rules:
  - Path API rules must have higher priority than `/`.
  - `/` rule must forward unknown non-API paths to frontend index.
  - No overlapping rules con distinto target y misma prioridad.

## Entity: ForwardedRequest
- Description: Solicitud HTTP reenviada por proxy hacia un upstream interno.
- Fields:
  - method (enum: GET, POST, PUT, DELETE, OPTIONS)
  - requestPath (string, required)
  - targetService (enum: `api`, `frontend`)
  - host (string, required)
  - xRealIp (string, required)
  - xForwardedFor (string, required)
  - xForwardedProto (string, required)
  - authorizationPresent (boolean)
  - responseStatus (integer)
- Validation Rules:
  - `Host`, `X-Real-IP`, `X-Forwarded-For`, `X-Forwarded-Proto` must always be present in upstream request.
  - If request has Bearer token, proxy must preserve Authorization header unchanged.

## Entity: ContainerNetworkTopology
- Description: Topologia de red de Compose para aislar servicios internos.
- Fields:
  - networkName (string, required)
  - internalOnlyServices (array, required; includes `db`, `api`, `frontend`)
  - publicServices (array, required; includes only `reverse-proxy`)
  - debugOverrides (array, optional; includes DB port override file)
- Validation Rules:
  - Exactly one service with host port publishing in base compose.
  - DB host port publishing is forbidden in base compose and allowed only in debug override.

## Entity: HealthValidationRun
- Description: Ejecucion de validacion operativa previa a pruebas funcionales.
- Fields:
  - composeBuildMode (enum: `clean`)
  - proxyHealthStatus (enum: `healthy|unhealthy`)
  - apiHealthStatus (enum: `healthy|unhealthy`)
  - rootStatusCode (integer, expected=200)
  - swaggerStatusCode (integer, expected=200)
  - authLoginStatusCode (integer, expected=200)
  - empleadosWithTokenStatusCode (integer, expected=200)
  - timestamp (datetime)
- State Transitions:
  - `initialized` -> `containers_up`
  - `containers_up` -> `health_verified`
  - `health_verified` -> `functional_tests_passed`
  - Any state -> `failed` when one expected status differs from contract.
