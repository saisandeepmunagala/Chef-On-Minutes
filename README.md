# Chef-On-Minutes

A home-lunch booking platform: customers book a chef who comes to their home,
prepares lunch, and is paid per session.

## Structure

- `backend/` — Spring Boot 3 (Java 17) REST API. Maven project.
- `frontend/` — React 18 + Vite SPA.

## Status

Templates/skeleton only — entities, DTOs, repositories, controllers, and
routes are scaffolded with `TODO`s. No business logic implemented yet.
Pending review/discussion before implementation.

## Local run (once implemented)

Backend:
```
cd backend
./mvnw spring-boot:run
```
Runs on `http://localhost:8080`, H2 in-memory DB, console at `/h2-console`.

Frontend:
```
cd frontend
npm install
npm run dev
```
Runs on `http://localhost:5173`.

CORS is configured in `backend/src/main/java/com/chefonminutes/config/CorsConfig.java`
to allow the frontend origin (`app.cors.allowed-origins` in `application.yml`).
