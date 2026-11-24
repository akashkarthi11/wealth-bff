# Wealth BFF

Spring Boot backend-for-frontend for the Wealth application.

## Responsibilities
- Expose frontend-facing REST APIs.
- Coordinate calls to Wealth internal services.
- Own API request validation, response composition and frontend compatibility.
- Enforce authentication and authorization at the API boundary.

## Technology baseline
- Java 21
- Spring Boot 3.5.x
- Maven Wrapper
- Spring Web, Validation, Actuator and Spring Security as required
- Docker image targeting OCI Ampere `linux/arm64`

## Development
The `development` branch is the only branch intended for automatic Oracle Cloud development deployment. Production deployment from `main` is not configured.

Do not commit secrets, database URLs, private keys, tokens, or personal financial data.
