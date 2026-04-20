param(
    [string]$BaseUrl = "http://localhost"
)

$ErrorActionPreference = "Stop"

$response = Invoke-WebRequest -Uri "$BaseUrl/swagger-ui" -Method Get -UseBasicParsing
if ($response.StatusCode -ne 200) {
    throw "Expected HTTP 200 for GET /swagger-ui but received $($response.StatusCode)."
}

Write-Output "PASS: GET /swagger-ui returned HTTP 200"
