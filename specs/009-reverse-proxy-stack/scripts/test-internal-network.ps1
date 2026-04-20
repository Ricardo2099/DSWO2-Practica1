$ErrorActionPreference = "Stop"

$network = docker network ls --format "{{.Name}}" | Where-Object { $_ -like "*_internal-net" -or $_ -eq "internal-net" } | Select-Object -First 1
if (-not $network) {
    throw "No docker internal-net network was found."
}

$inspect = docker network inspect $network | ConvertFrom-Json
$containerNames = @($inspect[0].Containers.PSObject.Properties.Value.Name)
$expected = @("empleados-postgres", "empleados-api", "empleados-frontend", "empleados-reverse-proxy")

$missing = $expected | Where-Object { $_ -notin $containerNames }
if ($missing.Count -gt 0) {
    throw "Missing containers in network ${network}: $($missing -join ', ')"
}

Write-Output "PASS: all services are attached to network $network"
