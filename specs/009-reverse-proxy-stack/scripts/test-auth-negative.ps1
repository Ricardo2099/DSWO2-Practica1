param(
    [string]$BaseUrl = "http://localhost"
)

$ErrorActionPreference = "Stop"

try {
    Invoke-WebRequest -Uri "$BaseUrl/api/v1/empleados" -Method Get -UseBasicParsing | Out-Null
    throw "Expected HTTP 401 for unauthenticated request, but call succeeded."
} catch {
    if (-not $_.Exception.Response) {
        throw
    }

    $status = [int]$_.Exception.Response.StatusCode
    if ($status -ne 401) {
        throw "Expected HTTP 401 for unauthenticated request, but received $status."
    }
}

Write-Output "PASS: GET /api/v1/empleados without token returned HTTP 401"
