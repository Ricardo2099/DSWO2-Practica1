<!--
Sync Impact Report
Version change: N/A -> 1.0.0
Modified principles:
- Placeholder -> I. Spring Boot 3 + Java 17 Baseline
- Placeholder -> II. Basic Authentication & Transport Security
- Placeholder -> III. PostgreSQL Source of Truth
- Placeholder -> IV. Dockerized Environments & Operations
- Placeholder -> V. API Documentation & Test Discipline
Added sections:
- Technology Stack Guardrails
- Development Workflow & Quality Gates
Removed sections: None
Templates requiring updates:
- .specify/templates/plan-template.md ✅ Constitution Check remains accurate
- .specify/templates/spec-template.md ✅ Already enforces prioritized, testable stories
- .specify/templates/tasks-template.md ✅ Matches independent story execution model
- .specify/templates/commands (missing directory) ⚠ Create if command-specific guardrails are later introduced
Follow-up TODOs: None
-->

# DSWO2-Practica1 Backend Constitution

## Core Principles

### I. Spring Boot 3 + Java 17 Baseline
- All backend services MUST compile against Spring Boot 3.2+ and Java 17 LTS; deviations require an ADR approved by two maintainers.
- Shared libraries MUST be published via a managed BOM; ad-hoc jar drops are prohibited to preserve reproducible builds.
- Runtime profiles are limited to `local`, `test`, and `production`; new profiles demand explicit review of configuration drift.
*Rationale: locking the platform stack prevents classpath drift and simplifies upgrade planning.*

### II. Basic Authentication & Transport Security
- Every HTTP endpoint MUST enforce HTTP Basic Authentication with credentials stored in a secrets manager or encrypted `.env`; no hard-coded secrets.
- TLS termination MUST happen before any request hits the Spring context; even internal Docker networks require self-signed certificates for testing.
- Security-sensitive code MUST include negative-path tests (invalid creds, replay, brute force) before merge.
*Rationale: consistent auth keeps attack surface predictable and reviewable.*

### III. PostgreSQL Source of Truth
- PostgreSQL (15+) is the only persistent store; introducing another database needs architectural approval and migration planning.
- Schema changes MUST be implemented with Flyway (SQL or Java migrations) and executed in CI before deployments.
- Data access MUST flow through Spring Data or jOOQ with explicit transaction boundaries; raw SQL must use prepared statements to avoid injection.
*Rationale: one database contract simplifies scaling, rollback, and auditing.*

### IV. Dockerized Environments & Operations
- The repository MUST ship Dockerfiles for the app and a `docker-compose.yml` wiring the Spring Boot service with PostgreSQL and supporting services (e.g., pgAdmin).
- Containers MUST expose `/actuator/health` and log in JSON to STDOUT/STDERR for aggregation; no log files inside containers.
- Environment parity is enforced through `.env.example`; deviations between local, CI, and production compose stacks must be documented before deployment.
*Rationale: Docker-first workflows guarantee that what we test is what we ship.*

### V. API Documentation & Test Discipline
- All REST endpoints MUST be described via OpenAPI 3 and exposed through Swagger UI at `/swagger-ui`; documentation updates are blocking for feature completion.
- Contract tests (Spring MockMvc or WebTestClient) and integration tests (Testcontainers + PostgreSQL) MUST pass before merging; minimum line coverage is 85% for domain and API packages.
- Error responses MUST follow a documented problem-details schema to keep clients predictable.
*Rationale: living documentation plus tests keep the API consumable and regression-safe.*

## Technology Stack Guardrails
- **Language/Framework**: Java 17 with Spring Boot 3, Spring Security, Spring Data/JPA, and Flyway for migrations.
- **Persistence**: PostgreSQL running in Docker; Testcontainers drives automated testing, while production relies on managed Postgres with identical schemas.
- **Authentication**: HTTP Basic Auth backed by hashed credentials; Secrets injected via Spring Config or Docker secrets, never committed to VCS.
- **Documentation & Tooling**: Swagger/OpenAPI for HTTP surface, Maven or Gradle Wrapper checked in, and Actuator for health/metrics.
- **Infrastructure**: Docker (desktop or engine) is required to boot the stack; docker-compose orchestrates app + db locally and in CI.

## Development Workflow & Quality Gates
1. **Plan**: Every feature references this constitution in the `/speckit.plan` Constitution Check. Work cannot start until all blocking gates (stack alignment, auth, DB impact) are satisfied.
2. **Branching**: Use feature branches named `feat/<ticket>`; rebase on `main` before opening a PR to minimize drift.
3. **Testing**: Run unit, integration (Testcontainers), and contract suites locally; CI reruns them plus static analysis (SpotBugs, Checkstyle) and verifies coverage ≥85%.
4. **Documentation**: Update Swagger docs, Flyway migrations, and `.env.example` in the same PR as the code change.
5. **Reviews**: Two reviewers required—one for security/auth impact, one for data/model impact. Reviewers confirm Docker parity by running `docker compose up` using the branch build.
6. **Release**: Tags follow `$MAJOR.$MINOR.$PATCH`; release artifacts are container images published to the registry declared in the plan template.

## Governance
- This constitution supersedes conflicting guidance. Feature plans, specs, and tasks MUST reference any exception along with mitigation.
- Amendments follow Semantic Versioning: PATCH for clarifications, MINOR for new principles/sections, MAJOR for removals or rule inversions.
- Proposed changes require an ADR, consensus from two maintainers, and an update to this file plus any dependent templates before adoption.
- Compliance is reviewed during PR checks and a quarterly architecture review covering auth posture, database migrations, and Docker health.
- Violations block merges until rectified or waived via documented, time-bound exceptions approved in writing.

**Version**: 1.0.0 | **Ratified**: 2026-02-25 | **Last Amended**: 2026-02-25
