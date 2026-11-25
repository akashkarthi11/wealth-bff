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


## API routing

- Frontend-facing base path: `/wealth/api/v1`.
- The BFF forwards the original HTTP method, path suffix, query string, request body, cookies and authorization headers to BSS.
- Internal BSS base path: `/wealth-service/api/v1`.
- Configure the BSS origin with `WEALTH_BSS_BASE_URL` (for example, the private service URL). The BFF returns upstream status, response body and response headers, including session cookies.
- Business rules and persistence belong in BSS; BFF is a compatibility and orchestration boundary only.
