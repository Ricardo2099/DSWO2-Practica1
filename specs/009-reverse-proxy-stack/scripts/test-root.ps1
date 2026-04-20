param(
    [string]$BaseUrl = "http://localhost"
)

$ErrorActionPreference = "Stop"

$response = Invoke-WebRequest -Uri "$BaseUrl/" -Method Get -UseBasicParsing
if ($response.StatusCode -ne 200) {
    throw "Expected HTTP 200 for GET / but received $($response.StatusCode)."
}

Write-Output "PASS: GET / returned HTTP 200"
