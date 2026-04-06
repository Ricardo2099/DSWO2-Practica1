# DSWO2-Practica1 Development Guidelines

Auto-generated from all feature plans. Last updated: 2026-03-26

## Active Technologies
- Java 17 + Spring Boot 3.2.8, Spring Web, Spring Data JPA, Spring Security, Bean Validation, Flyway, springdoc-openapi (003-crud-departamentos)
- PostgreSQL 15+ (003-crud-departamentos)
- TypeScript 5.x + HTML/CSS + Angular 22 LTS + Angular Router, Angular HttpClient, Angular Reactive Forms, Angular Material, RxJS, Cypress 13.x (005-cypress-e2e-strategy)
- `localStorage` para sesion de cliente (alcance academico) + PostgreSQL en backend (sin cambios de modelo) (005-cypress-e2e-strategy)
- `localStorage` para token/sesion de cliente + PostgreSQL en backend (sin cambios de modelo) (005-cypress-e2e-strategy)
- TypeScript 5.x + Angular 22 LTS + Angular Router, HttpClient, Reactive Forms, Angular Material, RxJS (006-crud-empleados-frontend)
- N/A (cliente web; sesion ya gestionada por flujo auth existente) (006-crud-empleados-frontend)
- TypeScript 5.9 + Angular 21.x (workspace actual) + Cypress 13.x + Cypress, Angular standalone app, HttpClient, Angular Material, JWT Bearer backend auth (008-cypress-e2e-suite)
- `localStorage` para token/rol durante escenarios autenticados; evidencias en `frontend/cypress/screenshots` y `frontend/cypress/videos` (008-cypress-e2e-suite)

- Java 17 + Spring Boot 3.2+, Spring Web, Spring Data JPA, Spring Security (Basic Auth), Flyway, springdoc-openapi, Bean Validation (001-crud-empleados)

## Project Structure

```text
backend/
frontend/
tests/
```

## Commands

# Add commands for Java 17

## Code Style

Java 17: Follow standard conventions

## Recent Changes
- 008-cypress-e2e-suite: Added TypeScript 5.9 + Angular 21.x (workspace actual) + Cypress 13.x + Cypress, Angular standalone app, HttpClient, Angular Material, JWT Bearer backend auth
- 006-crud-empleados-frontend: Added TypeScript 5.x + Angular 22 LTS + Angular Router, HttpClient, Reactive Forms, Angular Material, RxJS
- 005-cypress-e2e-strategy: Added TypeScript 5.x + HTML/CSS + Angular 22 LTS + Angular Router, Angular HttpClient, Angular Reactive Forms, Angular Material, RxJS, Cypress 13.x


<!-- MANUAL ADDITIONS START -->
<!-- MANUAL ADDITIONS END -->
