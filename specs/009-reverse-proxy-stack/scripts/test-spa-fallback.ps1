param(
    [string]$BaseUrl = "http://localhost"
)

$ErrorActionPreference = "Stop"

$response = Invoke-WebRequest -Uri "$BaseUrl/empleados/123" -Method Get -UseBasicParsing
if ($response.StatusCode -ne 200) {
    throw "Expected HTTP 200 for SPA fallback route but received $($response.StatusCode)."
}

if ($response.Content -notmatch "app-root") {
    throw "Expected SPA shell content for fallback route, but app-root marker was not found."
}

Write-Output "PASS: SPA fallback route returned HTTP 200 and Angular shell content"
