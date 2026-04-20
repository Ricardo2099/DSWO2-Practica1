$ErrorActionPreference = "Stop"

$containers = docker ps --format "{{.Names}}|{{.Ports}}"
if (-not $containers) {
    throw "No running containers were detected."
}

$invalid = @()
foreach ($line in $containers) {
    $parts = $line.Split('|', 2)
    $name = $parts[0]
    $ports = if ($parts.Count -gt 1) { $parts[1] } else { "" }

    if ($ports -match "0\.0\.0\.0:" -or $ports -match ":::" ) {
        if ($name -ne "empleados-reverse-proxy") {
            $invalid += "$name => $ports"
        }
    }
}

if ($invalid.Count -gt 0) {
    throw "Only empleados-reverse-proxy should publish host ports. Found: $($invalid -join '; ')"
}

Write-Output "PASS: only empleados-reverse-proxy publishes host ports"
