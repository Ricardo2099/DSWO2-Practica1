param(
    [string]$BaseUrl = "http://localhost",
    [string]$Token = ""
)

$ErrorActionPreference = "Stop"

if (-not $Token) {
    $Token = & "$PSScriptRoot/test-auth-login.ps1" -BaseUrl $BaseUrl
}

$headers = @{ Authorization = "Bearer $Token" }
$response = Invoke-WebRequest -Uri "$BaseUrl/api/v1/empleados" -Method Get -Headers $headers -UseBasicParsing
if ($response.StatusCode -ne 200) {
    throw "Expected HTTP 200 for GET /api/v1/empleados with token but received $($response.StatusCode)."
}

Write-Output "PASS: GET /api/v1/empleados with Bearer token returned HTTP 200"
