param(
    [string]$BaseUrl = "http://localhost",
    [string]$Correo = "admin@empleados.local",
    [string]$Contrasena = "password"
)

$ErrorActionPreference = "Stop"
$body = @{ correo = $Correo; contrasena = $Contrasena } | ConvertTo-Json

$response = Invoke-WebRequest -Uri "$BaseUrl/auth/login" -Method Post -ContentType "application/json" -Body $body -UseBasicParsing
if ($response.StatusCode -ne 200) {
    throw "Expected HTTP 200 for POST /auth/login but received $($response.StatusCode)."
}

$payload = $response.Content | ConvertFrom-Json
if (-not $payload.token) {
    throw "Expected auth response to include token property."
}

Write-Output $payload.token
