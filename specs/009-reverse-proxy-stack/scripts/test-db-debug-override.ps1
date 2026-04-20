$ErrorActionPreference = "Stop"

$config = docker compose -f docker-compose.yml -f docker-compose.debug-db.yml config
$configText = $config -join "`n"
if ($configText -notmatch 'published:\s+"5432"' -or $configText -notmatch 'target:\s+5432') {
    throw "Expected db debug override to publish 5432:5432, but it was not found in composed config."
}

Write-Output "PASS: debug override exposes DB port only when override file is included"
